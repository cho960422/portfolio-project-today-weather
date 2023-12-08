package com.hk.portfolio.today_weather.domain.usecase.tour

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.core.util.WeatherUtil.TO_GPS
import com.hk.portfolio.today_weather.data.dto.retrofit.tour.TourListPagingSource
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity
import com.hk.portfolio.today_weather.domain.repository.TourRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTourListUseCase @Inject constructor(
    private val tourRepository: TourRepository
): BaseUseCase<Unit, PagingSource<TourListPagingSource.Request, TourEntity>> {
    override fun invoke(param: Unit): PagingSource<TourListPagingSource.Request, TourEntity> {
        return tourRepository.getTourList()
    }
}