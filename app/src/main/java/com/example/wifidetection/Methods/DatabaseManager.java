package com.example.wifidetection.Methods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wifidetection.data.HttpHeader;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_NAME = "detectiondata";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "monitorData";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_VALUE = "value";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<HttpHeader> getAllHeaders() {
        List<HttpHeader> headers = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                headers.add(new HttpHeader(name, value));
            }
            cursor.close();
        }
        return headers;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " TEXT PRIMARY KEY," +
                    COLUMN_VALUE + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase o = null;
            return o;
        }
    }
}
