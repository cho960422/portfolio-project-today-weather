package com.hk.portfolio.today_weather.data.dto.naver

data class NaverLocationDto(
    val address: String? = "",
    val category: String? = "",
    val description: String? = "",
    val link: String? = "",
    val mapx: Int? = null,
    val mapy: Int? = null,
    val roadAddress: String? = "",
    val telephone: String? = "",
    val title: String? = ""
)