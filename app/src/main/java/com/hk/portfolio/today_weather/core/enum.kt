package com.hk.portfolio.today_weather.core

/**
 * 등록한 일정들의 날씨 데이터 카테고리를 변환하는 클래스
 * WeatherDto의 category를 대조해서 변환한다.
 */
enum class WeatherCategoryEnum(value:String, description:String) {
    RainPercent("POP", "강수확률, % 단위"),
    RainForm("PTY", "강수형태, 코드값"),
    RainPerHour("PCP", "1시간 강수량, 단위 : mm"),
    Humidity("REH", "습도, %"),
    SnowPerHour("SNO", "1시간 눈 내린 양, 단위 : cm"),
    SkyCondition("SKY", "하늘상태"),
    TemperatureThisHour("TMP", "1시간 기온, 단위: 섭씨"),
    TemperatureMin("TMN", "일 최저기온, 단위: 섭씨"),
    TemperatureMax("TMX", "일 최고기온, 단위: 섭씨"),
    WindEW("UUU", "풍속(동서), 단위 m/s"),
    WindSN("VVV", "풍속(남북), 단위 m/s"),
    WindSpeed("WSD", "풍속, 단위 m/s")
}

/**
 * 현재 장소의 날씨의 카테고리를 변환하는 클래스.
 * WeatherNowDto의 category를 변환하면 된다.
 */
enum class WeatherNowCategoryEnum(value:String, description: String) {
    Temperature("T1H", "기온, 섭씨"),
    WeatherCondition("PTY", "강수형태"),
    WindSpeed("WSD", "풍속"),
}

enum class WeatherConditionEnum(value:Int) {
    /**
     * 강수 없음
     */
    Clean(0),
    /**
     * 비
     */
    Rain(1),
    /**
     * 비/눈
     */
    RainOrSnow(2),
    /**
     * 눈
     */
    Snow(3),
    /**
     * 소나기
     */
    Shower(4),
    /**
     * 빗방울
     */
    Raindrop(5),
    /**
     * 빗방울/눈날림
     */
    RaindropOrSnowdrop(6),
    /**
     * 눈날림
     */
    Snowdrop(7),
}

enum class CloudCondition {
    /**
     * 맑음
     */
    Sunny,
    /**
     * 구름많음
     */
    Cloudy,
    /**
     * 흐림
     */
    Overcast,
}