package com.hk.portfolio.today_weather.domain.usecase.kakao

import com.hk.portfolio.today_weather.core.JobState
import com.hk.portfolio.today_weather.domain.entity.kakao.KakaoLocationEntity
import com.hk.portfolio.today_weather.domain.repository.KakaoLocationRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(
    private val kakaoLocationRepository: KakaoLocationRepository
) : BaseUseCase<SearchLocationUseCase.Request, Flow<JobState<List<KakaoLocationEntity>>>> {
    data class Request(
        val query: String,
        val page: Int
    )

    override suspend fun invoke(param: Request): Flow<JobState<List<KakaoLocationEntity>>> = flow {
        try {
            emit(JobState.Loading())
            val response = kakaoLocationRepository.getLocationList(
                query = param.query,
                page = param.page
            )
            emit(JobState.Success(response.data, response.isEnd))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(JobState.Error("데이터를 가져오는 도중 오류가 발생했습니다."))
        }
    }
}