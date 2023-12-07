package com.hk.portfolio.today_weather.presentation.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hk.portfolio.today_weather.data.repository.EventRepositoryImpl
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.usecase.event.DeleteEventUseCase
import com.hk.portfolio.today_weather.domain.usecase.event.GetEventPageSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListScreenViewModel @Inject constructor(
    private val getEventPageSourceUseCase: GetEventPageSourceUseCase,
    private val deleteEventUseCase: DeleteEventUseCase
) : ViewModel() {
    val items: Flow<PagingData<EventAndWeatherEntity>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            getEventPageSourceUseCase(null)
        }
    )
        .flow
        .cachedIn(viewModelScope)

    fun deleteEvent(eventAndWeatherEntity: EventAndWeatherEntity, onRefresh:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteEventUseCase(eventAndWeatherEntity)
            onRefresh()
        }
    }
}