package com.hiendao.eduschedule.ui.screen.home_schedule

import com.hiendao.eduschedule.control.datasource.remote.api.PersonalWork
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.ScheduleLearning

enum class ScheduleType {
    ASSIGNMENT,
    PERSONAL_WORK,
    LESSON
}

data class Event(
    val id: Int,
    val name: String,
    val timeStart: String? = null,
    val timeEnd: String ?= null,
    val state: String? = null,
    val note: String? = null,
    val courseId: Int? = null,
    val description: String? = null,
    val repeatCycle: String? = null,
    val createAt: String? = null,
    val userId: Int? = null,
    val validTimeRange: Boolean? = null,
    val workAddress: String? = null,
    val learningAddresses: String? = null,
    val teacher: String? = null,
    val date: Long = 0L,
    val endDate: Long = 0L,
    val type: ScheduleType
)


fun Assignment?.toEvent(): Event {
    return Event(
        id = this?.id ?: 0,
        name = this?.name ?: "",
        timeEnd = this?.endTime ?: "",
        state = this?.state ?: "",
        note = this?.note ?: "",
        courseId = this?.courseId ?: 0,
        date = this?.endTime?.convertToTimeInMillis() ?: 0L,
        type = ScheduleType.ASSIGNMENT
    )
}

fun PersonalWork?.toEvent(): Event {
    return Event(
        id = this?.id ?: 0,
        name = this?.name ?: "",
        timeStart = this?.timeStart ?: "",
        timeEnd = this?.timeEnd ?: "",
        description = this?.description ?: "",
        repeatCycle = this?.repeatCycle ?: "",
        createAt = this?.createAt ?: "",
        userId = this?.userId ?: 0,
        validTimeRange = this?.validTimeRange ?: false,
        workAddress = this?.workAddress ?: "",
        date = this?.timeStart?.convertToTimeInMillis() ?: 0L,
        endDate = this?.timeEnd?.convertToTimeInMillis() ?: 0L,
        type = ScheduleType.PERSONAL_WORK
    )
}

fun ScheduleLearning?.toEvent(): Event {
    return Event(
        id = this?.id ?: 0,
        name = this?.name ?: "",
        timeStart = this?.timeStart ?: "",
        timeEnd = this?.timeEnd ?: "",
        note = this?.note ?: "",
        state = this?.state ?: "",
        courseId = this?.courseId ?: 0,
        learningAddresses = this?.learningAddresses ?: "",
        teacher = this?.teacher ?: "",
        date = this?.timeStart?.convertToTimeInMillis() ?: 0L,
        endDate = this?.timeEnd?.convertToTimeInMillis() ?: 0L,
        type = ScheduleType.LESSON
    )
}

