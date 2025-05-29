package com.hiendao.eduschedule.control.datasource.remote.api

import com.hiendao.eduschedule.control.model.CourseStatsResponse
import com.hiendao.eduschedule.control.model.body.CourseStatsBody
import retrofit2.http.Body
import retrofit2.http.POST

interface StatisticalApi {
    @POST("api/performance/getAllPerformance")
    suspend fun getCourseStats(@Body courseStatsBody: CourseStatsBody): CourseStatsResponse
}