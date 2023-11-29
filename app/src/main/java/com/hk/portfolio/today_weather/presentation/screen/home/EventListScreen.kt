package com.hk.portfolio.today_weather.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.hk.portfolio.today_weather.presentation.screen.home.viewmodel.EventListScreenViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListScreen(

) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val viewModel = hiltViewModel<EventListScreenViewModel>()
    val list = viewModel.items.collectAsLazyPagingItems()

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "방문지역 목록")
                }
            )
        }
    ) { paddingValue ->
        Surface(
            modifier = Modifier.padding(paddingValue)
        ) {
            when (list.loadState.refresh) {
                LoadState.Loading -> {

                }

                is LoadState.Error -> {

                }

                else -> {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(all = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        itemsIndexed(list.itemSnapshotList) { index, item ->
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val startDateStr = formatter.format(item?.startDate)
                            val endDateStr = if (item?.endDate == null) "" else " ~ ${
                                formatter.format(item.endDate)
                            }"
                            ListItem(
                                headlineContent = {
                                    Text(text = item?.eventName ?: "알 수 없는 일정")
                                },
                                supportingContent = {
                                    Column {
                                        Text(text = item?.place?.addressName ?: "")
                                        if (item?.place?.detail?.isNotEmpty() == true) {
                                            Text(text = item.place.detail ?: "")
                                        }
                                    }
                                },
                                overlineContent = {
                                    Text(text = "$startDateStr$endDateStr")
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}