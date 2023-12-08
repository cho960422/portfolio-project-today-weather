package com.hk.portfolio.today_weather.domain.mapper.tour

import com.hk.portfolio.today_weather.core.TourContentTypeEnum
import com.hk.portfolio.today_weather.data.dto.retrofit.tour.TourDto
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity

fun TourDto.toEntity(): TourEntity {
    return TourEntity(
        id = contentid,
        name = title,
        address = addr1,
        addressDetail = addr2,
        image = firstimage,
        lat = mapy,
        lng = mapx,
        contentType = TourContentTypeEnum.findByCode(contenttypeid)
    )
}