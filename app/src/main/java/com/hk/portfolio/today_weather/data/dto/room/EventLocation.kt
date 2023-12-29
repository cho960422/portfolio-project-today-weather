package com.hk.portfolio.today_weather.data.dto.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity("event_location")
data class EventLocation(
    @PrimaryKey val id:String,
    @ColumnInfo("event_name") val eventName:String,
    @ColumnInfo("start_date") val startDate: LocalDate,
    @ColumnInfo("end_date") val endDate: LocalDate?,
    @ColumnInfo("alarm") val alarm: LocalDateTime?,
    @ColumnInfo("place_name") val placeName: String,
    @ColumnInfo("place_detail") val placeDetail: String,
    @ColumnInfo("nx") val nx: Double,
    @ColumnInfo("ny") val ny: Double,
    @ColumnInfo("update_at") val updateAt: LocalDateTime,
    @ColumnInfo("broadcast_id") val broadcastId: Int? = null,
)
