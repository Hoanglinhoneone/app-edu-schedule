package com.hiendao.eduschedule.control.model

import com.hiendao.eduschedule.utils.entity.Lesson
import com.squareup.moshi.Json

data class LessonsResponse(
    val lessons: List<Lesson>?,

    val errorCode: String?,
    val errorMessage: String?
)

data class LessonResponse(
    @field:Json(name = "scheduleLearning")
    val lesson: Lesson?,

    val errorCode: String?,
    val errorMessage: String?
)

data class DeleteLessonResponse (
    val statusCode: String?,
    val statusMsg: String?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)



