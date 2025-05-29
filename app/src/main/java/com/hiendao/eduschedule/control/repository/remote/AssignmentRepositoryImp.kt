package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.study.AssignmentApi
import com.hiendao.eduschedule.control.model.body.CreateAssignmentBody
import com.hiendao.eduschedule.control.model.body.UpdateAssignmentBody
import com.hiendao.eduschedule.ui.screen.study.assigndetail.AssignmentDetailState
import com.hiendao.eduschedule.ui.screen.study.assignment.AssignmentState
import com.hiendao.eduschedule.ui.screen.study.assignment.AssignmentsState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.utils.entity.AssignmentModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AssignmentRepositoryImp @Inject constructor(
    private val assignmentApi: AssignmentApi
) : AssignmentRepository {
    override suspend fun getAllAssignments(): Flow<AssignmentsState> = flow {

    }

    override suspend fun getAssignments(courseId: Int): Flow<AssignmentsState> = flow {
        Timber.i("get assignments by course id: $courseId")
        emit(AssignmentsState.Loading)
        try {
            val response = assignmentApi.getAssignments(courseId)
            if (response.assignments?.isNotEmpty() == true) {
                Timber.i("get assignments by courseId success")
                emit(AssignmentsState.Success(response.assignments))
            } else {
                Timber.i("get assignments by courseId error: ${response.errorMessage}")
                emit(AssignmentsState.Error(response.errorMessage ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Timber.i("get assignments by courseId error: ${e.message}")
        }
    }

    override suspend fun getAssignment(assignmentId: Int): Flow<AssignmentDetailState> = flow {
        Timber.i("get assignment by assignmentId $assignmentId")
        emit(AssignmentDetailState.Loading)
        try {
            val response = assignmentApi.getAssignment(assignmentId)
            if (response.assignment != null) {
                Timber.i("get assignment by assignmentId success: ${response.assignment}")
                emit(AssignmentDetailState.Success(response.assignment))
            } else {
                Timber.i("get assignment by assignmentId error: ${response.errorMessage}")
                emit(AssignmentDetailState.Error(response.errorMessage ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Timber.i("get assignment by assignmentId error: ${e.message}")
            emit(AssignmentDetailState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun createAssignment(assignment: AssignmentModel): Flow<AssignmentState> =
        flow {
            Timber.i("create assignment by course id: $assignment")
            emit(AssignmentState.Loading)
            try {
                val response = assignmentApi.createAssignment(
                    CreateAssignmentBody(
                        name = assignment.name,
                        timeEnd = assignment.endTime,
                        note = assignment.note,
                        courseId = assignment.courseId
                    )
                )
                Timber.d("response: $response")
                if (response.assignment != null) {
                    Timber.i("create assignment success")
                    emit(AssignmentState.Success(response.assignment))
                } else {
                    Timber.i("create assignment error: ${response.errorMessage}")
                    emit(AssignmentState.Error(response.errorMessage ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(AssignmentState.Error(e.message.toString()))
                Timber.i("create assignment error: ${e.message}")
            }
        }

    override suspend fun updateAssignment(assignment: AssignmentModel): Flow<AssignmentState> =
        flow {
            Timber.i("update assignment by assignmentId: $assignment")
            emit(AssignmentState.Loading)
            delay(700L)
            try {
                val response = assignmentApi.updateAssignment(
                    assignment.id, UpdateAssignmentBody(
                        name = assignment.name,
                        timeEnd = assignment.endTime,
                        state = assignment.state,
                        note = assignment.note,
                        courseId = assignment.courseId
                    )
                )
                if (response.assignment != null) {
                    Timber.i("update assignment by assignmentId success, result: ${response.assignment}")
                    emit(AssignmentState.Success(response.assignment))
                } else {
                    Timber.i("update assignment by assignmentId error: ${response.errorMessage}")
                    emit(AssignmentState.Error(response.errorMessage ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Timber.i("update assignment by assignmentId error: ${e.message}")
                emit(AssignmentState.Error(e.message ?: "Unknown error"))
            }
        } 

    override suspend fun deleteAssignment(assignmentId: Int): Flow<DeleteState> = flow {
        Timber.i("delete assignment by assignmentId: $assignmentId")
        emit(DeleteState.Loading)
        delay(700L)
        try {
            val response = assignmentApi.deleteAssignment(assignmentId)
            if (response.statusCode == "200") {
                Timber.i("delete assignment by assignmentId success")
                emit(DeleteState.Success)
            } else {
                Timber.i("delete assignment by assignmentId error: ${response.statusMsg}")
                emit(DeleteState.Error(response.statusMsg ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Timber.i("delete assignment by assignmentId error: ${e.message}")
            emit(DeleteState.Error(e.message ?: "Unknown error"))
        }
    }
}

interface AssignmentRepository {
    suspend fun getAllAssignments(): Flow<AssignmentsState>
    suspend fun getAssignments(courseId: Int): Flow<AssignmentsState>
    suspend fun getAssignment(assignmentId: Int): Flow<AssignmentDetailState>
    suspend fun createAssignment(assignment: AssignmentModel): Flow<AssignmentState>
    suspend fun updateAssignment(assignment: AssignmentModel): Flow<AssignmentState>
    suspend fun deleteAssignment(assignmentId: Int): Flow<DeleteState>
}