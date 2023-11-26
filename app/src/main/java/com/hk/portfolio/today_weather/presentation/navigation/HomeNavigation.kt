package com.hk.portfolio.today_weather.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.presentation.screen.home.EventListScreen
import com.hk.portfolio.today_weather.presentation.screen.home.HomeScreen
import com.hk.portfolio.today_weather.presentation.screen.write.SearchLocationScreen
import com.hk.portfolio.today_weather.presentation.screen.write.WriteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavigation(navController: NavHostController, padding: PaddingValues) {

    NavHost(navController = navController, startDestination = Routers.HomeRouter.route, modifier = Modifier.padding(padding)) {
        composable(Routers.HomeRouter.route) {
            HomeScreen {
                navController.navigate(Routers.WriteRouter.route.replace("{isNew}", "1"))
            }
        }
        composable(Routers.EventListRouter.route) {
            EventListScreen()
        }
        composable(
            Routers.WriteRouter.route,
            arguments = listOf(
                navArgument("isNew") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val isNew = backStackEntry.arguments?.getInt("isNew") == 1
            val data: String? = backStackEntry
                .savedStateHandle
                .get<String>("place")

            WriteScreen(
                isNew = isNew,
                initPlace = data,
                onSearchButtonClicked = {
                    navController.navigate(Routers.SearchRouter.route)
                }
            ) {
                navController.popBackStack()
            }
        }
        composable(
            Routers.SearchRouter.route
        ) {
            SearchLocationScreen(onSelectButtonClicked = {

            }) {
                navController.popBackStack()
            }
        }
    }
}