package com.hiendao.eduschedule.ui.screen.add

sealed interface AssignmentAction {
    data class OnChangeCourseId(val courseId: Int) : AssignmentAction
    data class OnChangeEndDay(val endDay: String) : AssignmentAction
    data class OnChangeEndTime(val endTime: String) : AssignmentAction
    data class OnChangeNote(val note: String) : AssignmentAction
}