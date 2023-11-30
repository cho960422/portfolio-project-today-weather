package com.hk.portfolio.today_weather.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    ): WeatherConditionEnum {
        val resultMap = resultOfRainSnow(list)
        val rainArr = resultMap[WeatherConditionEnum.Rain]
        val snowArr = resultMap[WeatherConditionEnum.Snow]
        val rainSnow = resultMap[WeatherConditionEnum.RainOrSnow]
        if (rainArr?.isEmpty() == true && snowArr?.isEmpty() == true && rainSnow?.isEmpty() == true)
            return WeatherConditionEnum.Clean

        return weatherPerDate(rainSize = rainArr?.size?: 0, snowSize = snowArr?.size?: 0, rainSnowSize = rainSnow?.size?:0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun exportDescription(
        list: List<WeatherEntity>
    ): String {
        val resultMap = resultOfRainSnow(list)
        val rainArr = resultMap[WeatherConditionEnum.Rain]
        val snowArr = resultMap[WeatherConditionEnum.Snow]
        val rainSnow = resultMap[WeatherConditionEnum.RainOrSnow]
        val rainStr = if (rainArr?.isNotEmpty() == true) rainArr.joinToString(", "){
            DateTimeFormatter.ofPattern("HH:mm").format(it)
        } + "에 비" else null
        val snowStr = if (snowArr?.isNotEmpty() == true) snowArr.joinToString(", "){
            DateTimeFormatter.ofPattern("HH:mm").format(it)
        } + "에 는" else null
        val rainSnowStr = if (rainSnow?.isNotEmpty() == true) rainSnow.joinToString(", "){
            DateTimeFormatter.ofPattern("HH:mm").format(it)
        } + "에 는/비" else null
        val strArr = mutableListOf<String>()

        if (rainStr != null) strArr.add(rainStr)
        if (snowStr != null) strArr.add(snowStr)
        if (rainSnowStr != null) strArr.add(rainSnowStr)

        return if (strArr.isEmpty()) "눈/비 예보가 없어요" else strArr.joinToString(",\n") + " 예보가 있어요"
    }

    private fun weatherPerDate(
        rainSize: Int,
        snowSize: Int,
        rainSnowSize: Int,
    ): WeatherConditionEnum {
        if (rainSize == snowSize && rainSize == rainSnowSize)
            return WeatherConditionEnum.RainOrSnow

        val map = HashMap<WeatherConditionEnum, Int>()
        val sortList = mutableListOf(
            WeatherConditionEnum.Rain,
            WeatherConditionEnum.Snow,
            WeatherConditionEnum.RainOrSnow
        )

        map[WeatherConditionEnum.Rain] = rainSize
        map[WeatherConditionEnum.Snow] = snowSize
        map[WeatherConditionEnum.RainOrSnow] = rainSnowSize

        for (i in 0 until sortList.size - 1) {
            for (j in i until sortList.size - 1) {
                if ((map[sortList[j]]?: 0) < (map[sortList[j + 1]]?:0)) {
                    val current = sortList[j]
                    val next = sortList[j + 1]
                    sortList[j] = next
                    sortList[j+1] = current
                }
            }
        }

        return sortList.first()
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
            if (rainSet[WeatherConditionEnum.Rain] == true) {
                resultMap[WeatherConditionEnum.Rain]?.add(it.dateTime.toLocalTime())
            } else if (rainSet[WeatherConditionEnum.Snow] == true) {
                resultMap[WeatherConditionEnum.Snow]?.add(it.dateTime.toLocalTime())
            } else if (rainSet[WeatherConditionEnum.RainOrSnow] == true) {
                resultMap[WeatherConditionEnum.RainOrSnow]?.add(it.dateTime.toLocalTime())
            }
        }

        return resultMap
    }
}