package com.hk.portfolio.today_weather.domain.mapper.event

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.data.dto.room.EventAndWeatherData
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.domain.mapper.weather.toDto
import com.hk.portfolio.today_weather.domain.mapper.weather.toEntity
import java.time.LocalDateTime

fun EventLocation.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        eventName = eventName,
        startDate = startDate,
        endDate = endDate,
        alarm = alarm,
        place = PlaceEntity(
            placeName, nx, ny, placeDetail
        ),
        updateAt = updateAt,
        broadcastId = broadcastId
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun EventEntity.toDto(): EventLocation {
    return EventLocation(
        id = id,
        eventName = eventName,
        startDate = startDate,
        endDate = endDate,
        alarm = alarm,
        placeName = place.addressName,
        placeDetail = place.detail,
        nx = place.nx,
        ny = place.ny,
        updateAt = updateAt
    )
}

fun EventAndWeatherData.toEntity(): EventAndWeatherEntity {
    return EventAndWeatherEntity(
        eventEntity = this.eventLocation.toEntity(),
        weatherEntity = this.weatherData?.toEntity()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun EventAndWeatherEntity.toDto(): EventAndWeatherData {
    return EventAndWeatherData(
        eventLocation = this.eventEntity.toDto(),
        weatherData = this.weatherEntity?.toDto()
    )
}