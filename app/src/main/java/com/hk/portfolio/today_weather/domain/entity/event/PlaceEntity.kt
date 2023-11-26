package com.hk.portfolio.today_weather.domain.entity.event

data class PlaceEntity(
    val addressName: String,
    val nx: Double,
    val ny: Double,
    val detail:String
)
