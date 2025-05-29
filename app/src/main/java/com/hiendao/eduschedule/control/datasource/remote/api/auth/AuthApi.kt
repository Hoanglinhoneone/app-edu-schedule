package com.hiendao.eduschedule.control.datasource.remote.api.auth

import com.hiendao.eduschedule.control.model.body.RegisterBody
import com.hiendao.eduschedule.control.model.body.LoginBody
import com.hiendao.eduschedule.control.model.body.VerifyUserBody
import com.hiendao.eduschedule.control.model.SendEmailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import com.hiendao.eduschedule.control.model.LoginResponse
import com.hiendao.eduschedule.control.model.RegisterResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): LoginResponse

    @POST("auth/signup")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): RegisterResponse

    @POST("auth/sendEmail")
    suspend fun sendEmail(
        @Query("email") email: String,
        @Query("event") event: String
//        @Body verifyUserBody: VerifyUserBody
    ): SendEmailResponse

    @POST("auth/loginWithGoogle")
    suspend fun loginWithGoogle(
        @Query("username") fullName: String,
        @Query("email") email: String,
        @Query("mobilePhone") phoneNumber: String
    ): LoginResponse

    @POST("auth/updatePass")
    suspend fun updatePassword(
        @Query("email") email: String,
        @Query("password") password: String
    ): LoginResponse

    @GET("auth/get_otp")
    suspend fun getOtpCode(
        @Query("email") email: String
    ): LoginResponse
}