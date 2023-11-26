package com.hk.portfolio.today_weather

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.hk.portfolio.today_weather.domain.usecase.event.GetEventListUseCase
import com.hk.portfolio.today_weather.presentation.screen.MainNavigationComponent
import com.hk.portfolio.today_weather.ui.theme.TodayWeatherPortFolioTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<TestViewModel>()
            TodayWeatherPortFolioTheme {
                // A surface container using the 'background' color from the theme
                MainNavigationComponent()
            }
        }
    }
}

@HiltViewModel
class TestViewModel @Inject constructor(
    private val getEventListUseCase: GetEventListUseCase
): ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getList() {
        val eventList = getEventListUseCase(LocalDate.now())
        Log.d("eventList ::", eventList.toString())
    }
}