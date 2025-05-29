package com.hiendao.eduschedule.control.datasource.remote.api.personalWork

data class PersonalWorkBody(
    val name: String,
    val description: String,
    val timeStart: String,
    val timeEnd: String,
    val workAddress: String,
    val repeatCycle: String?
)
