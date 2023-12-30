package com.hk.portfolio.today_weather.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.AlarmReceiver
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Random

object AlarmManagerUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun makePushAlarmSchedule(context: Context, notificationId: Int, eventEntity: EventEntity, dateTime: LocalDateTime, alarmManager: AlarmManager) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("title", "${eventEntity.eventName} 일정 알림")
            intent.putExtra("content", "${eventEntity.eventName} 일정 장소의 날씨를 앱에서 확인하세요.")
            intent.putExtra("notificationId", eventEntity.broadcastId)
            PendingIntent.getBroadcast(context, notificationId, intent,
                Intent.FILL_IN_DATA or PendingIntent.FLAG_IMMUTABLE)
        }

        val calendar = with(dateTime) {
            Calendar.getInstance().apply {
                set(year, monthValue - 1, dayOfMonth, hour, minute)
            }
        }
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
        Log.d("alarm", "set Complete :: ${calendar.time}")
    }

    fun cancelPushAlarmSchedule(context: Context, notificationId: Int, alarmManager: AlarmManager) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, notificationId, intent,
                Intent.FILL_IN_DATA or PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeNotificationId(date: LocalDateTime):Int {
        return (DateTimeFormatter.ofPattern("MMddHH").format(date) + Random().nextInt(999).toString()).toInt()
    }
}