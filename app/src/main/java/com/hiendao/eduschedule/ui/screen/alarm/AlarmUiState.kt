package com.hiendao.eduschedule.ui.screen.alarm


data class AlarmUiState(
    val alarmList: List<AlarmItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String ?= null,
    val filterAlarmList: List<AlarmItem> = emptyList(),
    val selectedAlarm: AlarmItem? = null,
    val isAddAlarmSuccess: Boolean ?= null,
    val isUpdateSuccess: Boolean ?= null,
    val isDeleteSuccess: Boolean ?= null,
)
