package com.hk.portfolio.today_weather.domain.mapper.weather

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.WeatherCategoryEnum
import com.hk.portfolio.today_weather.core.WeatherShortCategoryEnum
import com.hk.portfolio.today_weather.data.dto.retrofit.weather.WeatherDto
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherShortEntity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toEntity(): WeatherEntity {
    val baseDateTime = LocalDateTime.parse(baseDate + baseTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    val dateTime = LocalDateTime.parse(fcstDate + fcstTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    return WeatherEntity(
        baseDateTime = baseDateTime,
        dateTime = dateTime,
        category = WeatherCategoryEnum.findByCode(category),
        nx = nx ?: -1.0,
        ny = ny ?: -1.0,
        valueForCategory = fcstValue ?:"알 수 없음"
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toShortEntity(): WeatherShortEntity {
    val baseDateTime = LocalDateTime.parse(baseDate + baseTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    val dateTime = LocalDateTime.parse(fcstDate + fcstTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    return WeatherShortEntity(
        baseDateTime = baseDateTime,
        dateTime = dateTime,
        category = WeatherShortCategoryEnum.findByCode(category),
        nx = nx ?: -1.0,
        ny = ny ?: -1.0,
        valueForCategory = fcstValue?.toInt() ?: -1
    )
}
