package com.test.coolweather.network.model

data class AirQuality(
        val aqi: Aqi,
        val co: Number,
        val description: Description,
        val no2: Int,
        val o3: Int,
        val pm10: Int,
        val pm25: Int,
        val so2: Int
)