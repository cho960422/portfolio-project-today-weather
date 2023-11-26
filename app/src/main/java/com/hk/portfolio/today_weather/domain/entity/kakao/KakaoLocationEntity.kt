package com.hk.portfolio.today_weather.domain.entity.kakao

data class KakaoLocationEntity(
    val id:String,
    val title:String,
    val address: String,
    val roadAddress: String,
    val nx: Double,
    val ny: Double,
    val category: String
)
