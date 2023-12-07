package com.hk.portfolio.today_weather.data.service.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.hk.portfolio.today_weather.data.dto.room.EventAndWeatherData
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EventDao {
    @Query("SELECT * FROM event_location ORDER BY start_date DESC, update_at DESC LIMIT :start, :end")
    suspend fun getEventList(start:Int, end:Int): List<EventLocation>

    @Transaction
    @Query("SELECT * FROM event_location WHERE (start_date <= :date AND end_date > :date) OR ( start_date >= :date AND start_date <= :limitDate)")
    suspend fun getEventListAll(date: LocalDate, limitDate: LocalDate): List<EventAndWeatherData>

    @Upsert
    suspend fun upsertEvent(event: EventLocation)

    @Transaction
    @Query("SELECT * FROM event_location WHERE start_date = :date OR (start_date <= :date AND end_date >= :date)")
    fun getTodayEvent(date: LocalDate): Flow<List<EventAndWeatherData>>
}