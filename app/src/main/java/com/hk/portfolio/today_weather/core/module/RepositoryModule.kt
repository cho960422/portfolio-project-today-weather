package com.hk.portfolio.today_weather.core.module

import android.app.Application
import androidx.room.Room
import com.hk.portfolio.today_weather.data.repository.WeatherRepositoryImpl
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}