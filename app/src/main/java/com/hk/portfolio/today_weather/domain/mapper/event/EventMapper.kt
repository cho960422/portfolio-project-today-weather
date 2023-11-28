package com.hk.portfolio.today_weather.domain.mapper.event

import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity

fun EventLocation.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        eventName = eventName,
        startDate = startDate,
        endDate = endDate,
        alarm = alarm,
        place = PlaceEntity(
            placeName, nx, ny, ""
        )
    )
}

fun EventEntity.toDto(): EventLocation {
    return EventLocation(
        id = id,
        eventName = eventName,
        startDate = startDate,
        endDate = endDate,
        alarm = alarm,
        placeName = place.addressName,
        nx = place.nx,
        ny = place.ny
    )
}