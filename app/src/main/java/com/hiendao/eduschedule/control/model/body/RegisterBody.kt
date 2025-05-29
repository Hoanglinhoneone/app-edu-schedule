package com.hiendao.eduschedule.control.model.body

data class RegisterBody(
    val fullName: String,
    val email: String,
    val mobilePhone: String,
    val password: String
)
