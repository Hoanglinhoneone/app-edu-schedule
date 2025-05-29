package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.ui.screen.notification.NotificationItem
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getAllNotification(): Flow<Resource<List<NotificationItem>>>
}