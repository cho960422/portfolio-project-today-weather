package com.hk.portfolio.today_weather.core.module

import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.data.repository.EventRepositoryImpl
import com.hk.portfolio.today_weather.data.repository.SearchHistoryRepositoryImpl
import com.hk.portfolio.today_weather.data.repository.WeatherRepositoryImpl
import com.hk.portfolio.today_weather.data.service.retrofit.WeatherService
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import com.hk.portfolio.today_weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(db: AppDatabase, service: WeatherService): WeatherRepository =
        WeatherRepositoryImpl(db, service)

    @Provides
    @Singleton
    fun provideEventRepository(db: AppDatabase): EventRepository = EventRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(db: AppDatabase): SearchHistoryRepository = SearchHistoryRepositoryImpl(db)
}