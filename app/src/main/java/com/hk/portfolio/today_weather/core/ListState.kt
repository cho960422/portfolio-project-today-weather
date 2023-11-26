package com.hk.portfolio.today_weather.core

data class ListState<T>(
    val data: List<T>,
    val isEnd: Boolean
)
