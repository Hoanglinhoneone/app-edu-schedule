package com.hiendao.eduschedule.ui.screen.study.assigndetail

import  androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.AssignmentRepository
import com.hiendao.eduschedule.ui.screen.study.assignment.AssignmentState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.changeStateAssignment
import com.hiendao.eduschedule.utils.checkValidDateTime
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.AssignmentModel
import com.hiendao.eduschedule.utils.entity.StateAssignment
import com.hiendao.eduschedule.utils.getDay
import com.hiendao.eduschedule.utils.getTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AssignmentDetailViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Pending)
    val deleteState = _deleteState.asStateFlow()

    private val _updateState =
        MutableStateFlow<UpdateAssignmentState>(UpdateAssignmentState.Pending)
    val updateState = _updateState.asStateFlow()

    private val _assignmentState =
        MutableStateFlow<AssignmentDetailState>(AssignmentDetailState.Loading)
    val assignmentState = _assignmentState.asStateFlow()

    fun initAssignment(assignmentId: Int) {
        Timber.i("initAssignment with assignmentId: $assignmentId")
        _uiState.update { it.copy(isRefresh = true) }
        viewModelScope.launch(Dispatchers.IO) {
            assignmentRepository.getAssignment(assignmentId).collect { state ->
                when (state) {
                    is AssignmentDetailState.Loading -> {
                        _assignmentState.update { AssignmentDetailState.Loading }
                    }

                    is AssignmentDetailState.Success -> {
                        updateAssignmentLocal(state.assignment)
                        _assignmentState.update {
                            AssignmentDetailState.Success(state.assignment)
                        }
                        _uiState.update { it.copy(isRefresh = false) }

                    }

                    is AssignmentDetailState.Error -> {
                        _assignmentState.update { AssignmentDetailState.Error(state.message) }
                        _uiState.update { it.copy(isRefresh = false) }

                    }
                }
            }
        }
    }

    fun updateAssignmentLocal(assignment: Assignment) {
        _uiState.update {
            it.copy(
                id = assignment.id.toString(),
                name = assignment.name,
                note = assignment.note,
                endDay = assignment.endTime.getDay(),
                endTime = assignment.endTime.getTime(),
                state = when (assignment.state) {
                    "COMPLETE" -> StateAssignment.Complete.converter
                    "INCOMPLETE" -> StateAssignment.Incomplete.converter
                    else -> StateAssignment.Overdue.converter
                },
                courseId = assignment.courseId.toString(),
                assignmentBackup = assignment
            )
        }
    }

    fun deleteAssignment(assignmentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            assignmentRepository.deleteAssignment(assignmentId).collect { state ->
                when (state) {
                    is DeleteState.Loading -> _deleteState.update { DeleteState.Loading }
                    is DeleteState.Success -> _deleteState.update { DeleteState.Success }
                    is DeleteState.Error -> _deleteState.update { DeleteState.Error(state.message) }
                    is DeleteState.Pending -> _deleteState.update { DeleteState.Pending }
                }
            }
        }
    }

    fun onAssignmentAction(action: AssignmentDetailAction) {
        when (action) {
            is AssignmentDetailAction.OnChangeNote -> {
                _uiState.update { it.copy(note = action.note) }
            }

            is AssignmentDetailAction.OnChangeEndDay -> {
                _uiState.update { it.copy(endDay = action.endDay) }
            }

            is AssignmentDetailAction.OnChangeEndTime -> {
                _uiState.update { it.copy(endTime = action.endTime) }
            }

            is AssignmentDetailAction.OnChangeState -> {
                _uiState.update {
                    it.copy(
                        state = uiState.value.state.changeStateAssignment(
                            endTime =  "${uiState.value.endDay}T${uiState.value.endTime}:00",
                        ),
                    )
                }
            }

            is AssignmentDetailAction.OnDelete -> {
                if (_uiState.value.isFixed) {
                    _uiState.update { it.copy(isFixed = false) }
                } else {
                    deleteAssignment(uiState.value.id.toInt())
                }
            }

            is AssignmentDetailAction.OnFix -> {
                if (uiState.value.isFixed) {
                    updateAssignmentToServer()
                } else {
                    _uiState.update { it.copy(isFixed = true) }
                }
            }

            is AssignmentDetailAction.OnResetState -> {
                _deleteState.update { DeleteState.Pending }
                _updateState.update { UpdateAssignmentState.Pending }
            }

            is AssignmentDetailAction.OnChangeTitle -> {
                _uiState.update { it.copy(name = action.title) }
            }
        }
    }

    private fun updateAssignmentToServer() {
        Timber.i("update assignment: ${uiState.value.id}")
        val assignmentModel = AssignmentModel(
            id = uiState.value.id.toInt(),
            name = uiState.value.name,
            endTime = "${uiState.value.endDay}T${uiState.value.endTime}:00",
            state = uiState.value.state,
            note = uiState.value.note,
            courseId = uiState.value.courseId.toInt()
        )
        Timber.d("state new: ${assignmentModel.state}")
        viewModelScope.launch {
            assignmentRepository.updateAssignment(
                assignmentModel
            ).collect { state ->
                if (state is AssignmentState.Success) {
                    _updateState.update { UpdateAssignmentState.Success(state.assignment) }
                    Timber.i("update assignment success, data updated before call api")
                    _uiState.update { it.copy(isFixed = false) }
                } else if (state is AssignmentState.Error) {
                    _updateState.update { UpdateAssignmentState.Error(state.message) }
                    Timber.i("update assignment error: ${state.message}")
                    updateAssignmentLocal(uiState.value.assignmentBackup)
                } else {
                    _updateState.update { UpdateAssignmentState.Loading }
                }
            }
        }
    }
}

data class UiState(
    val id: String = "",
    val name: String = "",
    val note: String = "",
    val endDay: String = "",
    val endTime: String = "",
    val state: String = "",
    val courseId: String = "",
    val assignmentBackup: Assignment = Assignment(),
    val isFixed: Boolean = false,
    val isRefresh: Boolean = false
) {
    val isEmpty: Boolean
        get() = name.isEmpty()
    val isValidDateTime: Boolean
        get() = checkValidDateTime(endDay = endDay, endTime = endTime)
}

sealed class AssignmentDetailState {
    data object Loading : AssignmentDetailState()
    data class Success(val assignment: Assignment) : AssignmentDetailState()
    data class Error(val message: String) : AssignmentDetailState()
}

sealed class UpdateAssignmentState {
    data object Pending : UpdateAssignmentState()
    data object Loading : UpdateAssignmentState()
    data class Success(val assignment: Assignment) : UpdateAssignmentState()
    data class Error(val message: String) : UpdateAssignmentState()
}