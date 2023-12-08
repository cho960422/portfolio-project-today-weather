package com.hk.portfolio.today_weather.data.repository

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.data.dto.retrofit.tour.TourListPagingSource
import com.hk.portfolio.today_weather.data.service.retrofit.TourService
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.mapper.tour.toEntity
import com.hk.portfolio.today_weather.domain.repository.TourRepository
import javax.inject.Inject

class TourRepositoryImpl @Inject constructor(
    private val tourService: TourService
): TourRepository {
    override fun getTourList(): PagingSource<TourListPagingSource.Request, TourEntity> =
        TourListPagingSource(tourService)
}