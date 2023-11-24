package com.hk.portfolio.today_weather.data.dto.weather

data class WeatherNowDto(
    val baseDate: String?= "",
    val baseTime: String?= "",
    val category: String?= "",
    val nx:Double?= null,
    val ny:Double?= null,
    val obsrValue: Int?= null
)
