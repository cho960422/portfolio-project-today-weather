package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
): BaseUseCase<HashMap<String, Any>, Boolean> {
    override fun invoke(param: HashMap<String, Any>): Boolean {
        TODO("Not yet implemented")
    }
}