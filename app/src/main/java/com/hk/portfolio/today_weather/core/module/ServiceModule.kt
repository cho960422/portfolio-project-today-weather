package com.hk.portfolio.today_weather.core.module

import com.hk.portfolio.today_weather.core.RetrofitBuilder
import com.hk.portfolio.today_weather.data.service.retrofit.KakaoService
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Singleton
    @Provides
    fun provideKakaoService(): KakaoService = RetrofitBuilder.kakaoApi

    @Singleton
    @Provides
    fun provideTourService(): TourService = RetrofitBuilder.tourApi

    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService = RetrofitBuilder.weatherApi
}