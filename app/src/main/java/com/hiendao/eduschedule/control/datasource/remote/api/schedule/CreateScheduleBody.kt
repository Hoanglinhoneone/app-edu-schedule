package com.hiendao.eduschedule.control.datasource.remote.api.schedule

data class CreateScheduleBody(
    val name: String,
    val note: String,
    val timeStart: String,
    val timeEnd: String,
    val teacher: String,
    val learningAddresses: String,
    val state: String = "ABSENT"
)
