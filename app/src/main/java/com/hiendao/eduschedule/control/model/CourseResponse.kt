package com.hiendao.eduschedule.control.model

import com.google.gson.annotations.SerializedName
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.squareup.moshi.Json
import retrofit2.http.Field

data class CoursesResponse(
    @SerializedName("courses")
    val courses: List<Course>?,

    val errorCode: String?,
    val errorMessage: String?
)

data class CourseDetailResponse (
    @SerializedName("course")
    val course: CourseDetail?,

    val errorCode: String?,
    val errorMessage: String?
)

data class DeleteCourseResponse (
    val statusCode: String?,
    val statusMsg: String?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)

data class CreateCourseResponse (
    @field:Json(name ="course")
    val course: CourseDetail?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)

data class UpdateCourseResponse (
    @field:Json(name ="course")
    val course: CourseDetail?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)
