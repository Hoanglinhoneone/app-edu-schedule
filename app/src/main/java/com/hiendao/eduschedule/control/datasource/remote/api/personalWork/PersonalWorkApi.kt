package com.hiendao.eduschedule.control.datasource.remote.api.personalWork

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PersonalWorkApi {
    @GET("api/personalwork/getByUser")
    suspend fun getAllPersonalWork(): PersonalWorkResponse

    @PUT("api/personalwork/update")
    suspend fun updatePersonalWork(
        @Query("id") id: Int,
        @Body personalWork: PersonalWorkBody
    ): PersonalWorkResponse

    @POST("api/personalwork/create")
    suspend fun createPersonalWork(
        @Body personalWork: PersonalWorkBody
    ): PersonalWorkResponse
}