package com.hiendao.eduschedule.control.datasource.remote.api.study

import com.hiendao.eduschedule.control.model.DeleteLessonResponse
import com.hiendao.eduschedule.control.model.LessonResponse
import com.hiendao.eduschedule.control.model.body.UpdateLessonBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface LessonApi {
    @GET("api/lessons/")
    suspend fun getLessons(): LessonResponse

    @GET("api/lessons/fetch")
    suspend fun getLessonById(@Query("lessonId") lessonId: Int): LessonResponse

    @GET("api/lessons/course")
    suspend fun getLessonsByCourseId(@Query("courseId") courseId: Int): LessonResponse

    @DELETE("api/scheduleLearnings/delete")
    suspend fun deleteLesson(@Query("id") lessonId: Int): DeleteLessonResponse

    @PUT("api/scheduleLearnings/update")
    suspend fun updateLesson(@Query("id") lessonId: Int, @Body lesson: UpdateLessonBody): LessonResponse

}