package com.hiendao.eduschedule.ui.screen.home_schedule

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val errorMsg: String ?= null,
    var listSchedule: MutableList<Event> = mutableListOf(),
    var listTimeDate: MutableList<Long> = mutableListOf(),
    var currentTimeList: MutableList<DateItem>  = mutableListOf(),
    var selectedTime: Long = 0L,
    var currentListSchedules: MutableList<Event> = mutableListOf(),
    var currentMonthAndYear: String = "",
    var filterEvent: MutableList<Event> = mutableListOf(),
    var currentEvents: MutableList<Event> = mutableListOf(),
)
