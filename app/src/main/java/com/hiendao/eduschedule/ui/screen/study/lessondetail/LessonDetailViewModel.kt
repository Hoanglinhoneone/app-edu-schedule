package com.hiendao.eduschedule.ui.screen.study.lessondetail

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.LessonRepository
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.checkValidDate
import com.hiendao.eduschedule.utils.checkValidTime
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.entity.LessonModel
import com.hiendao.eduschedule.utils.entity.StateLesson
import com.hiendao.eduschedule.utils.getDay
import com.hiendao.eduschedule.utils.getTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val lessonRepository: LessonRepository
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _lessonUiState = MutableStateFlow<LessonDetailState>(LessonDetailState.Loading)
    val lessonUiState: MutableStateFlow<LessonDetailState> = _lessonUiState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Pending)
    val deleteState: MutableStateFlow<DeleteState> = _deleteState

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Pending)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

//    fun initLesson(lessonId: Int) {
//        viewModelScope.launch {
//            lessonRepository.getLesson(lessonId).collect {
//                _lessonUiState.value = it
//                if (it is LessonDetailState.Success) {
//                    updateLesson(it.lesson)
//                }
//            }
//        }
//    }

    /* **********************************************************************
     * Function Set/Get
     ***********************************************************************/
    fun updateLesson(lesson: Lesson) {
        _uiState.update {
            Timber.i("update lesson: $lesson")
            it.copy(
                id = lesson.id,
                name = lesson.name,
                startDay = lesson.startTime.getDay(),
                endDay = lesson.endTime.getDay(),
                startTime = lesson.startTime.getTime(),
                endTime = lesson.endTime.getTime(),
                stateLesson = when (lesson.state) {
                    StateLesson.Absent.converter -> StateLesson.Absent
                    StateLesson.NotYet.converter -> StateLesson.NotYet
                    else -> StateLesson.Present
                },
                address = lesson.location,
                note = lesson.note,
                lessonBackup = lesson
            )
        }
    }

    private fun updateToServer() {
        Timber.i("update to server ${updateState.value}")
        viewModelScope.launch(Dispatchers.IO) {
            lessonRepository.updateLesson(
                LessonModel(
                    id = uiState.value.id,
                    name = uiState.value.name,
                    note = uiState.value.note ?: "",
                    startTime = uiState.value.startDay +"T"+uiState.value.startTime+":00",
                    endTime = uiState.value.startDay +"T"+uiState.value.endTime+":00",
                    teacher = uiState.value.lessonBackup.teacher,
                    address = uiState.value.address,
                    state = uiState.value.stateLesson.converter
                )
            ).collect { state ->
                when (state) {
                    is UpdateState.Success -> {
                        _updateState.update { UpdateState.Success(state.lesson) }
                        _uiState.update { it.copy(isFixed = false) }
//                        updateLesson(state.lesson)
                    }

                    is UpdateState.Error -> {
                        _updateState.update {
                            UpdateState.Error(state.message)
                        }
                        updateLesson(uiState.value.lessonBackup)
                    }

                    is UpdateState.Loading -> _updateState.update { UpdateState.Loading }
                    UpdateState.Pending -> {  }
                }
            }
        }
    }

    private fun deleteLesson() {
        viewModelScope.launch {
            lessonRepository.deleteLesson(uiState.value.id).collect { state ->
                Timber.i("update state delete lesson: $state")
                when (state) {
                    is DeleteState.Success -> _deleteState.update { DeleteState.Success }
                    is DeleteState.Error -> _deleteState.update { DeleteState.Error(state.message) }
                    is DeleteState.Loading -> _deleteState.update { DeleteState.Loading }
                    is DeleteState.Pending -> _deleteState.update { DeleteState.Pending }
                }
            }
        }
    }


    /* **********************************************************************
     * Action
     ***********************************************************************/

    fun onLessonAction(action: LessonAction) {
        when (action) {
            is LessonAction.OnChangeName -> {
                Timber.i("change name: ${action.name}")
                _uiState.update { it.copy(name = action.name) }
            }

            is LessonAction.OnChangeStartDay -> {
                Timber.i("change start day: ${action.startDay}")
                _uiState.update { it.copy(startDay = action.startDay) }
            }

            is LessonAction.OnChangeEndDay -> {
                _uiState.update { it.copy(endDay = action.endDay) }
            }

            is LessonAction.OnChangeStateLesson -> {
                _uiState.update { it.copy(stateLesson = action.stateLesson) }
            }

            is LessonAction.OnChangeLocation -> {
                _uiState.update { it.copy(address = action.location) }
            }

            is LessonAction.OnChangeNote -> {
                _uiState.update { it.copy(note = action.note) }
            }

            is LessonAction.OnDelete -> {
                if (!uiState.value.isFixed) {
                    deleteLesson()
                } else {
                    _uiState.update { it.copy(isFixed = false) }
                }
            }

            is LessonAction.OnFix -> {
                if (uiState.value.isFixed) {
                    updateToServer()
                }
                else _uiState.update { it.copy(isFixed = true) }
            }

            is LessonAction.OnChangeEndTime -> {
                _uiState.update { it.copy(endTime = action.endTime) }
            }

            is LessonAction.OnChangeStartTime -> {
                _uiState.update { it.copy(startTime = action.startTime) }
            }

            LessonAction.OnResetState -> {
                _deleteState.update { DeleteState.Pending}
                _updateState.update { UpdateState.Pending }
            }
        }
    }
}

data class UiState(
    val id: Int = 0,
    val name: String = "Buổi học thứ 4",
    val startDay: String = "17/01/2025",
    val endDay: String = "17/01/2025",
    val startTime: String = "15:00",
    val endTime: String = "18:00",
    val stateLesson: StateLesson = StateLesson.Absent,
    val address: String = "online",
    val note: String? = "",
    val isFixed: Boolean = false,
    val lessonBackup: Lesson = Lesson(),
) {
    val isNoEmpty: Boolean
        get() = name.isNotEmpty() && address.isNotEmpty()
    val isValidTime: Boolean
        get() = checkValidTime(startTime, endTime)
    val isValidDay: Boolean
        get() = checkValidDate(startDay, endDay)
    val isValidState : Boolean
        get() = checkValidTime("${startDay}T${startTime}:00")
    val isValid: Boolean
        get() = isNoEmpty && isValidTime && isValidDay && isValidState
}

sealed class LessonDetailState {
    data object Loading : LessonDetailState()
    data class Success(val lesson: Lesson) : LessonDetailState()
    data class Error(val message: String) : LessonDetailState()
}

sealed class DeleteState {
    data object Pending : DeleteState()
    data object Loading : DeleteState()
    data object Success : DeleteState()
    data class Error(val message: String) : DeleteState()
}

sealed class UpdateState {
    data object Pending : UpdateState()
    data object Loading : UpdateState()
    data class Success(val lesson: Lesson) : UpdateState()
    data class Error(val message: String) : UpdateState()
}
