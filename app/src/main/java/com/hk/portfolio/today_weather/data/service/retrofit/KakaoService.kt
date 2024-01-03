package com.hk.portfolio.today_weather.data.service.retrofit

import com.hk.portfolio.today_weather.BuildConfig
import com.hk.portfolio.today_weather.data.dto.retrofit.kakao.KakaoResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {
    companion object {
        private const val restApiKey = BuildConfig.KAKAO_API_KEY
    }
    // 네이버 지도 장소명으로 검색
    @GET("v2/local/search/keyword.json")
    suspend fun getKakaoLocationSearchList(
        @Query("query") query:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") authorization:String = restApiKey
    ): KakaoResponseBody
}