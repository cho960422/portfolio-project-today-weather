package com.hk.portfolio.today_weather.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.mapper.event.toEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.LocalDate
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val db: AppDatabase
): EventRepository {
    private val dao = db.eventDao()
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getEventList(startDate: LocalDate?): List<EventEntity> {
        return dao.getEventList(startDate?: LocalDate.now()).map {
            it.toEntity()
        }
    }
}