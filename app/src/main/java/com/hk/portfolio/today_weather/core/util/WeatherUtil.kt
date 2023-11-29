package com.hk.portfolio.today_weather.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import java.time.LocalTime

object WeatherUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    private val EARLY_TIME_START = LocalTime.of(8, 0)
    @RequiresApi(Build.VERSION_CODES.O)
    private val MIDDLE_TIME_START = LocalTime.of(16, 0)
    @RequiresApi(Build.VERSION_CODES.O)
    private val NIGHT_TIME_START = LocalTime.of(0, 0)
    @RequiresApi(Build.VERSION_CODES.O)
    fun exportWeatherCondition(
        list: List<WeatherEntity>
    ): WeatherConditionEntity {
        val resultMap = resultOfRainSnow(list)
        val rainArr = resultMap[WeatherConditionEnum.Rain]
        val snowArr = resultMap[WeatherConditionEnum.Snow]
        val rainSnow = resultMap[WeatherConditionEnum.RainOrSnow]

//        val description =
    }

    /**
     * 값이 PTY일 때의 목록을
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resultOfRainSnow(
        list: List<WeatherEntity>
    ): Map<WeatherConditionEnum, List<LocalTime>> {
        val resultMap = hashMapOf<WeatherConditionEnum, MutableList<LocalTime>>()
        val rainSet = hashMapOf<WeatherConditionEnum, Boolean>()
        val snowSet = hashMapOf<WeatherConditionEnum, Boolean>()
        val rainSnowSet = hashMapOf<WeatherConditionEnum, Boolean>()
        rainSet[WeatherConditionEnum.Rain] = true
        rainSet[WeatherConditionEnum.Shower] = true
        rainSet[WeatherConditionEnum.Raindrop] = true

        snowSet[WeatherConditionEnum.Snow] = true
        snowSet[WeatherConditionEnum.Snowdrop] = true
        snowSet[WeatherConditionEnum.Raindrop] = true

        rainSnowSet[WeatherConditionEnum.RainOrSnow] = true
        rainSnowSet[WeatherConditionEnum.RaindropOrSnowdrop] = true

        resultMap[WeatherConditionEnum.Rain] = mutableListOf()
        resultMap[WeatherConditionEnum.Snow] = mutableListOf()
        resultMap[WeatherConditionEnum.RainOrSnow] = mutableListOf()
        list.forEach {
            if (rainSet[WeatherConditionEnum.Rain] == true){
                resultMap[WeatherConditionEnum.Rain]?.add(it.dateTime.toLocalTime())
            } else if (rainSet[WeatherConditionEnum.Snow] == true){
                resultMap[WeatherConditionEnum.Snow]?.add(it.dateTime.toLocalTime())
            } else if (rainSet[WeatherConditionEnum.RainOrSnow] == true){
                resultMap[WeatherConditionEnum.RainOrSnow]?.add(it.dateTime.toLocalTime())
            }
        }

        return resultMap
    }
}