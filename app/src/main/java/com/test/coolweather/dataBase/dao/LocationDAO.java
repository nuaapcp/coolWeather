package com.test.coolweather.dataBase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.coolweather.dataBase.entity.LocationInfoCN;

@Dao
public abstract class LocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(LocationInfoCN locationInfoCN);

    @Query("select * from LocationInfoCN where city like '%'|| :cityName||'%' AND cityName like '%'|| :countyName||'%'")
    public abstract LocationInfoCN getLocation(String cityName, String countyName);
}
