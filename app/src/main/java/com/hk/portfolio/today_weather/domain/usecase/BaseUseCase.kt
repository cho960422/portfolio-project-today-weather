package com.hk.portfolio.today_weather.domain.usecase

interface BaseUseCase<ParamType, ReturnType> {
    suspend operator fun invoke(param: ParamType): ReturnType
}