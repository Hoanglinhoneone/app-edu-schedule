package com.hiendao.eduschedule.utils.entity

data class ScheduleDTO(
    val id: Int,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val teacher: String,
    val learningAddresses: String,
    val state: String,
    val note: String,
    val courseID: Int?
)
