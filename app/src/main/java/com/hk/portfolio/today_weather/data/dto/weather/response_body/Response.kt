package com.hk.portfolio.today_weather.data.dto.weather.response_body

data class Response<T>(
    val body: Body<T>? = Body(),
    val header: Header? = Header()
)