package com.hk.portfolio.today_weather.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.dpToSp() = with(LocalDensity.current) { toSp() }

fun moveByIntent(activity: Activity, data: String) {
    activity.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(data)
        )
    )
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}