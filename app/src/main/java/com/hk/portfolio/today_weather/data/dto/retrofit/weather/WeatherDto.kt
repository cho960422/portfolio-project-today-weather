package com.hk.portfolio.today_weather.data.dto.retrofit.weather

data class WeatherDto(
    val baseDate: String?= "",
    val baseTime: String?= "",
    val category: String?= "",
    val fcstDate: String?= "",
    val fcstTime: String?= "",
    val fcstValue: String?= "",
    val nx:Double?= null,
    val ny:Double?= null
)
