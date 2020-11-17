package com.test.coolweather.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationInfoCN(@PrimaryKey val adCode: String, val formatted_address: String, val cityName: String, val province: String, val city: String, val district: String, val lng: String, val lat: String)