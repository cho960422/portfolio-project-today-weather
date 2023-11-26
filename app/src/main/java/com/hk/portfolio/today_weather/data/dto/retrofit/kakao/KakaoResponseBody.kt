package com.hk.portfolio.today_weather.data.dto.retrofit.kakao

data class KakaoResponseBody(
    val documents: List<KakaoLocationDto>? = listOf(),
    val meta: Meta? = Meta()
)