package com.hk.portfolio.today_weather.data.service.retrofit

import com.hk.portfolio.today_weather.data.dto.naver.NaverResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverService {
    // 네이버 지도 장소명으로 검색
    @GET("/v1/search/local.json")
    suspend fun getNaverLocationSearchList(
        @Header("X-Naver-Client-Id") clientId:String,
        @Header("X-Naver-Client-Secret") secretKey:String,
        @Query("query") query:String,
        @Query("display") display: Int,
        @Query("start") start: Int,
        @Query("sort") sort: String
    ): NaverResponseBody
}