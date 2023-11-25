package com.hk.portfolio.today_weather.data.dto.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "weather_short")
data class WeatherShortData(
    @PrimaryKey val uid:String,
    @ColumnInfo("nx") val nx: Double,
    @ColumnInfo("ny") val ny: Double,
    @ColumnInfo("date") val date: LocalDate,
    @ColumnInfo("base_date") val baseDateTime: LocalDateTime,
    @ColumnInfo("weather_condition") val weatherCondition: Int,
    @ColumnInfo("event_location_id") val eventLocationId: String
)
