package com.hiendao.eduschedule.ui.screen.home_schedule

data class ScheduleItem(
    val timeStart: String,
    val timeEnd: String,
    val name: String,
    val learningAddresses: String,
    val note: String = "",
    val teacher: String = "",
    val courseId: Int = 0,
    val state: String = "ABSENT"
)