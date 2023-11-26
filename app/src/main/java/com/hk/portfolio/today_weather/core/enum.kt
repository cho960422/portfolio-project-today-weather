package com.hk.portfolio.today_weather.core

/**
 * 등록한 일정들의 날씨 데이터 카테고리를 변환하는 클래스
 * WeatherDto의 category를 대조해서 변환한다.
 */
enum class WeatherCategoryEnum(
    val code:String, val description:String
) {
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
    WindSpeed("WSD", "풍속, 단위 m/s"),
    Unknown("unknown", "알 수 없는 코드값");

    companion object {
        fun findByCode(code:String?): WeatherCategoryEnum {
            return WeatherCategoryEnum.values().findLast {
                it.code == code
            }?: Unknown
        }
    }
}

/**
 * 현재 장소의 날씨의 카테고리를 변환하는 클래스.
 * WeatherNowDto의 category를 변환하면 된다.
 */
enum class WeatherShortCategoryEnum(val code:String, val description: String) {
    Temperature("T1H", "기온, 섭씨"),
    WeatherCondition("PTY", "강수형태"),
    WindSpeed("WSD", "풍속"),
    Unknown("unknown", "알 수 없는 코드값");

    companion object {
        fun findByCode(code:String?): WeatherShortCategoryEnum {
            return WeatherShortCategoryEnum.values().findLast {
                it.code == code
            }?: Unknown
        }
    }
}

enum class WeatherConditionEnum(val code:Int) {
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
    Unknown(-100);

    fun findByCode(code:Int): WeatherConditionEnum {
        return WeatherConditionEnum.values().findLast {
            it.code == code
        }?: Unknown
    }
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
    Unknown;
}

enum class SearchCategoryEnum(
    val category:Int
) {
    AddressSearch(1);
}