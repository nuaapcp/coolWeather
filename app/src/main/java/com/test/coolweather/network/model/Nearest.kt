package com.test.coolweather.network.model

data class Nearest(
        val distance: Double,
        val intensity: Double,
        val status: String
)