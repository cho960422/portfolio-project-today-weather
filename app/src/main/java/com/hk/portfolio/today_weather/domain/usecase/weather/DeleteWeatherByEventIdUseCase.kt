package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class DeleteWeatherByEventIdUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): BaseUseCase<String, Unit> {
    override fun invoke(param: String) {
        weatherRepository.deleteWeatherByEventId(param)
    }
}