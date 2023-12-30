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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")?: "오늘의 날씨 알림"
        val content = intent?.getStringExtra("content")?: "예정된 일정 알림입니다. 앱에 접속해서 날씨를 확인하세요."
        val notificationId: Int = intent?.getIntExtra("notificationId", -1)?: -1

        if (context != null && title.isNotEmpty() && content.isNotEmpty() && notificationId != -1) {
            NotificationBuilder.notify(title = title, content = content, notificationId, context)
        }
    }
}