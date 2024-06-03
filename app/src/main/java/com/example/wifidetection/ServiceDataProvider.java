package com.example.wifidetection;

import com.example.wifidetection.data.detectionData;

import java.util.List;

public class ServiceDataProvider {
    public static List<detectionData> getMonitorDataList(int limit, int offset) {
        return detectionHelper.getMonitorDataList(limit, offset);
    }

    public static List<detectionData> getMonitorDataByLastId(long lastUpdateDataId) {
        return detectionHelper.getMonitorDataByLastId(lastUpdateDataId);
    }
}
