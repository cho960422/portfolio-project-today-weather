package com.hk.portfolio.today_weather.data.service.retrofit

import com.hk.portfolio.today_weather.BuildConfig
import com.hk.portfolio.today_weather.data.dto.retrofit.weather.WeatherDto
import com.hk.portfolio.today_weather.data.dto.retrofit.weather.WeatherNowDto
import com.hk.portfolio.today_weather.data.dto.retrofit.weather.response_body.PublicResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate

interface WeatherService {
    companion object {
        const val mHost: String = BuildConfig.WEATHER_HOST
        const val serviceKey = BuildConfig.WEATHER_API_KEY
    }
    @GET("$mHost/getVilageFcst")
    suspend fun getWeather(
        @Query("serviceKey") serviceKey: String = WeatherService.serviceKey,
        @Query("numOfRows") offset: Int,
        @Query("pageNo") page: Int,
        @Query("dataType") type: String= "JSON",
        @Query("base_date") date: String,
        @Query("base_time") time: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): PublicResponseBody<WeatherDto>

    @GET("$mHost/getUltraSrtNcst")
    suspend fun getWeatherNow(
        @Query("serviceKey") serviceKey: String = WeatherService.serviceKey,
        @Query("numOfRows") offset: Int = 100,
        @Query("pageNo") page: Int = 1,
        @Query("dataType") type: String= "JSON",
        @Query("base_date") date: String,
        @Query("base_time") time: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): PublicResponseBody<WeatherNowDto>
}