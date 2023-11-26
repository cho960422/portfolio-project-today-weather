package com.hk.portfolio.today_weather.core

data class UiState<T>(
    val isLoading: Boolean= false,
    val data: T?= null,
    val isError: Boolean= false,
    val message: String= "",
    val isEnd: Boolean = false,
    val errorCode: Int?= null
)
