package com.example.wifidetection;

import android.content.Context;
import android.util.Log;

import com.example.wifidetection.data.propertiesData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MonitorProperties {
    private static final String TAG = "MonitorHelper";
    private static final String KEY_MONITOR_PORT = "monitor.port";
    private static final String KEY_MONITOR_DB_NAME = "monitor.dbName";
    private static final String KEY_WHITE_CONTENT_TYPES = "monitor.whiteContentTypes";
    private static final String KEY_WHITE_HOSTS = "monitor.whiteHosts";
    private static final String KEY_BLACK_HOSTS = "monitor.blackHosts";
    private static final String KEY_IS_FILTER_IPADDRESS_HOST = "monitor.isFilterIPAddressHost";

    private static final String ASSETS_FILE_NAME = "monitor.properties";

    public static propertiesData paramsProperties() {
        propertiesData propertiesData = null;
        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            Context context = detectionHelper.getContext();
            if (context == null) {
                Log.d(TAG, "初始化获取context失败");
                return propertiesData;
            }
            inputStream = context.getAssets().open(ASSETS_FILE_NAME);
            if (inputStream != null) {
                properties.load(inputStream);
                String port = properties.getProperty(KEY_MONITOR_PORT);
                String dbName = properties.getProperty(KEY_MONITOR_DB_NAME);
                String whiteContentTypes = properties.getProperty(KEY_WHITE_CONTENT_TYPES);
                String whiteHosts = properties.getProperty(KEY_WHITE_HOSTS);
                String blackHosts = properties.getProperty(KEY_BLACK_HOSTS);
                boolean isFilterIPAddressHost = Boolean.parseBoolean(properties.getProperty(KEY_IS_FILTER_IPADDRESS_HOST, "false"));

                propertiesData = new propertiesData(port, dbName, whiteContentTypes, whiteHosts, blackHosts, isFilterIPAddressHost);
            }
        } catch (IOException e) {
            if (e instanceof java.io.FileNotFoundException) {
                Log.d(TAG, "not found monitor.properties");
            } else {
                e.printStackTrace();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertiesData;
    }
}
