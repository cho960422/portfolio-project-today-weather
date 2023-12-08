package com.hk.portfolio.today_weather.domain.repository

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.data.dto.retrofit.tour.TourListPagingSource
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity

interface TourRepository {
    fun getTourList(): PagingSource<TourListPagingSource.Request, TourEntity>
}