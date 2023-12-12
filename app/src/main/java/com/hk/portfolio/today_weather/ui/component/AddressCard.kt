package com.hk.portfolio.today_weather.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hk.portfolio.today_weather.core.dpToSp
import com.hk.portfolio.today_weather.domain.entity.tour.TourEntity

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    inputAddress: TourEntity
) {
    with(inputAddress) {
        ElevatedCard(
            modifier = modifier
        ) {
            Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .background(
                            androidx.compose.ui.graphics.Color.White,
                            RoundedCornerShape(11.dp)
                        )
                        .border(
                            0.5.dp,
                            androidx.compose.ui.graphics.Color.LightGray,
                            RoundedCornerShape(11.dp)
                        )
                ) {
                    if (image.isNotEmpty()) {
                        AsyncImage(
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            model = image.replace("http", "https"),
                            contentDescription = null,
                            placeholder = painterResource(id = com.hk.portfolio.today_weather.R.drawable.no_photo),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            modifier = androidx.compose.ui.Modifier
                                .align(androidx.compose.ui.Alignment.Center)
                                .size(40.dp),
                            painter = painterResource(id = com.hk.portfolio.today_weather.R.drawable.no_photo),
                            contentDescription = ""
                        )
                    }
                }

                Column(
                    modifier = androidx.compose.ui.Modifier
                        .weight(0.5f)
                        .padding(15.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 17.dp.dpToSp(),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = androidx.compose.ui.Modifier.height(15.dp))
                    Text(
                        text = address + addressDetail,
                        fontSize = 13.dp.dpToSp(),
                        lineHeight = 15.dp.dpToSp()
                    )
                    Text(
                        text = contentType.description,
                        fontSize = 10.dp.dpToSp()
                    )
                }
            }
        }
    }
}