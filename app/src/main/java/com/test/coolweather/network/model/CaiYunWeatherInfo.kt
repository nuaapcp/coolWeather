package com.test.coolweather.network.model

data class CaiYunWeatherInfo(
        val api_status: String,
        val api_version: String,
        val lang: String,
        val location: List<Double>,
        val result: Result,
        val server_time: Int,
        val status: String,
        val timezone: String,
        val tzshift: Int,
        val unit: String
)