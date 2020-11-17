package com.test.coolweather.dataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CityEntity {
    @ColumnInfo
    private String cityName;
    @PrimaryKey
    @ColumnInfo
    private int cityCode;
    @ColumnInfo
    private int provinceId;

    public CityEntity(String cityName, int cityCode, int provinceId) {
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.provinceId = provinceId;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
