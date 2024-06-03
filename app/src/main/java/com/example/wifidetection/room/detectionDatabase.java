package com.example.wifidetection.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.wifidetection.data.detectionData;

@Database(entities = {detectionData.class}, version = 1, exportSchema = false)
public abstract class detectionDatabase extends RoomDatabase {
    public abstract detectionDao detectionDao();
}