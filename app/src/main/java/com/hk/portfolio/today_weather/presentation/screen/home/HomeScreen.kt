package com.hk.portfolio.today_weather.presentation.screen.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.presentation.screen.home.viewmodel.HomeScreenViewModel
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
    val pagerState = rememberPagerState {
        todayEventList.value.size
    }
    val currentWeather = viewModel.currentWeather.value
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

    LaunchedEffect(Unit) {
        viewModel.checkAndUpdateWeather()
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
                        val condition = this?.weatherCondition?: WeatherConditionEnum.Unknown
                        val description = when (condition) {
                            WeatherConditionEnum.Rain -> "비가 오고 있어요."
                            WeatherConditionEnum.Snow -> "눈이 오고 있어요."
                            WeatherConditionEnum.RainOrSnow -> "눈/비가 오고 있어요."
                            WeatherConditionEnum.Unknown -> "날씨를 업데이트 중이에요."
                            else -> "눈/비 예보가 없어요"
                        }
                        EventAndWeatherCardView(
                            weatherCondition = this?.weatherCondition?: WeatherConditionEnum.Unknown,
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
                            content = weatherEntity?.description ?: "",
                            isUpdate = isUpdating
                        )
                    }
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