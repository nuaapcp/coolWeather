package com.test.coolweather.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.coolweather.common.CommonConstants;
import com.test.coolweather.dataBase.dao.CityDAO;
import com.test.coolweather.dataBase.dao.CountyDAO;
import com.test.coolweather.dataBase.dao.LocationDAO;
import com.test.coolweather.dataBase.dao.ProvinceDAO;
import com.test.coolweather.dataBase.entity.CityEntity;
import com.test.coolweather.dataBase.entity.CountyEntity;
import com.test.coolweather.dataBase.entity.LocationInfoCN;
import com.test.coolweather.dataBase.entity.ProvinceEntity;

@Database(entities = {CityEntity.class, CountyEntity.class, ProvinceEntity.class, LocationInfoCN.class}, version = 1, exportSchema = false)
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

    public abstract ProvinceDAO provinceDAO();

    public abstract CityDAO cityDAO();

    public abstract CountyDAO countyDAO();

    public abstract LocationDAO locationDAO();
}
