package com.hk.portfolio.today_weather.domain.mapper.event

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
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
        updateAt = updateAt
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