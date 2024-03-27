package com.example.notify

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.NotificationCompat

class WaterApplicationService(
    private val context: Context
) {
    private var notificationManager = context.getSystemService(NotificationManager::class.java)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(timeInterval: String) {
        Toast.makeText(context, "time interval: $timeInterval", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // set the alarm to start at the desired time
        val triggerAtMillis = SystemClock.elapsedRealtime() + timeInterval.toInt()

        // set the alarm to repeat
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            timeInterval.toLong(),
            pendingIntent
        )
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(
            context,
            "water_notification"
        )
            .setContentTitle("Water Reminder")
            .setContentText("Time to drink water")
            .setSmallIcon(R.drawable.alarm)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            101,
            notification
        )
    }
}