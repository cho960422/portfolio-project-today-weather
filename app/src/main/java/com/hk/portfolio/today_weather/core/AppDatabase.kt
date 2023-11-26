package com.hk.portfolio.today_weather.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.data.dto.room.WeatherData
import com.hk.portfolio.today_weather.data.dto.room.WeatherShortData
import com.hk.portfolio.today_weather.data.service.room.EventDao
import com.hk.portfolio.today_weather.data.service.room.SearchHistoryDao
import com.hk.portfolio.today_weather.data.service.room.WeatherDao
import com.hk.portfolio.today_weather.domain.mapper.LocalDateTimeConverter

@Database(
    entities = [WeatherData::class, WeatherShortData::class, EventLocation::class, SearchHistoryData::class],
    version = 1
)
@TypeConverters(
    LocalDateTimeConverter::class
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun eventDao(): EventDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 데이터베이스의 인스턴스를 하나만 생성하여 불필요한 리소스 낭비를 줄이기 위함
         * private으로 선언함으로써 외부에서 직접적인 접근이 불가능하도록 설계
         *
         * 출처 : https://developer.android.com/training/data-storage/room?hl=ko
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance: AppDatabase =
                    Room.databaseBuilder(context = context, AppDatabase::class.java, "today_weather_db").build()
                INSTANCE = instance
                instance
            }
        }
    }

}