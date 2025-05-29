package com.hiendao.eduschedule.control.model

import com.squareup.moshi.Json

data class SendEmailResponse(
    val errorCode: String?,
    val errorMessage: String?,
    @field:Json(name = "verificationCode")
    val otpCode: String?
)
