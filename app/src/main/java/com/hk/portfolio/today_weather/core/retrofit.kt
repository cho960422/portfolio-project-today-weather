package com.hk.portfolio.today_weather.core

import android.util.Log
import com.hk.portfolio.today_weather.data.service.retrofit.KakaoService
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    private const val kakaoUrl = "https://dapi.kakao.com/"
    private const val weatherUrl = "http://apis.data.go.kr/"
    private const val tourUrl = "http://apis.data.go.kr/"
    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(httpLoggingInterceptor())
        .build()
    private fun makeRetrofitBuilder(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message ->
            Log.e(
                "MyGitHubData :",
                message + ""
            )
        }
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val kakaoSearchBuilder = makeRetrofitBuilder(kakaoUrl)
    private val weatherBuilder = makeRetrofitBuilder(weatherUrl)
    private val tourBuilder = makeRetrofitBuilder(tourUrl)

    val kakaoApi = kakaoSearchBuilder.create(KakaoService::class.java);
    val weatherApi = weatherBuilder.create(WeatherService::class.java);
    val tourApi = tourBuilder.create(TourService::class.java);
}