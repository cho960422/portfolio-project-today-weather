package com.hk.portfolio.today_weather.domain.usecase.weather

import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.mapper.weather.toDto
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import java.lang.Appendable
import javax.inject.Inject

class WriteWeatherUseCase @Inject constructor(
    private val db: AppDatabase
): CoroutineUseCase<WeatherConditionEntity, Unit> {
    val dao = db.weatherDao()
    override suspend fun invoke(param: WeatherConditionEntity) {
        dao.writeWeatherData(param.toDto())
    }
}