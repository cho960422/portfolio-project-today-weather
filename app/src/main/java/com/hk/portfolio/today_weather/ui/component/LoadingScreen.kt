package com.hk.portfolio.today_weather.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingScreen() {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xbb000000))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {  }
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}