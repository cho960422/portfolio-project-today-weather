package com.hk.portfolio.today_weather.domain.repository

import com.hk.portfolio.today_weather.core.ListState
import com.hk.portfolio.today_weather.domain.entity.kakao.KakaoLocationEntity

interface KakaoLocationRepository {
    suspend fun getLocationList(query: String, page: Int): ListState<KakaoLocationEntity>
}