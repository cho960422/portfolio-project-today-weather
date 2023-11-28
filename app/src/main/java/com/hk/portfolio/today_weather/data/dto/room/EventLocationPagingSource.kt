package com.hk.portfolio.today_weather.data.dto.room

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity

class EventLocationPagingSource: PagingSource<Int, EventEntity>() {
    override fun getRefreshKey(state: PagingState<Int, EventEntity>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventEntity> {
        TODO("Not yet implemented")
    }
}