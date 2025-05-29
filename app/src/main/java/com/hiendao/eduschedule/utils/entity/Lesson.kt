package com.hiendao.eduschedule.utils.entity

data class LessonModel(
    val id: Int = 0,
    val name : String = "",
    val note : String = "",
    val startTime: String = "",
    val endTime: String = "",
    val teacher: String = "",
    val address: String = "",
    val state: String = "",
)