package com.hk.portfolio.today_weather.core

import com.hk.portfolio.today_weather.data.service.retrofit.NaverService
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val naverUrl = "https://openapi.naver.com/"
    private const val weatherUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0"
    private const val tourUrl = "http://apis.data.go.kr/B551011/KorService1"
    private fun makeRetrofitBuilder(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val naverSearchBuilder = makeRetrofitBuilder(naverUrl)
    private val weatherBuilder = makeRetrofitBuilder(weatherUrl)
    private val tourBuilder = makeRetrofitBuilder(tourUrl)

    val naverApi = naverSearchBuilder.create(NaverService::class.java);
    val weatherApi = weatherBuilder.create(WeatherService::class.java);
    val tourApi = tourBuilder.create(TourService::class.java);
}