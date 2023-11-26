package com.hk.portfolio.today_weather.domain.entity

import com.hk.portfolio.today_weather.core.SearchCategoryEnum

data class InsertHistoryRequest(
    val query:String,
    val category: SearchCategoryEnum
)
