package com.hiendao.eduschedule.ui.screen.add

import com.hiendao.eduschedule.utils.entity.DayOfMonth
import com.hiendao.eduschedule.utils.entity.DayOfWeek
import com.hiendao.eduschedule.utils.entity.Repeat

sealed interface CourseAction {

    data class OnChangeCredits(val credits: String): CourseAction
    data class OnChangeTeacher(val teacher: String): CourseAction
    data class OnChangeLocation(val location: String): CourseAction
    data class OnChangeStartDay(val startDay: String): CourseAction
    data class OnChangeEndDay(val endDay: String): CourseAction
    data class OnChangeRepeatType(val repeatType: Repeat): CourseAction
    data class OnChangeDayOfWeek(val dayOfWeek: DayOfWeek): CourseAction
    data class OnChangeDayOfMonth(val dayOfMonth: DayOfMonth): CourseAction
    data class OnChangeStartTime(val startTime: String): CourseAction
    data class OnChangeEndTime(val endTime: String): CourseAction
    data class OnChangeNote(val note: String): CourseAction
    data class OnChangeErrorEmpty(val errorEmpty: Boolean): CourseAction

}