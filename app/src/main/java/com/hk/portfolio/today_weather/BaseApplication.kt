package com.hk.portfolio.today_weather

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hk.portfolio.today_weather.core.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application()

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")