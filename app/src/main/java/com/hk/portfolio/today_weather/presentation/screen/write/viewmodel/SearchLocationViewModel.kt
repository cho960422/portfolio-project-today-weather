package com.hk.portfolio.today_weather.presentation.screen.write.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.core.SearchCategoryEnum
import com.hk.portfolio.today_weather.core.UiState
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.domain.entity.GetHistoryRequest
import com.hk.portfolio.today_weather.domain.entity.InsertHistoryRequest
import com.hk.portfolio.today_weather.domain.entity.kakao.KakaoLocationEntity
import com.hk.portfolio.today_weather.domain.usecase.kakao.SearchLocationUseCase
import com.hk.portfolio.today_weather.domain.usecase.search_history.DeleteHistoryUseCase
import com.hk.portfolio.today_weather.domain.usecase.search_history.GetHistoryListUseCase
import com.hk.portfolio.today_weather.domain.usecase.search_history.InsertHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val searchLocationUseCase: SearchLocationUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase
): ViewModel() {
    private val category = SearchCategoryEnum.AddressSearch
    var historyList = mutableStateOf<List<SearchHistoryData>>(listOf())
        private set
    var searchState = mutableStateOf<UiState<List<KakaoLocationEntity>>>(UiState(isLoading = true, isEnd = false))
        private set
    var refresh = mutableStateOf(true)
        private set
    private var searchText = ""

    private val defaultPage = 1
    private var page = defaultPage

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
        loadData(query)
    }

    suspend fun getHistoryList(query: String) {
        /**
         * 검색 히스토리 가져오는 도중 같은 scope 안에서 목록에 집어넣으려고 하니 오류가 발생했었음,
         * 이 부분 트러블 슈팅으로 기룩하기
         */
        val list: List<SearchHistoryData> = viewModelScope.async (Dispatchers.IO) {
            val list: List<SearchHistoryData> = getHistoryListUseCase(GetHistoryRequest(query, category)).toList()
            Log.d("list :: ", list.toString())
            list
        }.await()

        historyList.value = list
    }

    fun loadData(query: String) {
        refresh.value = true
        page = defaultPage
        searchText = query
        viewModelScope.launch {
            searchState.value = searchState.value.copy(data = listOf())
            fetch(searchText, page, listOf())
        }
    }

    fun loadMore() {
        page ++
        viewModelScope.launch {
            fetch(searchText, page, searchState.value.data?: listOf())
        }
    }

    fun errorConfirm() {
        searchState.value = searchState.value.copy(isError = false, errorCode = null)
    }

    private suspend fun fetch(query:String, page:Int, originList: List<KakaoLocationEntity>) {
        searchLocationUseCase(
            SearchLocationUseCase.Request(
                query = query,
                page = page
            )
        ).onEach {
            when (it) {
                is JobState.Loading -> {
                    searchState.value = searchState.value.copy(isLoading = true)
                }
                is JobState.Success -> {
                    val existList = originList.toMutableList()
                    existList.addAll(it.data?: listOf())
                    searchState.value = searchState.value.copy(isLoading = false, data = existList, isEnd = it.isEnd?: true)
                    refresh.value = false
                }
                is JobState.Error -> {
                    searchState.value = searchState.value.copy(isLoading = false, isError = true, message = it.message?: "오류가 발생하였습니다.")
                    refresh.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteHistory(data: SearchHistoryData, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHistoryUseCase(data)
            getHistoryList(query)
        }
    }
}