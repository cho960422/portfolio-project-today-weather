package com.hk.portfolio.today_weather.domain.repository

import com.hk.portfolio.today_weather.domain.entity.weather.WeatherEntity
import com.hk.portfolio.today_weather.domain.entity.weather.WeatherShortEntity
import java.time.LocalDateTime

interface WeatherRepository {

    /**
     *  단기예보를 가져오는 함수
     *  3일 이후 날씨까지 모두 가져올 수 있도록 작업
     *  @param nx : 날씨를 가져올 좌표 x
     *  @param ny : 날씨를 가져올 좌표 y
     *  @param baseDateTime : 일기예보가 발표된 시간을 정해서 넣기
     *                          Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
     *  @param count : 일기예보 패킷을 가져올 총 개수, 한 시간에 10개의 패킷이 필요하기 때문에 3일치의 데이터를 가져오려면 720으로 설정하는 것이 기본
     *  @return 패킷을 Entity로 전환한 목록을 반환, UseCase 에서 필요한 비즈니스 로직 실행
     */
    suspend fun getWeather(nx: Double, ny: Double, baseDateTime: LocalDateTime, count: Int = 720): List<WeatherEntity>

    /**
     *  초단기예보를 가져오는 함수
     *  3일 이후 날씨까지 모두 가져올 수 있도록 작업
     *  @param nx : 날씨를 가져올 좌표 x
     *  @param ny : 날씨를 가져올 좌표 y
     *  @param baseDateTime : 일기예보가 발표된 시간을 정해서 넣기, 현재 시간을 그대로 집어넣으면 서버에서 알아서 알맞는 값으로 변환해서 응답을 보냄
     *  @param count : 일기예보 패킷을 가져올 총 개수, 한 시간에 10개의 패킷이 필요하기 때문에 6시간의 데이터를 가져오려면 60으로 설정하는 것이 기본
     *  @return 패킷을 Entity로 전환한 목록을 반환, UseCase 에서 필요한 비즈니스 로직 실행
     */
    suspend fun getShortWeather(nx: Double, ny: Double, baseDateTime: LocalDateTime): List<WeatherShortEntity>
}