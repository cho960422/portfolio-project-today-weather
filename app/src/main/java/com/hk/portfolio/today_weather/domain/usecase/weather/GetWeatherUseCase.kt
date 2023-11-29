package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
): CoroutineUseCase<GetWeatherUseCase.Request, WeatherConditionEntity> {
    class Request(val nx:Double, val ny: Double, val baseDateTime: LocalDateTime, val count:Int)

    override suspend fun invoke(param: Request): WeatherConditionEntity {
        val response = with(param){
            weatherRepository.getWeather(
                nx, ny, baseDateTime, count
            )
        }


    }
}