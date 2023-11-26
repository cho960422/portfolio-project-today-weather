package com.hk.portfolio.today_weather.data.repository

import com.hk.portfolio.today_weather.core.ListState
import com.hk.portfolio.today_weather.data.service.retrofit.KakaoService
import com.hk.portfolio.today_weather.domain.entity.kakao.KakaoLocationEntity
import com.hk.portfolio.today_weather.domain.mapper.kakao.toEntity
import com.hk.portfolio.today_weather.domain.repository.KakaoLocationRepository
import javax.inject.Inject

class KakaoLocationRepositoryImpl @Inject constructor(
    private val kakaoService: KakaoService
) : KakaoLocationRepository {
    override suspend fun getLocationList(query: String, page: Int): ListState<KakaoLocationEntity> {
        val response = kakaoService.getKakaoLocationSearchList(
            query = query,
            page = page,
            size = 15
        );
        val list = response.documents ?: listOf()

        return ListState(list.map {
            it.toEntity()
        }, isEnd = response.meta?.is_end ?: true)
    }
}