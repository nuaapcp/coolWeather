package com.test.coolweather.dataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CountyEntity {
    @ColumnInfo
    private String countyName;
    @PrimaryKey
    @ColumnInfo
    private int countyId;
    @ColumnInfo
    private String weatherId;
    @ColumnInfo
    private int cityId;

    public CountyEntity(String countyName, int countyId, String weatherId, int cityId) {
        this.countyName = countyName;
        this.countyId = countyId;
        this.weatherId = weatherId;
        this.cityId = cityId;
    }


    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
