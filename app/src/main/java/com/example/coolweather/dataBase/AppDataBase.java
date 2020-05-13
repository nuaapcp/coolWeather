package com.example.coolweather.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.coolweather.common.CommonConstants;

@Database(entities = {}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            Builder<AppDataBase> dataBaseBuilder = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    CommonConstants.COOLWEATHER_DB_NAME)
                    .allowMainThreadQueries()
                    .enableMultiInstanceInvalidation()
                    .fallbackToDestructiveMigration();
            instance = dataBaseBuilder.build();
        }
        return instance;
    }
}
