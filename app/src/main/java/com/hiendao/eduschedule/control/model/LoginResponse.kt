package com.hiendao.eduschedule.control.model

data class LoginResponse(
    val token: String?,
    val errorCode: String?,
    val errorMessage: String?
)
