package com.hiendao.eduschedule.control.datasource.remote.api.notification

import com.hiendao.eduschedule.ui.screen.notification.NotificationItem

data class NotificationDTO(
    val category: String,
    val content: String,
    val entityId: Int,
    val eventTime: String,
    val id: Int,
    val name: String,
    val readTime: Any,
    val state: String,
    val timeNoti: String
)

fun NotificationDTO.toNotificationItem(): NotificationItem{
    return NotificationItem(
        id = id,
        name = name,
        content = content,
        timeNoti = timeNoti,
        isRead = state == "READ",
        notiType = category
    )
}