package com.hiendao.eduschedule.control.datasource.remote.api.schedule

import com.hiendao.eduschedule.utils.entity.ScheduleDTO


data class ScheduleResponse(
    val scheduleLearningList: List<ScheduleDTO>?,
    val scheduleLearning: ScheduleDTO ?= null,
    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)
