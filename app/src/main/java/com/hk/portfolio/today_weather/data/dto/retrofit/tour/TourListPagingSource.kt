package com.hk.portfolio.today_weather.data.dto.retrofit.tour

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonParseException
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.mapper.tour.toEntity
import javax.inject.Inject
import kotlin.math.max

class TourListPagingSource @Inject constructor(
    private val service: TourService,
    private val latlng: WeatherUtil.LatXLngY,
    private val category: Int?
): PagingSource<Int, TourEntity>() {
    private val START_KEY = 0
    /**
     * key가 START_KEY아래로 내려가면 초기값인 null을 반환할 수 있도록 하는 함수 정의
     */
    private fun ensureValidKey(key: Int) = max(START_KEY, key)
    override fun getRefreshKey(state: PagingState<Int, TourEntity>): Int? {
        val key = state.anchorPosition?: return null
        return ensureValidKey(key)
    }

    @Throws(JsonParseException::class)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TourEntity> {
        try {
            val page: Int = params.key ?: START_KEY
            val list = service.getTourList(
                lng = latlng.lng, lat = latlng.lat, size = params.loadSize, page = page, category = category
            ).response?.body?.items?.item?: listOf()

            return LoadResult.Page(
                data = list.map {
                    // 쿼리 결과 가져온 목록을 Entity로 변환하여 내보내기
                    it.toEntity()
                },
                prevKey = when (page) {
                    START_KEY -> null
                    else -> ensureValidKey(page - 1)
                },
                nextKey = ensureValidKey(page + 1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}