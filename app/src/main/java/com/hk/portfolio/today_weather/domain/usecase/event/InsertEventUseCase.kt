package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.mapper.event.toDto
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
): BaseUseCase<EventEntity, Flow<JobState<Boolean>>> {
    override suspend fun invoke(param: EventEntity): Flow<JobState<Boolean>> = flow {
        emit(JobState.Loading())
        val result = eventRepository.insert(param.toDto())
        if (result) {
            emit(JobState.Success(data = result))
        } else {
            emit(JobState.Error("등록되지 않았습니다."))
        }
    }
}