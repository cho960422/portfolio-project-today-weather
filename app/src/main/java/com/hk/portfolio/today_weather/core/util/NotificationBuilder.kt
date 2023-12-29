package com.hk.portfolio.today_weather.core.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.hk.portfolio.today_weather.MainActivity
import com.hk.portfolio.today_weather.R

class NotificationBuilder {
    companion object {
        private const val EVENT_CHANNEL_NAME = "일정 알림"
        private const val CHANNEL_ID = "dlk4fj3fd4asl38dlk3faj"
        const val requestCode = 0
        const val flags = 0
        fun createEventPushChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = EVENT_CHANNEL_NAME
                val descriptionText = "일정 관련 알림 채널입니다."
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun getNotificationBuilder(
            title: String,
            content:String,
            notificationId:Int,
            context: Context
        ): NotificationCompat.Builder {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(context, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE)

            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        @SuppressLint("MissingPermission")
        fun notify(
            title: String,
            content:String,
            notificationId:Int,
            context: Context
        ) {
            val builder = getNotificationBuilder(title, content, notificationId, context)
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }
    }
}