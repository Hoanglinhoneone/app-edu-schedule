package com.hiendao.eduschedule.ui.screen.study.courseinfo

sealed interface CourseInfoAction {
    data class OnChangeName(val name: String) : CourseInfoAction
    data object OnUpdate : CourseInfoAction
    data object OnDelete : CourseInfoAction
    data object OnResetState : CourseInfoAction
}