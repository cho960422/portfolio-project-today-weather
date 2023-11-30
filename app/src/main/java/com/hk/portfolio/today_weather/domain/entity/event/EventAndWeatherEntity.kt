package com.hk.portfolio.today_weather.domain.entity.event

import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity

data class EventAndWeatherEntity(
    val eventEntity: EventEntity,
    val weatherEntity: WeatherConditionEntity?
)