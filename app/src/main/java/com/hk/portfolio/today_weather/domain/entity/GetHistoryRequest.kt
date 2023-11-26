package com.hk.portfolio.today_weather.domain.entity

import com.hk.portfolio.today_weather.core.SearchCategoryEnum

data class GetHistoryRequest(
    val query:String,
    val category: SearchCategoryEnum
)
