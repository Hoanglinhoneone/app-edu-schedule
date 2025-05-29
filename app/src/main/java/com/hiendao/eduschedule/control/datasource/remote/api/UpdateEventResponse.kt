package com.hiendao.eduschedule.control.datasource.remote.api

data class UpdateEventResponse(
    val statusCode: Int? = null,
    val statusMsg: String? = null,
    val errorMsg: String? = null,
    val apiPath: String? = null,
    val errorCode: Int? = null,
    val errorTime: String? = null
)
