package com.hiendao.eduschedule.control.repository.remote

import android.content.Context
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmApi
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmDTO
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.toAlarmBody
import com.hiendao.eduschedule.control.datasource.remote.api.convert
import com.hiendao.eduschedule.ui.notification.OfflineNotification
import com.hiendao.eduschedule.ui.screen.alarm.AlarmItem
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.toAlarmDTO
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.toAlarmItem
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AlarmRepositoryImp @Inject constructor(
    private val alarmApi: AlarmApi
): AlarmRepository {
    override suspend fun getAllAlarms(): Flow<Resource<List<AlarmItem>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = alarmApi.getAllAlarms()
                if (result.alarms.isNotEmpty()) {
                    emit(Resource.Success(result.alarms.map { it.toAlarmItem() }))
                } else {
                    emit(Resource.Error(Constants.HttpErrorMessage.EMPTY))
                }
            } catch (e: HttpException) {
                val errorConverter = e.convert()
                val errorMsg = if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    errorConverter.message
                } else {
                    println(e.message)
                    Constants.HttpErrorMessage.UNKNOWN
                }
                emit(Resource.Error(errorMsg))
            } catch (e: Exception) {
                println(e.message)
                emit(Resource.Error(Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun getAlarmById(id: Int): Flow<Resource<AlarmItem>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = alarmApi.getAlarmById(id)
                if (result.alarm != null) {
                    emit(Resource.Success(result.alarm.toAlarmItem()))
                } else {
                    emit(Resource.Error(Constants.HttpErrorMessage.EMPTY))
                }
            } catch (e: HttpException) {
                val errorConverter = e.convert()
                val errorMsg = if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    errorConverter.message
                } else {
                    println(e.message)
                    Constants.HttpErrorMessage.UNKNOWN
                }
                emit(Resource.Error(errorMsg))
            } catch (e: Exception) {
                println(e.message)
                emit(Resource.Error(Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun addAlarm(alarmItem: AlarmItem): Flow<Resource<AlarmDTO>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = alarmApi.addAlarm(alarmItem.toAlarmDTO().toAlarmBody())
                if(result.alarm != null) {
                    emit(Resource.Success(result.alarm))
                } else {
                    emit(Resource.Error(Constants.HttpErrorMessage.EMPTY))
                }
            } catch (e: HttpException) {
                val errorConverter = e.convert()
                val errorMsg = if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    errorConverter.message
                } else {
                    println(e.message)
                    Constants.HttpErrorMessage.UNKNOWN
                }
                emit(Resource.Error(errorMsg))
            } catch (e: Exception) {
                println(e.message)
                emit(Resource.Error(Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun updateAlarm(alarmItem: AlarmItem): Flow<Resource<AlarmDTO>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = alarmApi.updateAlarm(alarmItem.id, alarmItem.toAlarmDTO().toAlarmBody())
                if(result.alarm != null) {
                    emit(Resource.Success(result.alarm))
                } else {
                    emit(Resource.Error(Constants.HttpErrorMessage.EMPTY))
                }            } catch (e: HttpException) {
                val errorConverter = e.convert()
                val errorMsg = if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    errorConverter.message
                } else {
                    println(e.message)
                    Constants.HttpErrorMessage.UNKNOWN
                }
                emit(Resource.Error(errorMsg))
            } catch (e: Exception) {
                println(e.message)
                emit(Resource.Error(Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun deleteAlarm(id: Int): Flow<Resource<AlarmDTO>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = alarmApi.deleteAlarm(id)
                if(result.alarm != null) {
                    emit(Resource.Success(result.alarm))
                } else {
                    emit(Resource.Error(Constants.HttpErrorMessage.EMPTY))
                }            } catch (e: HttpException) {
                val errorConverter = e.convert()
                val errorMsg = if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    errorConverter.message
                } else {
                    println(e.message)
                    Constants.HttpErrorMessage.UNKNOWN
                }
                emit(Resource.Error(errorMsg))
            } catch (e: Exception) {
                println(e.message)
                emit(Resource.Error(Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override fun enableAlarm(
        alarmItem: AlarmItem,
        isActive: Boolean,
        context: Context
    ) {
        val message = when(alarmItem.alarmType) {
            1 -> "Bạn có sự kiện sắp diễn ra vào lúc ${alarmItem.timeAlarm}"
            2 -> "Bạn có một khóa học sắp diễn ra vào lúc ${alarmItem.timeAlarm}"
            3 -> "Bạn có một bài tập sắp đến hạn vào lúc ${alarmItem.timeAlarm}"
            else -> "Bạn có báo thức vào lúc ${alarmItem.timeAlarm}"
        }
        val offlineNoti = OfflineNotification(context)
        offlineNoti.setAlarm(
            alarmItem.id,
            alarmItem.timeInMillis,
            alarmItem.soundUri,
            alarmItem.name,
            message,
            ""
        )
    }
}