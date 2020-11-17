package com.test.coolweather.network.model

data class Realtime(
        val air_quality: AirQuality,
        val apparent_temperature: Double,
        val cloudrate: Double,
        val dswrf: Double,
        val humidity: Double,
        val life_index: LifeIndex,
        val precipitation: Precipitation,
        val pressure: Double,
        val skycon: String,
        val status: String,
        val temperature: Double,
        val visibility: Double,
        val wind: Wind
)