package com.hk.portfolio.today_weather.domain.usecase

interface CoroutineUseCase<ParamType, ReturnType> {
    suspend operator fun invoke(param: ParamType): ReturnType
}