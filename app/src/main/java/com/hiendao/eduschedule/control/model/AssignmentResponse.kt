package com.hiendao.eduschedule.control.model

import com.hiendao.eduschedule.utils.entity.Assignment

data class AssignmentsResponse (
    val assignments: List<Assignment>?,
    val assignment: Assignment?,
    val errorCode: String?,
    val errorMessage: String?
)

data class AssignmentResponse (
    val assignment: Assignment?,
    val errorCode: String?,
    val errorMessage: String?
)

data class DeleteAssignmentResponse (

    val statusCode: String?,
    val statusMsg: String?,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)