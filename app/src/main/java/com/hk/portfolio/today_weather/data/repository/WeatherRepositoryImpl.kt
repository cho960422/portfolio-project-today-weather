package com.hk.portfolio.today_weather.data.repository

import android.content.Context
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherShortEntity
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @ActivityContext private val context: Context,
    private val service: WeatherService
): WeatherRepository {
    val db = AppDatabase.getDatabase(context).weatherDao()

    override suspend fun getWeather(
        nx: Double,
        ny: Double,
        baseDateTime: LocalDateTime,
        count: Int
    ): List<WeatherEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getShortWeather(
        nx: Double,
        ny: Double,
        baseDateTime: LocalDateTime
    ): List<WeatherShortEntity> {
        TODO("Not yet implemented")
    }
}