package com.hiendao.eduschedule.ui.screen.add

import com.hiendao.eduschedule.utils.entity.Course


sealed interface EventAction {
    data class OnChangeCourses(val courses: Course): EventAction
    data class OnChangeCredits(val credits: String): EventAction
    data class OnChangeTeacher(val teacher: String): EventAction
    data class OnChangeLocation(val location: String): EventAction
    data class OnChangeStartDay(val startDay: String): EventAction
    data class OnChangeEndDay(val endDay: String): EventAction
    data class OnChangeStartTime(val startTime: String): EventAction
    data class OnChangeEndTime(val endTime: String): EventAction
    data class OnChangeNote(val note: String): EventAction
    data class OnChangeErrorEmpty(val errorEmpty: Boolean): EventAction
}