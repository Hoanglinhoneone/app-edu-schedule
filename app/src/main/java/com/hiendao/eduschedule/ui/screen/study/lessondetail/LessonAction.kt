package com.hiendao.eduschedule.ui.screen.study.lessondetail

import com.hiendao.eduschedule.utils.entity.StateLesson

sealed interface  LessonAction {
    data class OnChangeName(val name: String) : LessonAction
    data class OnChangeStartDay(val startDay: String) : LessonAction
    data class OnChangeEndDay(val endDay: String) : LessonAction
    data class OnChangeStartTime(val startTime: String) : LessonAction
    data class OnChangeEndTime(val endTime: String) : LessonAction
    data class OnChangeStateLesson(val stateLesson: StateLesson) : LessonAction
    data class OnChangeLocation(val location: String) : LessonAction
    data class OnChangeNote(val note: String) : LessonAction
    data object OnDelete : LessonAction
    data object OnFix : LessonAction
    data object OnResetState : LessonAction
}