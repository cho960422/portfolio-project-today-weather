package com.hk.portfolio.today_weather.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.usecase.event.GetAllEventListUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.GetWeatherUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.WriteWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getAllEventListUseCase: GetAllEventListUseCase,
    private val writeWeatherUseCase: WriteWeatherUseCase
) : ViewModel() {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val requestAvailableTime: List<Pair<LocalTime, LocalTime>> =
            listOf(
                Pair(toLocalTime("0200"), toLocalTime("0210")),
                Pair(toLocalTime("0500"), toLocalTime("0510")),
                Pair(toLocalTime("0800"), toLocalTime("0810")),
                Pair(toLocalTime("1100"), toLocalTime("1110")),
                Pair(toLocalTime("1400"), toLocalTime("1410")),
                Pair(toLocalTime("1700"), toLocalTime("1710")),
                Pair(toLocalTime("2000"), toLocalTime("2010")),
                Pair(toLocalTime("2300"), toLocalTime("2310")),
            )

        @RequiresApi(Build.VERSION_CODES.O)
        fun toLocalTime(time: String): LocalTime {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkAndUpdateWeather() {
        val baseDateTime = searchAvailableTime(LocalDateTime.now())
        val needUpdateEventList = filterNeedUpdateEvent(baseDateTime)
        updateWeather(needUpdateEventList, baseDateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateWeather(list: List<EventAndWeatherEntity>, baseDateTime: LocalDateTime) {
        val now = LocalDate.now()
        list.forEach {
            val weatherData = getWeatherUseCase(
                with(it.eventEntity) {
                    GetWeatherUseCase.Request(
                        eventId = id,
                        nx = place.nx,
                        ny = place.ny,
                        baseDateTime = baseDateTime,
                        count = 100,
                        date = if (startDate < now) startDate else now
                    )
                }
            )
            viewModelScope.launch(Dispatchers.IO) {
                writeWeatherUseCase(weatherData)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun filterNeedUpdateEvent(todayNeedUpdateTime: LocalDateTime): List<EventAndWeatherEntity> {
        val now = LocalDateTime.now()
        return getAllEventListUseCase(now.toLocalDate()).filter {
            if (it.weatherEntity?.baseDateTime == null) true
            else
                it.weatherEntity.baseDateTime < todayNeedUpdateTime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchAvailableTime(now: LocalDateTime): LocalDateTime {
        if (now.toLocalTime() < LocalTime.of(2, 11)) return now.minusDays(1).withHour(
            requestAvailableTime.last().first.hour
        ).withMinute(0)
        val nowTime = now.toLocalTime()
        val timePair = requestAvailableTime.last {
            it.second < nowTime
        }

        return now.withHour(timePair.first.hour).withMinute(0)
    }
}