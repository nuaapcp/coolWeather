package com.test.coolweather.dataBase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.coolweather.dataBase.entity.CityEntity;

import java.util.List;

@Dao
public abstract class CityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<CityEntity> cityEntities);

    @Query("select cityName from CityEntity where provinceId =:provinceId")
    public abstract List<String> getCityNamesByProvinceId(int provinceId);

    @Query("select * from CityEntity where cityName =:cityName")
    public abstract CityEntity getCityByName(String cityName);


}
