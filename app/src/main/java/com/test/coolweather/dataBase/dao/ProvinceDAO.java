package com.test.coolweather.dataBase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.coolweather.dataBase.entity.ProvinceEntity;

import java.util.List;

@Dao
public abstract class ProvinceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ProvinceEntity> provinces);

    @Query("select provinceName from ProvinceEntity")
    public abstract List<String> getAllProvinceNames();

    @Query("select * from ProvinceEntity")
    public abstract List<ProvinceEntity> getAllProvince();

    /**
     * procinceName只有34个且不重复所以返回的id是唯一的
     *
     * @param provinceName
     * @return
     */
    @Query("select provinceCode from ProvinceEntity where provinceName =:provinceName")
    public abstract int getProvinceId(String provinceName);

    @Query("select provinceName from ProvinceEntity where provinceCode =:provinceId")
    public abstract String getProvinceName(int provinceId);
}
