package com.example.wifidetection.service;

import static com.example.wifidetection.detectionHelper.getContext;

import android.content.Context;

import com.android.local.service.annotation.Get;
import com.android.local.service.annotation.Page;
import com.android.local.service.annotation.Service;
import com.example.wifidetection.ServiceDataProvider;
import com.example.wifidetection.data.detectionData;
import com.example.wifidetection.detectionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service(port = 5554)
public abstract class detectionService {

    @Page("index")
    public String showMonitorPage() {
        return "monitor_index.html";
    }

    @Page("sp_index")
    public String showSpPage() {
        return "sp_index.html";
    }

    @Page("mqtt_index")
    public String showMqttPage() {
        return "mqtt_index.html";
    }

    @Get("query")
    public List<detectionData> queryMonitorData(int limit, int offset) {
        return ServiceDataProvider.getMonitorDataList(limit, offset);
    }

    @Get("clean")
    public void cleanMonitorData() {
        detectionHelper.deleteAll();
    }

    /*
    @Get("autoFetch")
    public List<detectionData> autoFetchData(long lastFetchId) {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (lastFetchId != lastUpdateDataId || lastFetchId == 0L) {
                return ServiceDataProvider.getMonitorDataByLastId(lastFetchId);
            } else {
                return new ArrayList<>();
            }
        }
    }
     */

    @Get("sharedPrefs")
    public HashMap<String, HashMap<String, detectionHelper.SpValueInfo>> getSharedPrefsFilesData() {
        Context context = getContext();
        return detectionHelper.getSharedPrefsFilesData(context);
    }

    @Get("getSharedPrefsByFileName")
    public HashMap<String, detectionHelper.SpValueInfo> getSharedPrefsByFileName(String fileName) {
        Context context = getContext();
        return detectionHelper.getSpFile(context, fileName);
    }

    @Get("updateSpValue")
    public void updateSpValue(String fileName, String key, String value, String valueType) {
        Context context = getContext(); // 获取 Context 对象
        Object realValue;
        switch (valueType) {
            case "Int":
                realValue = Integer.parseInt(value);
                break;
            case "Double":
                realValue = Double.parseDouble(value);
                break;
            case "Long":
                realValue = Long.parseLong(value);
                break;
            case "Float":
                realValue = Float.parseFloat(value);
                break;
            case "Boolean":
                realValue = Boolean.parseBoolean(value);
                break;
            default:
                realValue = value;
                break;
        }
        detectionHelper.updateSpValue(context, fileName, key, realValue);
    }
}
