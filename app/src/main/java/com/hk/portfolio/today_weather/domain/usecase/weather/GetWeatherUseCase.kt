package com.hk.portfolio.today_weather.domain.usecase.weather

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
): CoroutineUseCase<GetWeatherUseCase.Request, WeatherConditionEntity> {
    class Request(val eventId: String, val nx:Double, val ny: Double, val baseDateTime: LocalDateTime, val count:Int, val date: LocalDate)

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun invoke(param: Request): WeatherConditionEntity {
        val resultList = with(param) {
            weatherRepository.getWeather(
                nx, ny, baseDateTime, count, date
            )
        }

        val weatherCondition = WeatherUtil.exportWeatherCondition(resultList)
        val description = WeatherUtil.exportDescription(resultList)
        return WeatherConditionEntity(
            id = param.eventId,
            nx = param.nx,
            ny = param.ny,
            baseDateTime = param.baseDateTime,
            weatherCondition = weatherCondition,
            description = description,
            eventLocationId = param.eventId,
            date = param.date
        )
    }
}