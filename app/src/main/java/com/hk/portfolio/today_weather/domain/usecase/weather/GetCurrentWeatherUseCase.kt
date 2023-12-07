package com.hk.portfolio.today_weather.domain.usecase.weather

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.core.WeatherShortCategoryEnum
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): CoroutineUseCase<GetCurrentWeatherUseCase.Request, Flow<JobState<WeatherConditionEntity>>> {
    class Request(
        val nx: Double,
        val ny: Double,
        val dateTime: LocalDateTime
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun invoke(param: Request): Flow<JobState<WeatherConditionEntity>> = flow {
        try {
            emit(JobState.Loading())
            val location = WeatherUtil.convertGRID_GPS(WeatherUtil.TO_GRID,param.nx, param.ny)
            val list = weatherRepository.getShortWeather(location.x, location.y, param.dateTime)
            val weather = list.first { it.category == WeatherShortCategoryEnum.WeatherCondition }

            emit(JobState.Success(
                data = WeatherConditionEntity(
                    id = "",
                    nx = location.x,
                    ny = location.y,
                    date = weather.baseDateTime.toLocalDate(),
                    baseDateTime = weather.baseDateTime,
                    weatherCondition = WeatherConditionEnum.findByCode(weather.valueForCategory.toInt()),
                    eventLocationId = "",
                    description = ""
                )
            ))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(JobState.Error(message = "날씨를 가져오지 못했습니다."))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(JobState.Error(message = "날씨를 가져오지 못했습니다."))
        }
    }
}