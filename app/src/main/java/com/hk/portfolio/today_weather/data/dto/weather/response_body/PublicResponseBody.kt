package com.hk.portfolio.today_weather.data.dto.weather.response_body

data class PublicResponseBody<T>(
    val response: Response<T>? = Response()
)