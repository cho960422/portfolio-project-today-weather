package com.hk.portfolio.today_weather.presentation.screen.home

import android.Manifest
import android.os.Build
import android.view.RoundedCorner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.hk.portfolio.today_weather.R
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.core.dpToSp
import com.hk.portfolio.today_weather.core.findActivity
import com.hk.portfolio.today_weather.core.moveByIntent
import com.hk.portfolio.today_weather.presentation.screen.home.viewmodel.HomeScreenViewModel
import com.hk.portfolio.today_weather.ui.component.AddressCard
import com.hk.portfolio.today_weather.ui.component.EventAndWeatherCardView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onCreateButtonClicked: () -> Unit
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val refreshScope = rememberCoroutineScope()
    val isUpdating = viewModel.isUpdating.value
    val menuExpanded = remember {
        mutableStateOf(false)
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isUpdating,
        onRefresh = {
            refreshScope.launch {
                viewModel.checkAndUpdateWeather()
            }
        }
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val todayEventList = viewModel.todayEventList.collectAsState()
    val categoryState = viewModel.category.collectAsState(initial = null)
    val pagerState = rememberPagerState {
        todayEventList.value.size
    }
    val currentWeather = viewModel.currentWeather.value
    val tourListSearchCondition = remember {
        mutableStateOf<Pair<Int, TourContentTypeEnum?>>(Pair(-1, null))
    }
    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // 사용자가 권한 팝업에서 어떠한 액션을 취했을 때에 대한 callback
            when {
                // 정확한 위치 권한을 허용했을 때
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    viewModel.getCurrentWeather()
                }
                // 대략적인 위치 권한을 허용했을 때
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    viewModel.getCurrentWeather()
                }

                else -> {
                    // No location access granted.
                }
            }
        })
    val tourList = viewModel.tourList.value?.collectAsLazyPagingItems()
    val tourCnt = tourList?.itemCount ?: 0
    val activity = LocalContext.current.findActivity()

    fun getTourList() {
        viewModel.getTourList(
            todayEventList.value[pagerState.currentPage],
            category = categoryState.value
        )
        tourList?.refresh()
    }

    LaunchedEffect(Unit) {
        viewModel.checkAndUpdateWeather()
    }
    if (todayEventList.value.isNotEmpty()) {
        LaunchedEffect(pagerState.currentPage) {
            tourListSearchCondition.value = tourListSearchCondition.value.copy(
                first = pagerState.currentPage
            )
        }
        LaunchedEffect(categoryState.value) {
            tourListSearchCondition.value = tourListSearchCondition.value.copy(
                second = categoryState.value
            )
        }
        LaunchedEffect(tourListSearchCondition.value) {
            getTourList()
        }
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
                viewModel.checkAndUpdateWeather()
                resultLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        // 권한을 추가로 요청할 것이라면 이 목록에 추가
                    )
                )
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        with(currentWeather.data) {
                            val condition = this?.weatherCondition ?: WeatherConditionEnum.Unknown
                            val description = when (condition) {
                                WeatherConditionEnum.Rain -> "비가 오고 있어요."
                                WeatherConditionEnum.Snow -> "눈이 오고 있어요."
                                WeatherConditionEnum.RainOrSnow -> "눈/비가 오고 있어요."
                                WeatherConditionEnum.Unknown -> "날씨를 업데이트 중이에요."
                                else -> "눈/비 예보가 없어요"
                            }
                            EventAndWeatherCardView(
                                weatherCondition = this?.weatherCondition
                                    ?: WeatherConditionEnum.Unknown,
                                name = "현재 위치",
                                addressDetail = "",
                                addressName = "",
                                content = description,
                                isUpdate = currentWeather.isLoading
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "오늘 방문하시는 곳"
                    )
                    if (todayEventList.value.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .clickable { onCreateButtonClicked() }
                                .fillMaxWidth()
                                .padding(vertical = 40.dp)
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "오늘 방문하는 지역이 없습니다.\n방문하실 지역을 등록해보세요."
                                )
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = ""
                                )
                            }
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            with(todayEventList.value[it]) {
                                EventAndWeatherCardView(
                                    weatherCondition = weatherEntity?.weatherCondition,
                                    name = eventEntity.eventName,
                                    addressDetail = eventEntity.place.detail,
                                    addressName = eventEntity.place.addressName,
                                    content = if (weatherEntity?.description != null) "현재 시간 이후로\n" + weatherEntity.description else "",
                                    isUpdate = isUpdating
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = "방문 지역 주변정보"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                modifier = Modifier.clickable {
                                    menuExpanded.value = true
                                },
                                text = categoryState.value?.description ?: "전체"
                            )
                            DropdownMenu(
                                modifier = Modifier,
                                expanded = menuExpanded.value,
                                onDismissRequest = {
                                    menuExpanded.value = false
                                }
                            ) {
                                TourContentTypeEnum.values().forEach {
                                    DropdownMenuItem(onClick = {
                                        viewModel.selectTourCategory(it)
                                        menuExpanded.value = false
                                    }) {
                                        Text(text = it.description)
                                    }
                                }
                            }
                        }
                    }
                    if (todayEventList.value.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 40.dp)
                                .fillMaxWidth()
                                .clickable { onCreateButtonClicked() }
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "오늘 방문하는 지역이 없습니다.\n방문하실 지역을 등록해보세요."
                                )
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = ""
                                )
                            }
                        }
                    } else {
                        if (tourCnt == 0) {
                            when (tourList?.loadState?.refresh) {
                                is LoadState.Error -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.align(Alignment.Center),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "주변정보를 가져오지 못했습니다."
                                            )
                                            Icon(
                                                modifier = Modifier.clickable { getTourList() },
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }

                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(
                                    count = tourCnt,
                                    key = {
                                        it
                                    }
                                ) { idx ->
                                    tourList?.get(idx)?.let { tourData ->
                                        AddressCard(
                                            modifier = Modifier
                                                .width(300.dp)
                                                .aspectRatio(0.9f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable {
                                                    try {
                                                        val url =
                                                            with(tourData) {
                                                                "nmap://place?lat=$lat&lng=$lng&name=$name&appname=com.hk.portfolio.today_weather"
                                                            }
                                                        moveByIntent(activity, url)
                                                    } catch (e: Exception) {
                                                        val url =
                                                            "market://details?id=com.nhn.android.nmap"
                                                        moveByIntent(activity, url)
                                                    }
                                                },
                                            tourData
                                        )
                                    }
                                }

                                if (tourList?.loadState?.refresh is LoadState.Loading) {
                                    item {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { onCreateButtonClicked() },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(imageVector = Icons.Filled.Create, contentDescription = null)
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isUpdating,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}