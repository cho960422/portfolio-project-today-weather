package com.hk.portfolio.today_weather.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.room.Transaction
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.data.dto.room.EventLocationPagingSource
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.mapper.event.toDto
import com.hk.portfolio.today_weather.domain.mapper.event.toEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : EventRepository {
    private val dao = db.eventDao()
    private val weatherDao = db.weatherDao()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getEventListAll(startDate: LocalDate?): List<EventAndWeatherEntity> {
        val nowDate = startDate ?: LocalDate.now()
        return dao.getEventListAll(startDate ?: nowDate, nowDate.plusDays(1)).map {
            it.toEntity()
        }
    }

    override fun getEventPagingSource(): PagingSource<Int, EventAndWeatherEntity> =
        EventLocationPagingSource(db)

    override suspend fun insert(eventLocation: EventLocation): Boolean {
        return try {
            dao.upsertEvent(eventLocation)
            true
        } catch (e: Exception) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTodayEvent(): Flow<List<EventAndWeatherEntity>> {
        val dtoFlow = dao.getTodayEvent(LocalDate.now())
        Log.d("dtoFlow :: ", dtoFlow.toString())
        return dtoFlow.map {
            it.map { dto ->
                dto.toEntity()
            }
        }
    }

    @Transaction
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun deleteEvent(eventEntity: EventAndWeatherEntity) {
        val dto = eventEntity.toDto()
        dto.weatherData?.let { weatherDao.deleteWeatherData(it) }
        dao.deleteEventAndWeather(dto.eventLocation)
    }

    override suspend fun getEvent(id: String): EventEntity {
        return dao.getEvent(id).toEntity()
    }


}