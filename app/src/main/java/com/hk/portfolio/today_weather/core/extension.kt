package com.hk.portfolio.today_weather.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.dpToSp() = with(LocalDensity.current) { toSp() }