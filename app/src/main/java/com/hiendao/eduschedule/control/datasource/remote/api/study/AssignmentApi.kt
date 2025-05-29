package com.hiendao.eduschedule.control.datasource.remote.api.study

import com.hiendao.eduschedule.control.model.AssignmentResponse
import com.hiendao.eduschedule.control.model.AssignmentsResponse
import com.hiendao.eduschedule.control.model.DeleteAssignmentResponse
import com.hiendao.eduschedule.control.model.body.CreateAssignmentBody
import com.hiendao.eduschedule.control.model.body.UpdateAssignmentBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AssignmentApi {
    @GET("api/assignments/all")
    suspend fun getAllAssignments(): AssignmentsResponse

    @GET("api/assignments/course")
    suspend fun getAssignments(@Query("courseID") courseId: Int): AssignmentsResponse

    @GET("api/assignments/fetch")
    suspend fun getAssignment(@Query("assignmentID") assignmentId: Int): AssignmentResponse

    @POST("api/assignments/create")
    suspend fun createAssignment(@Body assignment: CreateAssignmentBody): AssignmentResponse

    @PUT("api/assignments/update")
    suspend fun updateAssignment(@Query("id") assignmentId: Int, @Body assignment: UpdateAssignmentBody): AssignmentResponse

    @DELETE("api/assignments/delete")
    suspend fun deleteAssignment(@Query("id") assignmentId: Int): DeleteAssignmentResponse

}