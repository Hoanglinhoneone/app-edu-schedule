package com.hiendao.eduschedule.control.repository.remote

import android.content.Context
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmDTO
import com.hiendao.eduschedule.ui.screen.alarm.AlarmItem
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAllAlarms(): Flow<Resource<List<AlarmItem>>>
    suspend fun getAlarmById(id: Int): Flow<Resource<AlarmItem>>
    suspend fun addAlarm(alarmItem: AlarmItem): Flow<Resource<AlarmDTO>>
    suspend fun updateAlarm(alarmItem: AlarmItem): Flow<Resource<AlarmDTO>>
    suspend fun deleteAlarm(id: Int): Flow<Resource<AlarmDTO>>
    fun enableAlarm(alarmItem: AlarmItem, isActive: Boolean, context: Context)
}