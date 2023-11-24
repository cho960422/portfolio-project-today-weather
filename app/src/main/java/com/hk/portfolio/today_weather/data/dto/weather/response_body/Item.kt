package com.hk.portfolio.today_weather.data.dto.weather.response_body

data class Item(
    val baseDate: String? = "",
    val baseTime: String? = "",
    val category: String? = "",
    val fcstDate: String? = "",
    val fcstTime: String? = "",
    val fcstValue: String? = "",
    val nx: Int? = 0,
    val ny: Int? = 0
)