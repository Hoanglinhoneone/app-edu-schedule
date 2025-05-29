package com.hiendao.eduschedule.control.datasource.remote.api.notification

import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications/")
    suspend fun getAllNotifications(): NotificationResponse
}