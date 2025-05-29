package com.hiendao.eduschedule.ui.screen.notification

data class NotificationUiState(
    val notificationList: List<NotificationItem> = emptyList(),
    val filteredNotificationList: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
