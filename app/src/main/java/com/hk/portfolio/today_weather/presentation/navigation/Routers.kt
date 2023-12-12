package com.hk.portfolio.today_weather.presentation.navigation

sealed class Routers(val route:String) {
    object HomeRouter: Routers("home")
    object EventListRouter: Routers("event_list")
    object WriteRouter: Routers("write/{isNew}")
    object SearchRouter: Routers("search")
    object TourListRouter: Routers("tour/{latitude}/{longitude}/{name}/{addressName}")
}