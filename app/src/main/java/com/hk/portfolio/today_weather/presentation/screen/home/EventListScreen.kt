package com.hk.portfolio.today_weather.presentation.screen.home

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.presentation.screen.home.viewmodel.EventListScreenViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListScreen(
    onEventClicked:(EventAndWeatherEntity) -> Unit
) {
    val viewModel = hiltViewModel<EventListScreenViewModel>()
    val list = viewModel.items.collectAsLazyPagingItems()
    val editModeState = remember {
        mutableStateOf(false)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val isUpdating = remember {
        mutableStateOf(false)
    }
    val refreshScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isUpdating.value,
        onRefresh = {
            refreshScope.launch {
                list.refresh()
            }
        }
    )
    val deleteState = remember {
        mutableStateOf<EventAndWeatherEntity?>(null)
    }
    BackHandler(enabled = editModeState.value) {
        editModeState.value = !editModeState.value
    }
    LaunchedEffect(lifecycleState) {
        // Do something with your state
        // You may want to use DisposableEffect or other alternatives
        // instead of LaunchedEffect
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                list.refresh()
            }
        }
    }

    if (deleteState.value != null) {
        AlertDialog(
            onDismissRequest = {
                deleteState.value = null
            },
            confirmButton = {
                Text(
                    modifier = Modifier.clickable {
                        viewModel.deleteEvent(deleteState.value!!) {
                            list.refresh()
                        }
                        deleteState.value = null
                    },
                    text = "삭제"
                )
            },
            dismissButton = {
                Text(
                    modifier = Modifier.clickable {
                        deleteState.value = null
                    },
                    text = "취소"
                )
            },
            title = {
                Text(text = "안내")
            },
            text = {
                Text(text = "일정을 삭제하시겠습니까?")
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "방문지역 목록")
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .clickable {
                                editModeState.value = !editModeState.value
                            }
                            .padding(horizontal = 20.dp),
                        text = if (editModeState.value) {
                            "완료"
                        } else {
                            "편집"
                        }
                    )
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValue)
        ) {
            LazyColumn(
                modifier = Modifier.pullRefresh(pullRefreshState),
                contentPadding = PaddingValues(all = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(list.itemCount) { index ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDateStr = formatter.format(list[index]?.eventEntity?.startDate)
                    val endDateStr =
                        if (list[index]?.eventEntity?.endDate == null) "" else " ~ ${
                            formatter.format(list[index]?.eventEntity?.endDate)
                        }"
                    ListItem(
                        modifier = Modifier.clickable { onEventClicked(list[index]!!) },
                        headlineContent = {
                            Text(text = list[index]?.eventEntity?.eventName ?: "알 수 없는 일정")
                        },
                        supportingContent = {
                            Column {
                                Text(
                                    text = list[index]?.eventEntity?.place?.addressName
                                        ?: ""
                                )
                                if (list[index]?.eventEntity?.place?.detail?.isNotEmpty() == true) {
                                    Text(
                                        text = list[index]?.eventEntity?.place?.detail ?: ""
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = list[index]?.weatherEntity?.description
                                        ?: "일기예보가 없어요."
                                )
                            }
                        },
                        overlineContent = {
                            Text(text = "$startDateStr$endDateStr")
                        },
                        trailingContent = {
                            if (editModeState.value) {
                                Box(modifier = Modifier.fillMaxHeight()) {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            deleteState.value = list[index]
                                        }
                                            .align(Alignment.Center),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = ""
                                    )
                                }
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
            when (list.loadState.refresh) {
                LoadState.Loading -> {
                    isUpdating.value = true
                }

                is LoadState.Error -> {
                    isUpdating.value = false
                }

                else -> {
                    isUpdating.value = false
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isUpdating.value,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}