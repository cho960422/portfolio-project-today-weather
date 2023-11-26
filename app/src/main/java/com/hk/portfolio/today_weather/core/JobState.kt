package com.hk.portfolio.today_weather.core

/**
 * sealed class 관련 공부 해서 블로그에 올리기
 * 트러블슈팅
 */
sealed class JobState<T>(val isLoading: Boolean = false, val data: T? = null, val isError: Boolean = false, val message: String? = null, val isEnd: Boolean? = false) {
    class Loading<T>: JobState<T>(isLoading = true)
    class Success<T>(data: T?, isEnd: Boolean?=null): JobState<T>(data = data, isEnd = isEnd)
    class Error<T>(message: String?): JobState<T>(isError = true, message = message)
}

