package com.example.yonseiproject.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.yonseiproject.R

object NotificationUtils {
    private const val channelId = "my_channel_id"
    private const val notificationId = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "My Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context,message:String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.emergency)
            .setContentTitle("Wear OS 알림")
            .setContentText("$message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
    fun sendEmergencyNotification(context: Context,message:String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.emergency)
            .setContentTitle("Emergency")
            .setContentText("$message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
    fun sendServiceNotification(context: Context,message:String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.emergency)
            .setContentTitle("Service")
            .setContentText("$message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
    // 알림 예약 함수
    fun sendScheduleNotification(context: Context, patienName:String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.tile_preview)
            .setContentTitle("다음 스케쥴")
            .setContentText("$patienName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}