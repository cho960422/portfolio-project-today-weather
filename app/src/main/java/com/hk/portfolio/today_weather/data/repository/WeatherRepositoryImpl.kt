package com.hk.portfolio.today_weather.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherShortEntity
import com.hk.portfolio.today_weather.domain.mapper.weather.toEntity
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val service: WeatherService
): WeatherRepository {
    val dao = db.weatherDao()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeather(
        nx: Double,
        ny: Double,
        baseDateTime: LocalDateTime,
        count: Int,
        date: LocalDate
    ): List<WeatherEntity> {
        val resultArr = mutableListOf<WeatherEntity>()
        val page = 2
        val offset = 100
        val firstResponse = service.getWeather(
            page = 1,
            offset = offset,
            date = DateTimeFormatter.ofPattern("yyyyMMdd").format(baseDateTime),
            time = DateTimeFormatter.ofPattern("HHmm").format(baseDateTime),
            nx = nx,
            ny = ny
        )
        val total = firstResponse.response?.body?.totalCount?:0
        val endPage = total / offset + 1
        for (i in page .. endPage) {
            try {
                val response = service.getWeather(
                    page = i,
                    offset = offset,
                    date = DateTimeFormatter.ofPattern("yyyyMMdd").format(baseDateTime),
                    time = DateTimeFormatter.ofPattern("HHmm").format(baseDateTime),
                    nx = nx,
                    ny = ny
                )
                val list = response.response?.body?.items?.item?.map {
                    it.toEntity()
                }?: listOf()
                resultArr.addAll(list)
                if (list.isEmpty()) break
            } catch (e:Exception) {
                continue
            }
        }

        return resultArr.filter { it.dateTime.toLocalDate() == date }
    }

    override suspend fun getShortWeather(
        nx: Double,
        ny: Double,
        baseDateTime: LocalDateTime
    ): List<WeatherShortEntity> {
        TODO("Not yet implemented")
    }
}