package com.hk.portfolio.today_weather.domain.entity.tour

import com.hk.portfolio.today_weather.core.TourContentTypeEnum

data class TourEntity(
    val id:String,
    val name: String,
    val address: String,
    val addressDetail: String,
    val image: String,
    val lat: Double,
    val lng: Double,
    val contentType: TourContentTypeEnum
)