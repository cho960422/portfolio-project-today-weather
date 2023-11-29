package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
): CoroutineUseCase<HashMap<String, Any>, Boolean> {
    override suspend fun invoke(param: HashMap<String, Any>): Boolean {
        TODO("Not yet implemented")
    }
}