package com.hk.portfolio.today_weather.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.AlarmReceiver
import java.time.LocalDateTime
import java.util.Calendar

object AlarmManagerUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun makePushAlarmSchedule(context: Context, dateTime: LocalDateTime, alarmManager: AlarmManager) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("data", "testData")
            PendingIntent.getBroadcast(context, 0, intent,
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
}