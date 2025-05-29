package com.hiendao.eduschedule.control.datasource.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hiendao.eduschedule.control.model.CourseDetailResponse
import com.hiendao.eduschedule.control.model.CoursesResponse
import com.hiendao.eduschedule.control.model.DeleteCourseResponse
import com.hiendao.eduschedule.control.model.LessonResponse
import com.hiendao.eduschedule.utils.entity.Course
import timber.log.Timber
import java.io.InputStreamReader
import javax.inject.Inject

class JsonDataSource @Inject constructor(val context: Context) {

    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val fileCourse = "courses.json"
    private val fileCourseDetail = "course_detail.json"
    private val fileDeleteCourse = "delete_course.json"
    private val fileLessonDetail = "lesson_detail.json"

    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun getCourse(): CoursesResponse {
        Timber.i("get courses")
        val json = readJsonFromAssets(fileCourse)
        return parseJsonToCourseList(json)
    }

    fun getCourseById(courseId: Int): CourseDetailResponse {
        Timber.i("get course_detail")
        val json = readJsonFromAssets(fileCourseDetail)
        return parseJsonToCourse(json)
    }
    fun deleteCourseById(courseId: Int): DeleteCourseResponse {
        Timber.i("delete course_detail")
        val json = readJsonFromAssets(fileDeleteCourse)
        return parseJsonToMessage(json)
    }

    fun getLessonById(lessonId: Int): LessonResponse {
        Timber.i("get lesson_detail")
        val json = readJsonFromAssets(fileLessonDetail)
        return parseJsonToLesson(json)
    }

    private fun parseJsonToLesson(json: String) : LessonResponse {
        Timber.i("parsing json")
        val listType = object : TypeToken<LessonResponse>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun parseJsonToMessage(json: String): DeleteCourseResponse {
        Timber.i("parsing json")
        val listType = object : TypeToken<DeleteCourseResponse>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun parseJsonToCourse(json: String): CourseDetailResponse {
        Timber.i(" parsing json")
        val listType = object : TypeToken<CourseDetailResponse>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun parseJsonToCourseList(json: String): CoursesResponse {
        Timber.i(" parsing json")
        val listType = object : TypeToken<CoursesResponse>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun readJsonFromAssets(fileName: String): String {
        Timber.i(" reading file json")
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = InputStreamReader(inputStream)
        return reader.readText()
    }
}