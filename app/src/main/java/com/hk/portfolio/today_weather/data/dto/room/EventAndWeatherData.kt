package com.hk.portfolio.today_weather.data.dto.room

import androidx.room.Embedded
import androidx.room.Relation

data class EventAndWeatherData(
    @Embedded val eventLocation: EventLocation,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_location_id"
    )
    val weatherData: WeatherData
)