package com.hiendao.eduschedule.ui.screen.study.assigndetail

sealed interface AssignmentDetailAction {
    data class OnChangeTitle(val title: String) : AssignmentDetailAction
    data class OnChangeNote(val note: String) : AssignmentDetailAction
    data class OnChangeEndDay(val endDay: String) : AssignmentDetailAction
    data class OnChangeEndTime(val endTime: String) : AssignmentDetailAction
    data object OnChangeState : AssignmentDetailAction
    data object OnFix : AssignmentDetailAction
    data object OnDelete : AssignmentDetailAction
    data object OnResetState : AssignmentDetailAction
}