package com.hk.ijournal.models.repository.localdata.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hk.ijournal.models.DiaryUser;
import com.hk.ijournal.models.repository.localdata.dao.UserDao;

@Database(entities = DiaryUser.class, exportSchema = false, version = 1)
public abstract class IJDatabase extends RoomDatabase implements DBSchema {
    private static IJDatabase sInstance;

    public static synchronized IJDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, IJDatabase.class, DBNAME).fallbackToDestructiveMigration().build();
        }
        return sInstance;
    }

    public abstract UserDao userDao();
}
