package com.hiendao.eduschedule.ui.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class OfflineNotification(private val context: Context) {

    fun setRepeatingNotification(timeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.SCHEDULE_EXACT_ALARM
            )

            if (alarmPermission == PackageManager.PERMISSION_GRANTED ||
                alarmManager.canScheduleExactAlarms()
            ) {
                // Có quyền, đặt báo thức chính xác
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, timeMillis, 60000L, pendingIntent
                )
            } else {
                // Không có quyền, chỉ       đặt báo thức thông thường
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    timeMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
        } else {
            // Android < 12, đặt báo thức chính xác luôn
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, timeMillis - 60000L, 60000L, pendingIntent
            )
        }
    }

    fun setNotification(timeMillis: Long, title: String, message: String, id: Int, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcast::class.java)
        intent.putExtra("AlarmType", 1)
        intent.putExtra("itemId", id)
        intent.putExtra("AlarmTitle", title)
        intent.putExtra("AlarmMessage", message)
        intent.putExtra("entityType", type)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.SCHEDULE_EXACT_ALARM
            )

            if (alarmPermission == PackageManager.PERMISSION_GRANTED ||
                alarmManager.canScheduleExactAlarms()
            ) {
                // Có quyền, đặt báo thức chính xác
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeMillis,
                    pendingIntent
                )
            } else {
                // Không có quyền, chỉ đặt báo thức thông thường
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
            }
        } else {
            // Android < 12, đặt báo thức chính xác luôn
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMillis,
                pendingIntent
            )
        }
    }

    fun setAlarm(id: Int, timeMillis: Long, alarmUri: String, title: String, message: String, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcast::class.java)
        intent.putExtra("itemId", id)
        intent.putExtra("AlarmType", 2)
        intent.putExtra("AlarmUri", alarmUri)
        intent.putExtra("AlarmTitle", title)
        intent.putExtra("AlarmMessage", message)
        intent.putExtra("entityType", type)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.SCHEDULE_EXACT_ALARM
            )

            if (alarmPermission == PackageManager.PERMISSION_GRANTED ||
                alarmManager.canScheduleExactAlarms()
            ) {
                // Có quyền, đặt báo thức chính xác
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeMillis,
                    pendingIntent
                )
            } else {
                // Không có quyền, chỉ đặt báo thức thông thường
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
            }
        } else {
            // Android < 12, đặt báo thức chính xác luôn
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMillis,
                pendingIntent
            )
        }
    }
}