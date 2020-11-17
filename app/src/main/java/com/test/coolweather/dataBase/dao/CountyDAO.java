package com.test.coolweather.dataBase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.coolweather.dataBase.entity.CountyEntity;

import java.util.List;

@Dao
public abstract class CountyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<CountyEntity> countyEntities);

    @Query("select countyName from CountyEntity where cityId =:cityId")
    public abstract List<String> getCountyNamesByCityId(int cityId);
}
