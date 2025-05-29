package com.hiendao.eduschedule.control.datasource.remote.api.alarm

data class AlarmResponse(
    val alarms: List<AlarmDTO>,
    val message: String,
    val status: Int,
    val alarm: AlarmDTO? = null,
)
