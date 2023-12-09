package com.hk.portfolio.today_weather.domain.repository

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.core.util.WeatherUtil
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity

interface TourRepository {
    fun getTourList(latXLngY: WeatherUtil.LatXLngY, category: TourContentTypeEnum?): PagingSource<Int, TourEntity>
}