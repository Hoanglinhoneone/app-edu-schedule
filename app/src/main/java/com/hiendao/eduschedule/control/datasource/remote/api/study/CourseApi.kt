package com.hiendao.eduschedule.control.datasource.remote.api.study

import com.hiendao.eduschedule.control.model.CourseDetailResponse
import com.hiendao.eduschedule.control.model.CoursesResponse
import com.hiendao.eduschedule.control.model.CreateCourseResponse
import com.hiendao.eduschedule.control.model.DeleteCourseResponse
import com.hiendao.eduschedule.control.model.UpdateCourseResponse
import com.hiendao.eduschedule.control.model.body.CourseBody
import com.hiendao.eduschedule.utils.entity.CourseDetail
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CourseApi {

    @GET("api/courses/")
    suspend fun getCourses(): CoursesResponse

    @GET("api/courses/fetch")
    suspend fun getCourseById(  @Query("courseId") courseId: Int) : CourseDetailResponse

    @DELETE("api/courses/delete")
    suspend fun deleteCourseById(@Query("courseId") courseId: Int) : DeleteCourseResponse

    @POST("api/courses/create")
    suspend fun createCourse(@Body courseBody: CourseBody) : CreateCourseResponse

    @PUT("api/courses/update")
    suspend fun updateCourseById(@Query("courseId") courseId: Int, @Body courseBody: CourseBody) : UpdateCourseResponse

}