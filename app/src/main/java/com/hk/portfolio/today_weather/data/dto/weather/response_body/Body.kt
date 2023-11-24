package com.hk.portfolio.today_weather.data.dto.weather.response_body

data class Body(
    val dataType: String? = "",
    val items: Items? = Items(),
    val numOfRows: Int? = 0,
    val pageNo: Int? = 0,
    val totalCount: Int? = 0
)