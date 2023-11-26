package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import javax.inject.Inject

class CheckWeatherUpdateUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): BaseUseCase<Any, Boolean> {
    override suspend fun invoke(param: Any): Boolean {
        TODO("Not yet implemented")
    }
}