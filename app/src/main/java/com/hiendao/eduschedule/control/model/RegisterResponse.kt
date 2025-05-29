package com.hiendao.eduschedule.control.model

import com.squareup.moshi.Json

data class RegisterResponse(
    @field:Json(name = "username")
    val username: String?,
    val email: String,
    val fullName: String?,
    @field:Json(name = "mobilePhone")
    val phoneNumber: String?,
    val age: String?,
    val gender: String?,
    val dateOfBirth: String?,

    val errorCode: String?,
    val errorMessage: String?
)


