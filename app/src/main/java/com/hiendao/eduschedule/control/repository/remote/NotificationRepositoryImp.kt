package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.notification.NotificationApi
import com.hiendao.eduschedule.control.datasource.remote.api.notification.toNotificationItem
import com.hiendao.eduschedule.ui.screen.notification.NotificationItem
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val notificationApi: NotificationApi
): NotificationRepository {
    override suspend fun getAllNotification(): Flow<Resource<List<NotificationItem>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = notificationApi.getAllNotifications()
                if (response.notifications.isNotEmpty()) {
                    emit(Resource.Success(response.notifications.map { it.toNotificationItem() }))
                } else {
                    emit(Resource.Error("No notifications found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }
}