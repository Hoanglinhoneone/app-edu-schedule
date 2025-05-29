package com.hiendao.eduschedule.ui.screen.study.assignment

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.AssignmentRepository
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.changeStateAssignment
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.AssignmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _assignmentsState = MutableStateFlow<AssignmentsState>(AssignmentsState.Loading)
    val assignmentsState: StateFlow<AssignmentsState> = _assignmentsState

    private val _assignmentState = MutableStateFlow<AssignmentState>(AssignmentState.Loading)
    val assignmentState: StateFlow<AssignmentState> = _assignmentState

    fun initAssignments(courseId: Int) {
        Timber.i("get assignments")
        viewModelScope.launch {
            assignmentRepository.getAssignments(courseId).collect { state ->
                when(state) {
                    is AssignmentsState.Loading -> _assignmentsState.update { AssignmentsState.Loading }
                    is AssignmentsState.Success -> {
                        _uiState.value = UiState(state.data)
                        updateTypeAssignment()
                        _assignmentsState.update { AssignmentsState.Success(state.data) }
                    }
                    is AssignmentsState.Error -> _assignmentsState.update { AssignmentsState.Loading }
                }
            }
        }
    }

    fun onAssignmentAction(action: OnAssignmentAction) {
        if (action is OnAssignmentAction.OnChangeState) {
            updateAssignment(action.assignmentId)
        }
    }

    fun updateAssignment(assignmentId: Int) {
        Timber.i("update assignment: $assignmentId")
        _uiState.update { it.copy(isRefresh = true) }
        val assignment = uiState.value.assignments.find { it.id == assignmentId }
        Timber.d("state old: ${assignment?.state}")
        assignment?.let { assignmentLocal ->
            val assignmentModel = AssignmentModel(
                id = assignmentLocal.id,
                name = assignmentLocal.name,
                endTime = assignmentLocal.endTime,
                state = assignmentLocal.state.changeStateAssignment(
                    endTime = assignmentLocal.endTime,
                ),
                note = assignmentLocal.note,
                courseId = assignmentLocal.courseId
            )
            Timber.d("state new: ${assignmentModel.state}")
            updateAssignmentLocal(assignmentModel)
            viewModelScope.launch {
                assignmentRepository.updateAssignment(
                    assignmentModel
                ).collect {
                    _assignmentState.value = it
                    if (it is AssignmentState.Success) {
                        Timber.i("update assignment success, data updated before call api")
                    } else if (it is AssignmentState.Error) {
                        Timber.i("update assignment error: ${it.message}")
                        updateAssignmentLocal(
                            assignmentModel.copy(
                                state = assignmentLocal.state
                            )
                        )
                    }
                    if(it is AssignmentState.Success || it is AssignmentState.Error) {
                        _uiState.update { it.copy(isRefresh = false) }
                    }
                }
            }
        }
    }

    fun updateTypeAssignment() {
        _uiState.update { ui ->
            ui.copy(
                assignmentCompleted = uiState.value.assignments.filter { it.state == "COMPLETE" },
                assignmentIncomplete = uiState.value.assignments.filter { it.state == "INCOMPLETE" || it.state == "OVERDUE" }
            )
        }
    }

    private fun updateAssignmentLocal(assignmentModel: AssignmentModel) {
        val assignment = Assignment(
            id = assignmentModel.id,
            name = assignmentModel.name,
            endTime = assignmentModel.endTime,
            state = assignmentModel.state,
            note = assignmentModel.note,
        )
        Timber.i("update assignment local: $assignment")
        val assignments = uiState.value.assignments.toMutableList()
        _uiState.update {
            it.copy(assignments = assignments.map { item ->
                if (item.id == assignment.id) {
                    assignment
                } else {
                    item
                }
            }
            )
        }
        updateTypeAssignment()
    }
}

data class UiState(
    val assignments: List<Assignment> = DataSource.assignment,
    val assignmentCompleted: List<Assignment> = assignments.filter { it.state == "COMPLETE" },
    val assignmentIncomplete: List<Assignment> = assignments.filter { it.state == "INCOMPLETE" || it.state == "OVERDUE" },
    val isRefresh: Boolean = false,
)

sealed class AssignmentsState {
    data object Loading : AssignmentsState()
    data class Success(val data: List<Assignment>) : AssignmentsState()
    data class Error(val message: String) : AssignmentsState()
}

sealed class AssignmentState {
    data object Loading : AssignmentState()
    data class Success(val assignment: Assignment) : AssignmentState()
    data class Error(val message: String) : AssignmentState()
}