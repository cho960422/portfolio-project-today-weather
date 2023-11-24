package com.hk.portfolio.today_weather.data.dto.weather.response_body

data class Items<T>(
    val item: List<T>? = listOf()
)