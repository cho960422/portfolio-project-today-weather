package com.hk.portfolio.today_weather.data.dto.retrofit.weather.response_body

data class Items<T>(
    val item: List<T>? = listOf()
)