package com.hiendao.eduschedule.utils.entity

data class DayOfWeek(
    val day: Day,
    val isSelected: Boolean = true
)

data class DayOfMonth(
    val day: Int,
    val isSelected: Boolean = false
)
