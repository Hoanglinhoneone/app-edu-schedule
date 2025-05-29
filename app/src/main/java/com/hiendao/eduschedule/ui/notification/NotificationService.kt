package com.hiendao.eduschedule.ui.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager

class NotificationService : Service() {

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        sendBroadcast()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NotificationService::lock")
        wakeLock.acquire()
        return START_STICKY
    }

    override fun onDestroy() {
        sendBroadcast()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendBroadcast() {
        val offlineNotification = OfflineNotification(applicationContext)

    }
}