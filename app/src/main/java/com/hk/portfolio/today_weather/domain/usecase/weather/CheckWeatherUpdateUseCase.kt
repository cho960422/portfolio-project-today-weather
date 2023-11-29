package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class CheckWeatherUpdateUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): CoroutineUseCase<Any, Boolean> {
    override suspend fun invoke(param: Any): Boolean {
        TODO("Not yet implemented")
    }
}