package com.hiendao.eduschedule.ui.screen.study.assignment

sealed interface  OnAssignmentAction {
    data class OnChangeState(val assignmentId: Int) : OnAssignmentAction
//    data class OnClickAssignment(val assignmentId: Int) : OnAssignmentAction
}