package com.hiendao.eduschedule.utils.entity

import com.squareup.moshi.Json

data class CourseStats (
    val timeStart: String = "",
    val timeEnd: String = "",
    val courseStatsInformation: List<CourseStatsInformation> = emptyList(),
    val present: Int = 0,
    val absent: Int = 0
)

data class CourseStatsInformation(
    val id: Int = 0,
    val name: String = "",
    val teacher: String = "",
    val timeStart: String = "",
    val timeEnd: String = "",
    val addressLearning: String = "",
    val state: String = "",

    @field:Json(name = "review_sche")
    val reviewSchedule: String = "",

    @field:Json(name = "review_ass")
    val reviewAssignment: String = "",

    @field:Json(name = "percent_review_all")
    val percentReviewAll: Int = 0,

    @field:Json(name = "text_review")
    val textReview: String = "",

    val totalScheduleLearning: Int = 0,

    @field:Json(name = "scheduleLearning_present")
    val scheduleLearningPresent: Int = 0,

    @field:Json(name = "scheduleLearning_absent")
    val scheduleLearningAbsent: Int = 0,

    val totalAssignment: Int = 0,

    val totalAssignmentCurrent: Int = 0,

    @field:Json(name = "assignment_overdue")
    val assignmentOverdue: Int = 0,

//    @field:Json(name = "numberOfLesion")
//    val numberOfLesson: Int? = 0,
//    val repeatTime: String? = "",
//    val totalScheduleLearningCurrent: Int? = 0,

    val scheduleLearningList: List<ScheduleLearning> = emptyList()
)

data class ScheduleLearning(
    val id: Int,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val teacher: String,
    val learningAddresses: String,
    val state: String,
    val note: String,

    @field:Json(name = "courseID")
    val courseId: Int?
)
