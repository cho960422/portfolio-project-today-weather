package com.hk.portfolio.today_weather.presentation.screen.tour_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.dpToSp
import com.hk.portfolio.today_weather.core.findActivity
import com.hk.portfolio.today_weather.core.moveByIntent
import com.hk.portfolio.today_weather.presentation.screen.tour_list.viewmodel.TourListScreenViewModel
import com.hk.portfolio.today_weather.ui.component.AddressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourListScreen(
    lat: Double,
    lng: Double,
    name: String,
    addressName: String,
    onBackButtonClicked:() -> Unit
) {
    val viewModel = hiltViewModel<TourListScreenViewModel>()
    val category = viewModel.category.value
    val menuExpanded = remember {
        mutableStateOf(false)
    }
    val tourList = viewModel.tourList.value?.collectAsLazyPagingItems()
    val tourCnt = tourList?.itemCount ?: 0
    val activity = LocalContext.current.findActivity()

    fun getTourList() {
        viewModel.getTourList(lat, lng, category)
        tourList?.refresh()
    }

    BackHandler(enabled = menuExpanded.value) {
        menuExpanded.value = false
    }

    LaunchedEffect(Unit) {
        viewModel.selectTourCategory(null)
    }

    LaunchedEffect(category) {
        getTourList()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "주변 지역")
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable {
                                onBackButtonClicked()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                },
                actions = {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .clickable { menuExpanded.value = !menuExpanded.value },
                        text = category?.description ?: "전체"
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
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (tourList != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp)
                                .padding(bottom = 10.dp),
                            text = "* $addressName 주변의 20km 이내의 관광 정보입니다.",
                            style = TextStyle(
                                fontSize = 12.dp.dpToSp()
                            )
                        )
                    }
                    items(
                        count = tourCnt,
                        key = {
                            it
                        }
                    ) { idx ->
                        tourList[idx]?.let { tourData ->
                            AddressCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.5f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        try {
                                            val url =
                                                with(tourData) {
                                                    "nmap://place?lat=${tourData.lat}&lng=${tourData.lng}&name=${tourData.name}&appname=com.hk.portfolio.today_weather"
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

                    if (tourList.loadState.refresh is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

    }
}