package com.hk.portfolio.today_weather.data.dto.retrofit.weather.response_body

data class Body<T>(
    val dataType: String? = "",
    val items: Items<T>? = Items(),
    val numOfRows: Int? = 0,
    val pageNo: Int? = 0,
    val totalCount: Int? = 0
)