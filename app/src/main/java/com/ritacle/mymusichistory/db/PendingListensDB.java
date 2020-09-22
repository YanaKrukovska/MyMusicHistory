package com.ritacle.mymusichistory.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {PendingListenEntity.class}, version = 1, exportSchema = false)
public abstract class PendingListensDB extends RoomDatabase {
    private static final String TAG = PendingListensDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "pendinglistenslist";
    private static PendingListensDB sInstance;

    public static PendingListensDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PendingListensDB.class, PendingListensDB.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PendingListenDao pendingListenDao();
}