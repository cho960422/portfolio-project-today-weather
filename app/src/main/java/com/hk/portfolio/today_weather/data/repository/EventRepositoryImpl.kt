package com.hk.portfolio.today_weather.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.data.dto.room.EventLocationPagingSource
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.mapper.event.toEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val db: AppDatabase
): EventRepository {
    private val dao = db.eventDao()
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getEventListAll(startDate: LocalDate?): List<EventEntity> {
        return dao.getEventListAll(startDate?: LocalDate.now()).map {
            it.toEntity()
        }
    }

    override fun getEventPagingSource(): PagingSource<Int, EventEntity> =
        EventLocationPagingSource(db)

    override suspend fun insert(eventLocation: EventLocation): Boolean {
        return try {
            dao.upsertEvent(eventLocation)
            true
        } catch (e:Exception) {
            false
        }
    }


}