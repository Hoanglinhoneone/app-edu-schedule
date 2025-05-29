package com.hiendao.eduschedule.ui.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.hiendao.eduschedule.MainActivity
import com.hiendao.eduschedule.MainApplication
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.control.repository.remote.AlarmRepository
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepository
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleType
import com.hiendao.eduschedule.utils.Constants
import com.higherstudio.calculatorlauncher.calculatorvault.vault.hidemedia.hideapp.local.LocalData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBroadcast: BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var scheduleRepository: ScheduleRepository

    override fun onReceive(context: Context?, intent: Intent) {
        if (context == null) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val application = context.applicationContext

        val itemId = intent.getIntExtra("itemId", 0)
        val alarmType = intent.getIntExtra("AlarmType", 1)
        val alarmUri = intent.getStringExtra("AlarmUri")
        val alarmTitle = intent.getStringExtra("AlarmTitle")
        val alarmMessage = intent.getStringExtra("AlarmMessage")
        val entityType = intent.getStringExtra("entityType")
        when(intent.action){
            DONE -> {
                MusicControl.getInstance(context).stopMusic()
                if(alarmType == 2){
                    runBlocking {
                        println("Tắt alarm")
                    }
                } else if(alarmType == 1){
                    // Update scheduleId to joined
                    runBlocking {
                        println("Tham gia")
                        entityType?.let {
                            val state =
                                when(entityType){
                                    ScheduleType.LESSON.toString() -> Constants.ScheduleLearningState.PRESENT
                                    ScheduleType.ASSIGNMENT.toString() -> Constants.AssignmentPersonalState.COMPLETE
                                    else -> Constants.AssignmentPersonalState.COMPLETE
                                }
                            scheduleRepository.updateStateEvent(itemId, state, entityType)
                        }
                    }
                }
                val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(Constants.NOTIFICATION_ID)
            }
            REJECT -> {
                    // Update scheduleId to missed
                runBlocking {
                    println("Vắng mặt")
                }
            }
            else -> {
                var notificationTitle = if(alarmType == 2){
                    context.getString(R.string.alarm_title)
                } else {
                    context.getString(R.string.notification_title)
                }

                var actionContent = if(alarmType == 2){
                    context.getString(R.string.alarm_done_content)
                } else {
                    context.getString(R.string.notification_done_content)
                }

                if(!alarmTitle.isNullOrEmpty()){
                    notificationTitle = alarmTitle
                }
                val contentText = if(!alarmMessage.isNullOrEmpty()){
                    alarmMessage
                } else "Bạn có sự kiện sắp diễn ra"

                val actionIntent = Intent(context, MediaPlayerReceiver::class.java).apply {
                    action = DONE
                }
                alarmUri?.let {
                    actionIntent.putExtra("AlarmUri", alarmUri)
                }
                entityType?.let {
                    actionIntent.putExtra("entityType", entityType)
                }
                actionIntent.putExtra("AlarmType", alarmType)
                actionIntent.putExtra("itemId", itemId)
                val actionPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    actionIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val dismissIntent = Intent(context, MediaPlayerReceiver::class.java).apply {
                    action = REJECT
                }
                alarmUri?.let {
                    dismissIntent.putExtra("AlarmUri", alarmUri)
                }
                entityType?.let {
                    dismissIntent.putExtra("entityType", entityType)
                }
                dismissIntent.putExtra("AlarmType", alarmType)
                dismissIntent.putExtra("itemId", itemId)
                val dismissPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    dismissIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val localStorage = LocalData(context, "sharedPreferences")
                localStorage.countPostNoti++
                val newIntent = Intent(context, MainActivity::class.java)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                val create = TaskStackBuilder.create(context)
                create.addNextIntentWithParentStack(newIntent)
                val pendingIntent = create.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val builder = NotificationCompat.Builder(context, Constants.ALARM_CHANNEL_NAME)
                val notification = builder
                    .setOngoing(true)
                    .setContentTitle(notificationTitle)
                    .setContentText(contentText)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .addAction(R.drawable.ic_launcher_foreground, actionContent, actionPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(Notification.CATEGORY_EVENT)
                if(alarmType == 1){
                    builder.addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.notification_dismisss_content), dismissPendingIntent)
                }
                try {
                    manager.notify(Constants.NOTIFICATION_ID, notification.build())
                } catch (e: Exception) {
                    localStorage.errorNoti = e.message.toString()
                }
                if(alarmType == 2){
                    MusicControl.getInstance(context).playMusic(alarmUri)
                }
            }
        }
    }
}

const val DONE = "DONE"
const val REJECT = "REJECT"
