package com.hiendao.eduschedule.control.datasource.remote.api

import com.hiendao.eduschedule.control.model.UpdateUserResponse
import com.hiendao.eduschedule.utils.entity.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface AllApi {
    @GET("api/users/me")
    suspend fun getAllUserInfo(): UserResponse

    @PUT("api/users/update")
    suspend fun updateUserInfo(@Body user: User) : UpdateUserResponse

    @PUT("api/users/update/eventState")
    suspend fun updateEventState(
        @Query("eventId") eventId: Int,
        @Query("type") type: String,
        @Query("state") state: String
    ): UpdateEventResponse
}