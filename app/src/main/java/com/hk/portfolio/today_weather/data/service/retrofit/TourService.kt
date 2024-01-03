package com.hk.portfolio.today_weather.data.service.retrofit

import com.hk.portfolio.today_weather.BuildConfig
import com.hk.portfolio.today_weather.data.dto.retrofit.tour.TourDto
import com.hk.portfolio.today_weather.data.dto.retrofit.weather.response_body.PublicResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate

interface TourService {
    companion object {
        const val mHost: String = BuildConfig.TOUR_HOST
        const val serviceKey = BuildConfig.TOUR_API_KEY
    }
    @GET("$mHost/locationBasedList1")
    suspend fun getTourList(
        @Query("mapX") lng: Double,
        @Query("mapY") lat: Double,
        @Query("numOfRows") size: Int,
        @Query("pageNo") page: Int,
        @Query("contentTypeId") category: Int?,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") appName:String = "오늘의 날씨",
        @Query("radius") nx: String = "20000",
        @Query("serviceKey") serviceKey: String = TourService.serviceKey,
        @Query("_type") type: String= "json",
    ): PublicResponseBody<TourDto>
}