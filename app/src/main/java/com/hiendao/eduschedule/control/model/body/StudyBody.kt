package com.hiendao.eduschedule.control.model.body

data class CourseBody (
    val name: String = "",
    val note: String = "",
    val credits: String = "",
    val teacher: String = "",
    val timeStart: String = "1/1/2025",
    val timeEnd: String = "1/1/2026",
    val addressLearning: String = "",
    val repeatType: String = "",
    val startLessonTime: String = "13:00",
    val endLessonTime: String = "15:00",
    val listDay: String = ""
)

data class DeleteCourseResponse(
    val statusCode: String = ""
)

data class UpdateCourseBody(
    val name: String = "",
    val note: String = "",
    val credits: String = "",
    val teacher: String = "",
    val timeStart: String = "1/1/2025",
    val timeEnd: String = "1/1/2026",
    val addressLearning: String = "",
    val repeatType: String = "",
    val listDay: String = ""
)

data class UpdateLessonBody(
    val name: String = "",
    val note: String = "",
    val timeStart: String = "",
    val timeEnd: String = "",
    val teacher: String = "",
    val learningAddresses: String = "",
    val state: String = ""
)

data class CreateAssignmentBody(
    val name: String = "",
    val timeEnd: String = "",
    val note: String = "",
    val courseId: Int = 0
)

data class UpdateAssignmentBody(
    val name: String = "",
    val note: String = "",
    val timeEnd: String = "",
    val state: String? = "",
    val courseId: Int = 0
)