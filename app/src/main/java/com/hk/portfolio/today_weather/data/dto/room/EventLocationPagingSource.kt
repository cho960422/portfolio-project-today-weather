package com.hk.portfolio.today_weather.data.dto.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.mapper.event.toEntity
import java.time.LocalDate
import java.util.Collections.max
import javax.inject.Inject
import kotlin.math.max

class EventLocationPagingSource @Inject constructor(
    // Room에 접근할 수 있는 구현체를 주입
    private val db: AppDatabase
) : PagingSource<Int, EventEntity>() {
    // 실질적인 쿼리를 날릴 수 있는 Dao
    val dao = db.eventDao()
    // 시작점 인덱스 정의
    private val START_KEY = 0

    /**
     * key가 START_KEY아래로 내려가면 초기값인 null을 반환할 수 있도록 하는 함수 정의
     */
    private fun ensureValidKey(key: Int) = max(START_KEY, key)

    // 새로고침할 때의 refresh 또는 읽을 수 없는 invalidate가 호출되었을 때 키를 재정의하는 함수
    override fun getRefreshKey(state: PagingState<Int, EventEntity>): Int? {
        val key = state.anchorPosition?: return null
        return ensureValidKey(key - (state.config.pageSize / 2))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventEntity> {
        // 키는 nullable한 타입이기 때문에 키가 없을 경우 최초로 페이지네이션을 시작할 때의 값을 넣어준다.
        val start: Int = params.key ?: START_KEY
        // limit의 마지막 값을 만들어준다.
        val end: Int = start + params.loadSize
        val list = dao.getEventList(start, end)

        return LoadResult.Page(
            data = list.map {
                // 쿼리 결과 가져온 목록을 Entity로 변환하여 내보내기
                it.toEntity()
            },
            prevKey = when (start) {
                START_KEY -> null
                else -> ensureValidKey(start - params.loadSize)
            },
            nextKey = end + 1
        )
    }
}