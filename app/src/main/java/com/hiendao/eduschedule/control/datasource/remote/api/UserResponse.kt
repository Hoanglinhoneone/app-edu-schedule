package com.hiendao.eduschedule.control.datasource.remote.api

import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmDTO
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.ScheduleLearning

data class UserResponse(
    val age: String?,
    val alarmList: List<AlarmDTO>,
    val assignmentList: List<Assignment>,
    val courseList: List<Course>,
    val dateOfBirth: String?,
    val email: String?,
    val fullName: String?,
    val gender: String?,
    val id: Int,
    val mobilePhone: String?,
    val personalWorkList: List<PersonalWork>,
    val scheduleLearningList: List<ScheduleLearning>
)