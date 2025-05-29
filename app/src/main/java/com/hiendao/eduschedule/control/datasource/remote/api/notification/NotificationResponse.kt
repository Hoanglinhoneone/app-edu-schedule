package com.hiendao.eduschedule.control.datasource.remote.api.notification

data class NotificationResponse(
    val notification: NotificationDTO,
    val notifications: List<NotificationDTO>
)