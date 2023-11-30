package com.hk.portfolio.today_weather

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.hk.portfolio.today_weather.domain.usecase.event.GetAllEventListUseCase
import com.hk.portfolio.today_weather.presentation.screen.MainNavigationComponent
import com.hk.portfolio.today_weather.presentation.screen.MainViewModel
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
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            LaunchedEffect(Unit) {
                viewModel.checkAndUpdateWeather()
            }

            TodayWeatherPortFolioTheme {
                // A surface container using the 'background' color from the theme
                MainNavigationComponent()
            }
        }
    }
}