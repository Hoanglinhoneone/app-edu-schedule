package com.hiendao.eduschedule.control.model

data class UpdateUserResponse(
    val statusCode: String?,
    val statusMsg: String?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)