package com.hk.portfolio.today_weather.data.dto.retrofit.kakao

data class Meta(
    val is_end: Boolean? = false,
    val pageable_count: Int? = 0,
    val same_name: SameName? = SameName(),
    val total_count: Int? = 0
)