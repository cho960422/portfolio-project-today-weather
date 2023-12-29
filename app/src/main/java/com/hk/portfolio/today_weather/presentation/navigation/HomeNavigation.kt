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
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.domain.entity.event.PlaceEntity
import com.hk.portfolio.today_weather.presentation.screen.home.EventListScreen
import com.hk.portfolio.today_weather.presentation.screen.home.HomeScreen
import com.hk.portfolio.today_weather.presentation.screen.tour_list.TourListScreen
import com.hk.portfolio.today_weather.presentation.screen.write.SearchLocationScreen
import com.hk.portfolio.today_weather.presentation.screen.write.WriteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavigation(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = Routers.HomeRouter.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(Routers.HomeRouter.route) {
            HomeScreen {
                navController.navigate(Routers.WriteRouter.route.replace("{id}", ""))
            }
        }
        composable(Routers.EventListRouter.route) { navBackStackEntry ->
            EventListScreen(
                onEventClicked = {
                    val latlng = WeatherUtil.convertGRID_GPS(
                        WeatherUtil.TO_GPS,
                        it.eventEntity.place.nx,
                        it.eventEntity.place.ny
                    )
                    navController.navigate(
                        Routers.TourListRouter.route.replace("{latitude}", latlng.lat.toString())
                            .replace("{longitude}", latlng.lng.toString())
                            .replace("{name}", it.eventEntity.eventName)
                            .replace("{addressName}", it.eventEntity.place.addressName)
                    )
                }
            ) {
                navController.navigate(Routers.WriteRouter.route.replace("{id}", it.id))
            }
        }
        composable(
            Routers.WriteRouter.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val addressName: String? = backStackEntry.savedStateHandle.get<String>("addressName")
            val detail: String? = backStackEntry.savedStateHandle.get<String>("detail")
            val nx: Double? = backStackEntry.savedStateHandle.get<Double>("nx")
            val ny: Double? = backStackEntry.savedStateHandle.get<Double>("ny")

            val data = if (addressName != null && nx != null && ny != null) {
                PlaceEntity(
                    addressName = addressName,
                    nx = nx,
                    ny = ny,
                    detail = detail ?: "알 수 없음"
                )
            } else null

            WriteScreen(
                id = id,
                initPlace = data,
                onSearchButtonClicked = {
                    navController.navigate(Routers.SearchRouter.route)
                },
                onChangePlaceEntity = {
                    backStackEntry.savedStateHandle["addressName"] = it.addressName
                    backStackEntry.savedStateHandle["nx"] = it.nx
                    backStackEntry.savedStateHandle["ny"] = it.ny
                    backStackEntry.savedStateHandle["detail"] = it.detail
                }
            ) {
                navController.popBackStack()
            }
        }
        composable(
            Routers.SearchRouter.route
        ) {
            /**
             * 이전 백스택에 데이터를 남기는 방법, navController의 previousBackStackEntry 에 기록하고 현재 내비게이션 아이템을 팝하면 남길 수 있는 것으로 보임
             * 이 부분도 트러블슈팅
             */
            SearchLocationScreen(onSelectButtonClicked = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "addressName",
                    it.addressName
                )
                navController.previousBackStackEntry?.savedStateHandle?.set("nx", it.nx)
                navController.previousBackStackEntry?.savedStateHandle?.set("ny", it.ny)
                navController.previousBackStackEntry?.savedStateHandle?.set("detail", it.detail)
                navController.popBackStack()
            }) {
                navController.popBackStack()
            }
        }
        composable(
            Routers.TourListRouter.route,
            arguments = listOf(
                navArgument(
                    "latitude"
                ) {
                    type = NavType.FloatType
                },
                navArgument(
                    "longitude"
                ) {
                    type = NavType.FloatType
                },
                navArgument(
                    "name"
                ) {
                    type = NavType.StringType
                },
                navArgument(
                    "addressName"
                ) {
                    type = NavType.StringType
                },
            )
        ) {
            val lat = it.arguments?.getFloat("latitude")?.toDouble() ?: 0.0
            val lng = it.arguments?.getFloat("longitude")?.toDouble() ?: 0.0
            val name = it.arguments?.getString("name") ?: "알 수 없음"
            val addressName = it.arguments?.getString("addressName") ?: "알 수 없음"
            TourListScreen(lat = lat, lng = lng, name = name, addressName = addressName) {
                navController.popBackStack()
            }
        }
    }
}