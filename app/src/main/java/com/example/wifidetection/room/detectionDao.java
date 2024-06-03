package com.example.wifidetection.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.wifidetection.data.detectionData;
import java.util.List;

@Dao
public interface detectionDao {
    @Query("SELECT * FROM detection WHERE id > :lastId ORDER BY id DESC")
    LiveData<List<detectionData>> queryByLastIdForAndroid(long lastId);

    @Query("SELECT * FROM detection ORDER BY id DESC LIMIT :limit OFFSET :offset")
    LiveData<List<detectionData>> queryByOffsetForAndroid(int limit, int offset);

    @Query("SELECT * FROM detection")
    LiveData<List<detectionData>> queryAllForAndroid();

    @Query("SELECT * FROM detection WHERE id > :lastId ORDER BY id DESC")
    List<detectionData> queryByLastId(long lastId);

    @Query("SELECT * FROM detection ORDER BY id DESC LIMIT :limit OFFSET :offset")
    List<detectionData> queryByOffset(int limit, int offset);

    @Query("SELECT * FROM detection")
    List<detectionData> queryAll();

    @Insert
    void insert(detectionData data);

    @Update
    void update(detectionData data);

    @Query("DELETE FROM detection")
    void deleteAll();
}