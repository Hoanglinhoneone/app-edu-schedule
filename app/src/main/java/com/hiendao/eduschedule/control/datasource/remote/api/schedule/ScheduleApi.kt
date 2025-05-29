package com.hiendao.eduschedule.control.datasource.remote.api.schedule

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/scheduleLearnings")
    suspend fun getAllSchedule(): ScheduleResponse

    @POST("api/scheduleLearnings/create")
    suspend fun createSchedule(
        @Body scheduleBody: CreateScheduleBody
    ): ScheduleResponse

    @GET("api/scheduleLearnings/user/time")
    suspend fun getScheduleByTime(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): ScheduleResponse

    @POST("api/scheduleLearnings/update")
    suspend fun updateSchedule(
        @Query("id") id: Int,
        @Body scheduleBody: CreateScheduleBody
    ): ScheduleResponse

    @GET("api/scheduleLearnings/course")
    suspend fun getScheduleByCourse(
        @Query("courseId") courseId: Int
    ): ScheduleResponse

    @DELETE("api/scheduleLearnings/delete")
    suspend fun deleteSchedule(
        @Query("id") id: Int
    ): ScheduleResponse
}