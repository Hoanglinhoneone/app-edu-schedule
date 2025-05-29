package com.hiendao.eduschedule.control.datasource.remote.api.alarm

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AlarmApi {
    @GET("api/alarms/")
    suspend fun getAllAlarms(): AlarmResponse

    @GET("api/alarms/fetch")
    suspend fun getAlarmById(
        @Query("id") id: Int): AlarmResponse

    @POST("api/alarms/create")
    suspend fun addAlarm(
        @Body alarmItem: AlarmBody
    ): AlarmResponse

    @PUT("api/alarms/update")
    suspend fun updateAlarm(
        @Query("alarmId") id: Int,
        @Body alarmItem: AlarmBody): AlarmResponse

    @DELETE("api/alarms/delete")
    suspend fun deleteAlarm(
        @Query("alarmId") id: Int): AlarmResponse
}