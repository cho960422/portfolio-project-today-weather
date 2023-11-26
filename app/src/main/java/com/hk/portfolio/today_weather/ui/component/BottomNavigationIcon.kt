package com.hk.portfolio.today_weather.ui.component

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.hk.portfolio.today_weather.presentation.navigation.Routers

data class BottomNavigationIcon(
    val route: Routers,
    val icon: ImageVector,
    val label: String
)