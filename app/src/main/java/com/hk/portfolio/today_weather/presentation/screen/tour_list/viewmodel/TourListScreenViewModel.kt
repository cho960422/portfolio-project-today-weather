package com.hk.portfolio.today_weather.presentation.screen.tour_list.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.dataStore
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.usecase.tour.GetTourListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourListScreenViewModel @Inject constructor(
    private val getTourListUseCase: GetTourListUseCase
): ViewModel() {
    var tourList = mutableStateOf<Flow<PagingData<TourEntity>>?>(null)
        private set
    var category = mutableStateOf<TourContentTypeEnum?>(null)
        private set

    fun getTourList(lat:Double, lng: Double, category: TourContentTypeEnum?) {
        tourList.value = null
        val latlng = WeatherUtil.LatXLngY()
        latlng.lat = lat
        latlng.lng = lng
        tourList.value = Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            initialKey = 1,
            pagingSourceFactory = {
                getTourListUseCase(Pair(latlng, if (category == null || category == TourContentTypeEnum.Unknown) null else category))
            }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    fun selectTourCategory(inputCategory: TourContentTypeEnum?) {
        viewModelScope.launch {
            category.value = inputCategory
        }
    }
}