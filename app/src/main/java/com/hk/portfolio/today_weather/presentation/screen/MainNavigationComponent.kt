package com.hk.portfolio.today_weather.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hk.portfolio.today_weather.presentation.navigation.HomeNavigation
import com.hk.portfolio.today_weather.presentation.navigation.Routers
import com.hk.portfolio.today_weather.ui.component.BottomNavigationIcon

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationComponent() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationIcon(
            Routers.HomeRouter,
            Icons.Filled.Home,
            "홈"
        ),
        BottomNavigationIcon(
            Routers.EventListRouter,
            Icons.Filled.List,
            "일정"
        )
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (bottomNavBarVisibility(currentDestination?.route?:"")) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    items.forEach { item ->
                        // 이 부분 공부
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route.route } == true,
                            icon = { Icon(imageVector = item.icon, contentDescription = null) },
                            label = {
                                Text(text = item.label)
                            },
                            onClick = {
                                navController.navigate(item.route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HomeNavigation(navController = navController, innerPadding)
    }
}

fun bottomNavBarVisibility(route:String): Boolean {
    val visibleRoute = listOf(Routers.HomeRouter.route, Routers.EventListRouter.route)
    return visibleRoute.contains(route)
}