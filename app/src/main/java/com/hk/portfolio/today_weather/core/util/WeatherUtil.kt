package com.hk.portfolio.today_weather.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object WeatherUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    private val EARLY_TIME_START = LocalTime.of(8, 0)

    @RequiresApi(Build.VERSION_CODES.O)
    private val MIDDLE_TIME_START = LocalTime.of(16, 0)

    @RequiresApi(Build.VERSION_CODES.O)
    private val NIGHT_TIME_START = LocalTime.of(0, 0)
    var TO_GRID = 0
    var TO_GPS = 1

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
            WeatherConditionEnum.RainOrSnow,
            WeatherConditionEnum.Rain,
            WeatherConditionEnum.Snow
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

    fun convertGRID_GPS(mode: Int, lat_X: Double, lng_Y: Double): LatXLngY {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //
        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD
        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        val rs = LatXLngY()
        if (mode == TO_GRID) {
            rs.lat = lat_X
            rs.lng = lng_Y
            var ra = Math.tan(Math.PI * 0.25 + lat_X * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = lng_Y * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
        } else {
            rs.x = lat_X
            rs.y = lng_Y
            val xn = lat_X - XO
            val yn = ro - lng_Y + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5
            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            rs.lat = alat * RADDEG
            rs.lng = alon * RADDEG
        }
        return rs
    }


    class LatXLngY {
        var lat = 0.0
        var lng = 0.0
        var x = 0.0
        var y = 0.0
    }


}