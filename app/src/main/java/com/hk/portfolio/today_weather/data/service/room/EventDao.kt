package com.hk.portfolio.today_weather.data.service.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.hk.portfolio.today_weather.data.dto.room.EventAndWeatherData
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM event_location ORDER BY start_date DESC, update_at DESC LIMIT :start, :end")
    suspend fun getEventList(start:Int, end:Int): List<EventAndWeatherData>

    @Transaction
    @Query("SELECT * FROM event_location WHERE (start_date <= :date AND end_date > :date) OR ( start_date >= :date AND start_date <= :limitDate)")
    suspend fun getEventListAll(date: LocalDate, limitDate: LocalDate): List<EventAndWeatherData>

    @Query("SELECT * FROM event_location WHERE (start_date <= :date AND end_date > :date) OR ( start_date = :date)")
    suspend fun getEventList(date: LocalDate): List<EventLocation>

    @Upsert
    suspend fun upsertEvent(event: EventLocation)

    @Transaction
    @Query("SELECT * FROM event_location WHERE start_date = :date OR (start_date <= :date AND end_date >= :date)")
    fun getTodayEvent(date: LocalDate): Flow<List<EventAndWeatherData>>

    @Delete
    suspend fun deleteEventAndWeather(data: EventLocation)

    @Query("SELECT * FROM event_location WHERE id = :id")
    suspend fun getEvent(id:String): EventLocation
}