package com.hk.portfolio.today_weather.domain.usecase.weather

interface BaseUseCase<ParamType, ReturnType> {
    operator fun invoke(param: ParamType): ReturnType
}