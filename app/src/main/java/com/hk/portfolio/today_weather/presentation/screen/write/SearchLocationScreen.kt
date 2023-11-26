package com.hk.portfolio.today_weather.presentation.screen.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hk.portfolio.today_weather.presentation.screen.write.viewmodel.SearchLocationViewModel
import com.hk.portfolio.today_weather.ui.component.SearchHistoryCell
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLocationScreen(
    onSelectButtonClicked: (String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SearchLocationViewModel>()
    val scope = rememberCoroutineScope()
    Surface {
        val active = remember { mutableStateOf(false) }
        val searchText = remember { mutableStateOf("") }
        val paddingValue = remember { mutableStateOf(16.dp) }
        val historyList = viewModel.historyList.value

        LaunchedEffect(active.value) {
            paddingValue.value = if (active.value) {
                0.dp
            } else 16.dp
        }

        Column {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValue.value),
                query = searchText.value,
                onQueryChange = {
                    searchText.value = it
                    scope.launch {
                        viewModel.getHistoryList(it)
                    }
                },
                onSearch = {
                    active.value = false
                    viewModel.onSearch(it)
                },
                active = active.value,
                onActiveChange = {
                    active.value = it
                    //TODO() 장소 검색 API 호출 후 밑 목록에 뿌려주기
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            active.value = true
                        },
                    )
                },
                trailingIcon = {
                    if (active.value) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                active.value = true
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
                        SearchHistoryCell(it) { query ->
                            active.value = false
                            searchText.value = query
                            viewModel.onSearch(query)
                        }
                    }
                }
            }

//            LazyColumn {
//                items(
//                    items = historyList
//                ) {
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
//                        Text(text = it.query)
//                    }
//                }
//            }
        }
    }
}