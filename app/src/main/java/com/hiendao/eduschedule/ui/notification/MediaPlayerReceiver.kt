package com.hiendao.eduschedule.ui.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.hiendao.eduschedule.control.repository.remote.AlarmRepository
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepository
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleType
import com.hiendao.eduschedule.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerReceiver: BroadcastReceiver() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var scheduleRepository: ScheduleRepository


    override fun onReceive(context: Context, intent: Intent) {
        if (context == null) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val application = context.applicationContext

        val itemId = intent.getIntExtra("itemId", 0)
        val alarmType = intent.getIntExtra("AlarmType", 1)
        val entityType = intent.getStringExtra("entityType")
        when(intent.action) {
            DONE -> {
                MusicControl.getInstance(context).stopMusic()
                if (alarmType == 2) {
                    runBlocking {
                        println("Tắt alarm")
                    }
                } else if (alarmType == 1) {
                    // Update scheduleId to joined
                    runBlocking {
                        println("Tham gia")
                        entityType?.let {
                            val state =
                                when (entityType) {
                                    ScheduleType.LESSON.toString() -> Constants.ScheduleLearningState.PRESENT
                                    ScheduleType.ASSIGNMENT.toString() -> Constants.AssignmentPersonalState.COMPLETE
                                    else -> Constants.AssignmentPersonalState.COMPLETE
                                }
                            scheduleRepository.updateStateEvent(itemId, state, entityType)
                        }
                    }
                }
                val notificationManager =
                    application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(Constants.NOTIFICATION_ID)
            }

            REJECT -> {
                // Update scheduleId to missed
                runBlocking {
                    println("Vắng mặt")
                }
            }
        }
    }
}