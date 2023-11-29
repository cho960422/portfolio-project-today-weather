package com.hk.portfolio.today_weather.data.service.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import java.time.LocalDate

@Dao
interface EventDao {
    @Query("SELECT * FROM event_location ORDER BY start_date DESC, update_at DESC LIMIT :start, :end")
    suspend fun getEventList(start:Int, end:Int): List<EventLocation>

    @Query("SELECT * FROM event_location WHERE start_date <= :date AND end_date > :date")
    suspend fun getEventListAll(date: LocalDate): List<EventLocation>

    @Upsert
    suspend fun upsertEvent(event: EventLocation)
}