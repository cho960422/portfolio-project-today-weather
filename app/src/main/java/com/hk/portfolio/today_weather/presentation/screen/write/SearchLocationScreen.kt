package com.hk.portfolio.today_weather.presentation.screen.write

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.presentation.screen.write.viewmodel.SearchLocationViewModel
import com.hk.portfolio.today_weather.ui.component.PlaceCell
import com.hk.portfolio.today_weather.ui.component.SearchHistoryCell
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLocationScreen(
    onSelectButtonClicked: (PlaceEntity) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SearchLocationViewModel>()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val active = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    val paddingValue = remember { mutableStateOf(16.dp) }
    val historyList = viewModel.historyList.value
    val searchState = viewModel.searchState.value
    val refresh = viewModel.refresh.value
    val lazyListState = rememberLazyListState()
    val canPagination = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(searchState) {
        canPagination.value = !searchState.isLoading && !refresh && !searchState.isEnd
    }

    val shouldStartPaginate = remember {
        derivedStateOf {
            canPagination.value && (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (lazyListState.layoutInfo.totalItemsCount - 6)
        }
    }

    fun searchButtonClicked(query:String) {
        if (query.isNotEmpty()) {
            active.value = false
            viewModel.onSearch(query)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(message = "장소 이름을 입력해주세요.", duration = SnackbarDuration.Short)
            }
        }
    }

    LaunchedEffect(shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            viewModel.loadMore()
        }
    }

    LaunchedEffect(active.value) {
        paddingValue.value = if (active.value) {
            0.dp
        } else 16.dp
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        val paddingValues = it

        Column {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValue.value),
                query = searchText.value,
                onQueryChange = { query ->
                    searchText.value = query
                    scope.launch {
                        viewModel.getHistoryList(query)
                    }
                },
                onSearch = { query ->
                    searchButtonClicked(query)
                },
                active = active.value,
                onActiveChange = {
                    active.value = it
                    //TODO() 장소 검색 API 호출 후 밑 목록에 뿌려주기
                },
                leadingIcon = {
                    if (!active.value) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                active.value = true
                            },
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                active.value = false
                            },
                        )
                    }
                },
                trailingIcon = {
                    if (searchText.value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                searchText.value = ""
                            },
                        )
                    }
                },
                placeholder = {
                    Text(text = "방문하실 장소를 검색하세요")
                }
            ) {
                LazyColumn {
                    items(
                        items = historyList
                    ) {
                        SearchHistoryCell(
                            item = it,
                            onDeleteClicked = { history ->
                                viewModel.deleteHistory(history, searchText.value)
                            }
                        ) { query ->
                            active.value = false
                            searchText.value = query
                            viewModel.onSearch(query)
                        }
                    }
                }
            }

            LazyColumn(
                state = lazyListState
            ) {
                items(
                    items = searchState.data?: listOf(),
                    key = { entity ->
                        entity.id
                    }
                ) { item ->
                    PlaceCell(item) { selectedItem ->
                        val record = PlaceEntity(
                            addressName = selectedItem.title,
                            detail = selectedItem.roadAddress,
                            nx = selectedItem.nx,
                            ny = selectedItem.ny
                        )
                        onSelectButtonClicked(record)
                    }
                }

                if (!refresh && !searchState.isEnd) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}