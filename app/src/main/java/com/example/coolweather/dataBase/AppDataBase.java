package com.example.coolweather.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.coolweather.common.CommonConstants;
import com.example.coolweather.dataBase.entity.CityEntity;
import com.example.coolweather.dataBase.entity.CountyEntity;
import com.example.coolweather.dataBase.entity.ProvinceEntity;

@Database(entities = {CityEntity.class, CountyEntity.class, ProvinceEntity.class}, version = 1, exportSchema = false)
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
