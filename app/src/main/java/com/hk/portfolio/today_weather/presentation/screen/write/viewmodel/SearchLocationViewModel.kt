package com.hk.portfolio.today_weather.presentation.screen.write.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hk.portfolio.today_weather.core.SearchCategoryEnum
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.domain.entity.GetHistoryRequest
import com.hk.portfolio.today_weather.domain.entity.InsertHistoryRequest
import com.hk.portfolio.today_weather.domain.usecase.search_history.GetHistoryListUseCase
import com.hk.portfolio.today_weather.domain.usecase.search_history.InsertHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase
): ViewModel() {
    private val category = SearchCategoryEnum.AddressSearch
    var historyList = mutableStateOf<List<SearchHistoryData>>(listOf())
        private set

    init {
        viewModelScope.launch {
            getHistoryList("")
        }
    }

    fun onSearch(query:String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                insertHistoryUseCase(InsertHistoryRequest(query, category))
            }
        }
    }

    suspend fun getHistoryList(query: String) {
        val list: List<SearchHistoryData> = viewModelScope.async (Dispatchers.IO) {
            val list: List<SearchHistoryData> = getHistoryListUseCase(GetHistoryRequest(query, category)).toList()
            Log.d("list :: ", list.toString())
            list
        }.await()

        historyList.value = list
    }
}