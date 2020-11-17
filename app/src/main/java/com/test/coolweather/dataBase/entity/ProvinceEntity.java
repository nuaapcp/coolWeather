package com.test.coolweather.dataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProvinceEntity {
    @ColumnInfo
    private String provinceName;
    @PrimaryKey
    @ColumnInfo
    private int provinceCode;

    public ProvinceEntity(String provinceName, int provinceCode) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
    }


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
