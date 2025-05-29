package com.hiendao.eduschedule.utils.entity

import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmDTO

data class User (
    val id: Int,
    val fullName: String,
    val email: String,
    val mobilePhone: String,
    val gender: String,
    val dateOfBirth: String,
    val listAlarm: List<AlarmDTO> ?= null,
    val listCourse: List<Course> ?= null
)