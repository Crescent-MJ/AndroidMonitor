package com.example.wifidetection;

import static android.net.Proxy.getPort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.android.local.service.core.ALSHelper;
import com.android.local.service.core.data.ServiceConfig;
import com.example.wifidetection.data.detectionData;
import com.example.wifidetection.data.propertiesData;
import com.example.wifidetection.interceptor.MonitorInterceptor;
import com.example.wifidetection.room.detectionDao;
import com.example.wifidetection.room.detectionDatabase;
import com.example.wifidetection.service.detectionService;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Interceptor;

public class detectionHelper {

    public static final String TAG = "MonitorHelper";

    public static Context context = null;
    public static detectionDatabase monitorDb = null;

    public static int port = 0;

    // Used for bytecode manipulation with ASM to hook into OKHTTP
    public static List<Interceptor> hookInterceptors = Arrays.asList(
            //new MonitorMockInterceptor(),
            new MonitorInterceptor()
            //new MonitorWeakNetworkInterceptor(),
            //new MonitorMockResponseInterceptor()
    );

    public static String whiteContentTypes = "text/html";
    private static String defaultContentTypes = "text/plain";
    public static String whiteHosts = null;
    public static String blackHosts = null;
    public static boolean isFilterIPAddressHost = false;

    public static boolean isOpenMonitor = true;

    private static ExecutorService singleThreadExecutor = null;

    public static boolean isWhiteContentType(String contentType) {
        return whiteContentTypes != null && whiteContentTypes.contains(contentType);
    }

    static {
        //whiteContentTypes.add("application/json");
        //whiteContentTypes.add("text/html");
        // Add other content types as needed
    }

    private static void threadExecutor(Runnable action) {
        if (singleThreadExecutor == null || singleThreadExecutor.isShutdown()) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        singleThreadExecutor.execute(action);
    }

    @SuppressLint("StaticFieldLeak")
    public static void init(Context context) {
        detectionHelper.context = context;
        new Thread(() -> {
            propertiesData propertiesData = new MonitorProperties().paramsProperties();
            String dbName = propertiesData != null ? propertiesData.getDbName() : "monitor_room_db";
            List<String> contentTypes = propertiesData != null ? Collections.singletonList(propertiesData.getWhiteContentTypes()) : null;
            whiteContentTypes = contentTypes != null && !contentTypes.isEmpty() ? contentTypes.toString() : defaultContentTypes;
            whiteHosts = propertiesData != null ? propertiesData.getWhiteHosts() : null;
            blackHosts = propertiesData != null ? propertiesData.getBlackHosts() : null;
            port = propertiesData != null ? Integer.parseInt(propertiesData.getPort()) : 0;
            isFilterIPAddressHost = propertiesData != null && propertiesData.isFilterIPAddressHost();
            initMonitorDataDao(context, dbName);
        }).start();
    }

    public static void initMonitorDataDao(Context context, String dbName) {
        if (monitorDb == null) {
            monitorDb = Room.databaseBuilder(context.getApplicationContext(), detectionDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    private static detectionDao getMonitorDataDao() {
        return monitorDb != null ? monitorDb.detectionDao() : null;
    }

    public static void insert(detectionData detectionData) {
        long lastUpdateDataId = detectionData.getId();
        Objects.requireNonNull(getMonitorDataDao()).insert(detectionData);
    }

    private void initPCService(Context context, int port) {
        ALSHelper ALSHelper;
        ALSHelper = null;
        ALSHelper.init(context);
        if (port > 0) {
            ALSHelper.startService(new ServiceConfig(detectionService.class, port));
        } else {
            ALSHelper.startService(new ServiceConfig(detectionService.class, 0));
        }
        detectionHelper.port = ALSHelper.getServiceList().isEmpty() ? 0 : ALSHelper.getServiceList().get(0).getPort();
    }

    public static void insertAsync(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return;
        singleThreadExecutor.execute(() -> {
            try {
                detectionData detection = new Gson().fromJson(new Gson().toJson(map), detectionData.class);
                insert(detection);
            } catch (Exception e) {
                Log.d(TAG, "insertAsync--" + e.getMessage());
            }
        });
    }

    public static void update(detectionData detectionData) {
        getMonitorDataDao().update(detectionData);
    }

    public static void deleteAll() {
        long lastUpdateDataId = 0L;
        getMonitorDataDao().deleteAll();
    }

    public static boolean isDetectionDataNull() {
        return getMonitorDataDao() != null;
    }

    public static LiveData<List<detectionData>> getMonitorDataListForAndroid(int limit, int offset) {
        return Objects.requireNonNull(getMonitorDataDao()).queryByOffsetForAndroid(limit, offset);
    }

    public static List<detectionData> getMonitorDataList(int limit, int offset) {
        return Objects.requireNonNull(getMonitorDataDao()).queryByOffset(limit, offset);
    }

    public static LiveData<List<detectionData>> getMonitorDataByLastIdForAndroid(long lastUpdateDataId) {
        return Objects.requireNonNull(getMonitorDataDao()).queryByLastIdForAndroid(lastUpdateDataId);
    }

    public static List<detectionData> getMonitorDataByLastId(long lastUpdateDataId) {
        return Objects.requireNonNull(getMonitorDataDao()).queryByLastId(lastUpdateDataId);
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isOpenMonitor() {
        return true;
    }

    public static boolean isFilterIPAddressHost() {
        return false;
    }

    public static HashMap<String, HashMap<String, SpValueInfo>> getSharedPrefsFilesData(Context context) {
        if (context == null) {
            return new HashMap<>();
        }
        HashMap<String, HashMap<String, SpValueInfo>> map = new HashMap<>();
        File targetFile = new File(context.getCacheDir().getParentFile().getAbsolutePath() + "/shared_prefs");
        if (!targetFile.exists()) {
            return new HashMap<>();
        }
        if (targetFile.isDirectory()) {
            File[] files = targetFile.listFiles();
            if (files != null) {
                for (File spFile : files) {
                    Log.d(TAG, "getSharedPrefsFiles: " + spFile.getName());
                    String fileName = spFile.getName();
                    if (!fileName.isEmpty()) {
                        String name = fileName.endsWith(".xml") ? fileName.split("\\.xml")[0] : fileName;
                        HashMap<String, SpValueInfo> value = getSpFile(context, name);
                        map.put(name, value);
                    }
                }
            }
        }
        return map;
    }

    public static HashMap<String, SpValueInfo> getSpFile(Context context, String name) {
        if (name == null || name.isEmpty()) {
            return new HashMap<>();
        }
        HashMap<String, SpValueInfo> map = new HashMap<>();
        Map<String, ?> all = context.getSharedPreferences(name, Context.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            SPValueType valueType;
            Object value = entry.getValue();
            if (value instanceof Integer) {
                valueType = SPValueType.Int;
            } else if (value instanceof Double) {
                valueType = SPValueType.Double;
            } else if (value instanceof Float) {
                valueType = SPValueType.Float;
            } else if (value instanceof Long) {
                valueType = SPValueType.Long;
            } else if (value instanceof Boolean) {
                valueType = SPValueType.Boolean;
            } else {
                valueType = SPValueType.String;
            }
            String key = entry.getKey();
            if (!key.isEmpty()) {
                map.put(key, new SpValueInfo(value, valueType));
            }
        }
        return map;
    }

    public static void updateSpValue(Context context, String fileName, String key, Object value) {
        SPUtils.saveValue(context, fileName, key, value);
    }
    public static class SpValueInfo {
        private Object value;
        private SPValueType type;

        public SpValueInfo(Object value, SPValueType type) {
            this.value = value;
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public SPValueType getType() {
            return type;
        }
    }

    public enum SPValueType {
        Int,
        Double,
        Float,
        Long,
        Boolean,
        String
    }

    static class SPUtils {
        public static void saveValue(Context context, String fileName, String key, Object value) {
            android.content.SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
            if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                editor.putFloat(key, ((Double) value).floatValue());
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else {
                editor.putString(key, value.toString());
            }
            editor.apply();
        }
    }
}
