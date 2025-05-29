 package com.hiendao.eduschedule.utils.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class Course(

   @field:Json(name = "id")
    val id: Int = 0,

   @field:Json(name = "name")
    val name: String = "Công nghệ phần mềm",

   @field:Json(name = "teacher")
    val teacher: String = "Nguyễn Hoàng Anh",

    @field:Json(name = "timeStart")
    val startTime: String = "",

    @field:Json(name = "timeEnd")
    val endTime: String = "",

    @field:Json(name = "numberOfLessons")
    val numberOfLesson: Int = 0,

   @field:Json(name = "numberOfAssignment")
    val numberOfAssignment: Int? = 0,

   @field:Json(name = "credits")
    val credits: String = "3",

   @field:Json(name = "startDay")
    val startDay: String = "",

   @field:Json(name = "endDay")
    val endDay: String = "",

   @field:Json(name = "state")
   val state: String = "",

   @field:Json(name = "repeat")
    val repeat: String = Repeat.Weekly.name,

   @field:Json(name = "location")
    val location: String = "",

   @field:Json(name = "note")
    val note: String = "",
)

data class CourseDetail(

   @field:Json(name = "id")
    val id: Int = 0,

   @field:Json(name = "name")
    val name: String = "Công nghệ phần mềm",

   @field:Json(name = "teacher")
    val teacher: String = "Nguyễn Hoàng Anh",

   @field:Json(name = "timeStart")
    val startDay: String = "",

   @field:Json(name = "timeEnd")
    val endDay: String = "",

   @field:Json(name = "numberOfLessons")
    val numberOfLesson: String? = "",

   @field:Json(name = "numberOfAssignments")
    val numberOfAssignment: String = "",

   @field:Json(name = "credits")
    val credits: String = "3",

   @field:Json(name = "addressLearning")
    val location: String = "",

   @field:Json(name = "repeatType")
    val repeatType: String = "",

   @field:Json(name = "listDay")
    val days: String = "",

   @field:Json(name = "state")
    val state: String = "",

   @field:Json(name = "note")
    val note: String = "",

   @field:Json(name = "scheduleLearningList")
    val lessons: List<Lesson> = emptyList(),

   @field:Json(name = "assignmentList")
    val assignments: List<Assignment> = emptyList(),

    )

data class CourseModel(
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

@Parcelize
data class Lesson(
   @field:Json(name = "id")
    val id: Int = 0,

   @field:Json(name = "name")
    val name: String = "",

    val date: String = "",

   @field:Json(name = "timeStart")
    val startTime: String = "",

   @field:Json(name = "timeEnd")
    val endTime: String = "",

   @field:Json(name = "teacher")
    val teacher: String = "",

   @field:Json(name = "learningAddresses")
    val location: String = "",

   @field:Json(name = "state")
    val state: String = StateLesson.NotYet.name,

   @field:Json(name = "note")
    val note: String = "",

   @field:Json(name = "courseID")
    val courseId: Int = 0,
) : Parcelable

data class Assignment(
   @field:Json(name = "id")
    val id: Int = 0,

   @field:Json(name = "name")
    val name: String = "Vẽ ui figma android app",

   @field:Json(name = "timeEnd")
    val endTime: String = "03/03/2025 14:00",

   @field:Json(name = "state")
    val state: String = StateAssignment.Complete.name,

   @field:Json(name = "note")
    val note: String = "Vẽ ui android app",

   @field:Json(name = "courseId")
    val courseId: Int = 0,
)

data class AssignmentModel(
    val id: Int = 0,
    val name: String = "",
    val endTime: String = "",
    val state: String = "",
    val note: String = "",
    val courseId: Int = 0,
)