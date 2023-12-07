package com.hk.portfolio.today_weather.domain.mapper.kakao

import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.data.dto.retrofit.kakao.KakaoLocationDto
import com.hk.portfolio.today_weather.domain.entity.kakao.KakaoLocationEntity

fun KakaoLocationDto.toEntity(): KakaoLocationEntity {
    val location = WeatherUtil.convertGRID_GPS(WeatherUtil.TO_GRID,y?.toDouble()?: 37.0, x?.toDouble()?: 127.0)
    return KakaoLocationEntity(
        id = id?: "",
        title = place_name?: "알 수 없음",
        address = address_name?: "알 수 없음",
        roadAddress = road_address_name?: "알 수 없음",
        category = category_name?: "알 수 없음",
        nx = location.x,
        ny = location.y,
    )
}