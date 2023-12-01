package com.hk.portfolio.today_weather.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hk.portfolio.today_weather.R
import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import com.hk.portfolio.today_weather.core.bigTextSize
import com.hk.portfolio.today_weather.core.dpToSp
import com.hk.portfolio.today_weather.core.eventWeatherCardContentStyle
import com.hk.portfolio.today_weather.core.eventWeatherCardHeadStyle
import com.hk.portfolio.today_weather.core.normalTextSize
import com.hk.portfolio.today_weather.core.smallTextSize
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity

@Composable
fun EventAndWeatherCardView(
    weatherCondition: WeatherConditionEnum?,
    addressDetail: String,
    addressName:String,
    content: String
) {
    val image = mutableListOf<Painter>()
    if (weatherCondition == WeatherConditionEnum.Clean) {
        image.add(painterResource(id = R.drawable.clean_sky))
    } else if (weatherCondition == WeatherConditionEnum.Rain) {
        image.add(painterResource(id = R.drawable.rain))
    } else if (weatherCondition == WeatherConditionEnum.Snow) {
        image.add(painterResource(id = R.drawable.snow))
    } else if (weatherCondition == WeatherConditionEnum.RainOrSnow) {
        image.add(painterResource(id = R.drawable.rain))
        image.add(painterResource(id = R.drawable.snow))
    } else {
        image.add(painterResource(id = R.drawable.clean_sky))
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                image.forEach {
                    Image(
                        modifier = Modifier.weight(1f),
                        painter = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000))
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp),
                    text = addressName,
                    style = eventWeatherCardHeadStyle,
                    fontSize = normalTextSize.dpToSp()
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 20.dp),
                    text = addressDetail,
                    style = eventWeatherCardHeadStyle,
                    fontSize = smallTextSize.dpToSp()
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    text = content,
                    style = eventWeatherCardContentStyle,
                    fontSize = bigTextSize.dpToSp()
                )
            }
        }
    }
}