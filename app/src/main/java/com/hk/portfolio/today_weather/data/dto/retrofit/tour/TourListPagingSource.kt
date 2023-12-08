package com.hk.portfolio.today_weather.data.dto.retrofit.tour

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.mapper.tour.toEntity
import javax.inject.Inject
import kotlin.math.max

class TourListPagingSource @Inject constructor(
    private val service: TourService
): PagingSource<TourListPagingSource.Request, TourEntity>() {
    class Request(
        val latlng: WeatherUtil.LatXLngY,
        val page: Int
    )
    private val START_KEY = 0
    /**
     * key가 START_KEY아래로 내려가면 초기값인 null을 반환할 수 있도록 하는 함수 정의
     */
    private fun ensureValidKey(key: Int) = max(START_KEY, key)
    override fun getRefreshKey(state: PagingState<Request, TourEntity>): Request? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Request>): LoadResult<Request, TourEntity> {
        val page: Int = params.key?.page ?: START_KEY
        // limit의 마지막 값을 만들어준다.
        val latlng = params.key?.latlng
        if (latlng == null) invalidate()
        val list =  if (latlng != null) {
            val response = service.getTourList(
                lng = latlng.lng, lat = latlng.lat, size = params.loadSize, page = page
            )
            response.response?.body?.items?.item?: listOf()
        } else listOf()

        return LoadResult.Page(
            data = list.map {
                // 쿼리 결과 가져온 목록을 Entity로 변환하여 내보내기
                it.toEntity()
            },
            prevKey = when (page) {
                START_KEY -> null
                else -> Request(
                    latlng!!,
                    ensureValidKey(page - 1)
                )
            },
            nextKey = Request(
                latlng!!,
                ensureValidKey(page + 1)
            )
        )
    }

}