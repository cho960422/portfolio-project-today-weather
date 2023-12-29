package com.hk.portfolio.today_weather.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.util.NotificationBuilder
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity

class AlarmReceiver: BroadcastReceiver() {
    val random = java.util.Random()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent?.getStringExtra("data")
        if (context != null) {
            NotificationBuilder.notify(title = "테스트", content = "알림 수신. $data", random.nextInt(2000000), context)
        }
        Log.d("alarm", "received")
        submitAllPushAlarm(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAllPushAlarm(context: Context?) {
        context?.let {
            NotificationBuilder.notify(title = "테스트", content = "목록 비어있음.", random.nextInt(2000000), context)
        }
    }

    private fun setNotification(event: EventEntity, context: Context) {
        val builder = NotificationBuilder.getNotificationBuilder(
            title = "${event.eventName} 일정 알림",
            content = "${event.eventName} 일정 지역의 날씨를 미리 확인하세요.",
            notificationId = event.broadcastId!!,
            context = context
        )
    }
}