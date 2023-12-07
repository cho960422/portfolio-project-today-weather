package com.hk.portfolio.today_weather.data.service.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Upsert
import com.hk.portfolio.today_weather.data.dto.room.WeatherData

@Dao
interface WeatherDao {
    @Upsert
    fun writeWeatherData(weatherData: WeatherData)

    @Delete
    suspend fun deleteWeatherData(weatherData: WeatherData)
}