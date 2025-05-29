package com.hiendao.eduschedule.ui.screen.study.courseinfo

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.CourseRepository
import com.hiendao.eduschedule.ui.screen.add.CourseAction
import com.hiendao.eduschedule.ui.screen.add.CourseUiState
import com.hiendao.eduschedule.ui.screen.study.coursedetail.CourseDetailUiState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.convertStringToListDayOfMonth
import com.hiendao.eduschedule.utils.convertStringToListDayOfWeek
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.hiendao.eduschedule.utils.entity.CourseModel
import com.hiendao.eduschedule.utils.entity.DayOfWeek
import com.hiendao.eduschedule.utils.entity.Repeat
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
class CourseInfoViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _courseUiState = MutableStateFlow(CourseUiState())
    val courseUiState: StateFlow<CourseUiState> = _courseUiState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Pending)
    val deleteState: StateFlow<DeleteState> = _deleteState

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Pending)
    val updateState: StateFlow<UpdateState> = _updateState

    fun onCourseInfoAction(action: CourseInfoAction) {
        Timber.i("update course")
        when (action) {
            is CourseInfoAction.OnChangeName -> {
                Timber.i("update name course: ${action.name}")
                _uiState.update { it.copy(name = action.name) }
            }

            is CourseInfoAction.OnUpdate -> {
                if (uiState.value.isFixed) {
                    updateToServer()
                } else {
                    _uiState.update { it.copy(isFixed = true) }
                    Timber.i("fix model = ${uiState.value.isFixed}")
                }
            }

            is CourseInfoAction.OnDelete -> {
                if (uiState.value.isFixed) {
                    updateCourseInfo(uiState.value.courseBackup)
                    _uiState.update { it.copy(isFixed = false) }
                    Timber.i("fix model = ${uiState.value.isFixed}")
                } else {
                    deleteCourse()
                }
            }

            CourseInfoAction.OnResetState -> {
                _deleteState.update { DeleteState.Pending }
                _updateState.update { UpdateState.Pending }
            }
        }
    }

    fun onCourseAction(action: CourseAction) {
        when (action) {
            is CourseAction.OnChangeCredits -> {
                _courseUiState.update { it.copy(credits = action.credits) }
            }

            is CourseAction.OnChangeTeacher -> {
                _courseUiState.update { it.copy(teacher = action.teacher) }
            }

            is CourseAction.OnChangeLocation -> {
                _courseUiState.update {
                    it.copy(
                        addressLearning = action.location

                    )
                }
            }

            is CourseAction.OnChangeStartDay -> {
                _courseUiState.update { it.copy(startDay = action.startDay) }
            }

            is CourseAction.OnChangeEndDay -> {
                _courseUiState.update { it.copy(endDay = action.endDay) }
            }

            is CourseAction.OnChangeRepeatType -> {
                _courseUiState.update { it.copy(repeatType = action.repeatType) }
            }

            is CourseAction.OnChangeDayOfWeek -> {
                _courseUiState.update { currentState ->
                    currentState.copy(
                        dayOfWeeks = currentState.dayOfWeeks.map { dayOfWeek ->
                            if (dayOfWeek == action.dayOfWeek) {
                                dayOfWeek.copy(isSelected = !dayOfWeek.isSelected)
                            } else {
                                dayOfWeek
                            }
                        }
                    )
                }
            }

            is CourseAction.OnChangeDayOfMonth -> {
                _courseUiState.update { currentState ->
                    currentState.copy(
                        dayOfMonths = currentState.dayOfMonths.map { dayOfMonth ->
                            if (dayOfMonth == action.dayOfMonth) {
                                dayOfMonth.copy(isSelected = !dayOfMonth.isSelected)
                            } else {
                                dayOfMonth
                            }
                        }
                    )
                }
            }

            is CourseAction.OnChangeStartTime -> {
                _courseUiState.update {
                    it.copy(
                        lessonStartTime = action.startTime
                    )
                }
            }

            is CourseAction.OnChangeErrorEmpty -> {
                _courseUiState.update { it.copy(showErrorEmpty = action.errorEmpty) }
            }

            is CourseAction.OnChangeEndTime -> {
                _courseUiState.update {
                    it.copy(
                        lessonEndTime = action.endTime
                    )
                }
            }

            is CourseAction.OnChangeNote -> {
                _courseUiState.update { it.copy(note = action.note) }
            }
        }
    }

    private fun deleteCourse() {
        Timber.i("start delete course")
        viewModelScope.launch(Dispatchers.IO) {
            courseRepository.deleteCourseById(uiState.value.id).collect { state ->
                when (state) {
                    is DeleteState.Error -> {
                        Timber.i("error delete course: ${state.message}")
                        _deleteState.update { DeleteState.Error(state.message) }
                    }

                    is DeleteState.Loading -> {
                        Timber.i("loading delete course")
                        _deleteState.update { DeleteState.Loading }
                    }

                    is DeleteState.Pending -> {
                        _deleteState.update { DeleteState.Pending }
                    }

                    is DeleteState.Success -> {
                        Timber.i("success delete course")
                        _deleteState.update { DeleteState.Success }
                    }
                }
            }
        }
    }

    private fun updateToServer() {
        Timber.i("start update course")
        viewModelScope.launch(Dispatchers.IO) {
            courseRepository.updateCourseById(
                uiState.value.id, CourseModel(
                    name = uiState.value.name,
                    credits = courseUiState.value.credits,
                    teacher = courseUiState.value.teacher,
                    timeStart = courseUiState.value.startDay,
                    timeEnd = courseUiState.value.endDay,
                    addressLearning = courseUiState.value.addressLearning,
                    repeatType = courseUiState.value.repeatType.converter,
                )
            ).collect { state ->
                when (state) {
                    is CourseDetailUiState.Error -> {
                        Timber.i("error update course: ${state.exception}")
                        _updateState.update { UpdateState.Error(state.exception) }
                    }

                    is CourseDetailUiState.Loading -> {
                        Timber.i("loading update course")
                        _updateState.update { UpdateState.Loading }
                    }

                    is CourseDetailUiState.Success -> {
                        Timber.i("success update course, data new = : ${state.course}")
                        _updateState.update { UpdateState.Success(state.course) }
                        _uiState.update { it.copy(isFixed = false) }
                    }
                }
            }
        }
    }

    fun updateCourseInfo(courseDetail: CourseDetail) {
        Timber.i("update course info $courseDetail")
        _courseUiState.update {
            it.copy(
                credits = courseDetail.credits,
                teacher = courseDetail.teacher,
                addressLearning = courseDetail.location,
                startDay = courseDetail.startDay,
                endDay = courseDetail.endDay,
                repeatType = when (courseDetail.repeatType) {
                    Repeat.Daily.converter -> Repeat.Daily
                    Repeat.Weekly.converter -> Repeat.Weekly
                    Repeat.Monthly.converter -> Repeat.Monthly
                    Repeat.Yearly.converter -> Repeat.Yearly
                    else -> Repeat.None
                },
                dayOfWeeks = if (courseDetail.repeatType == Repeat.Weekly.converter) convertStringToListDayOfWeek(
                    courseDetail.days
                ) else DataSource.days,
                dayOfMonths = if(courseDetail.repeatType == Repeat.Monthly.converter) convertStringToListDayOfMonth(
                    courseDetail.days
                ) else DataSource.dayOfMonth,
                note = courseDetail.note
            )
        }
        _uiState.update {
            it.copy(
                id = courseDetail.id,
                name = courseDetail.name,
                courseBackup = courseDetail
            )
        }
    }

}

data class UiState(
    val id: Int = 0,
    val name: String = "",
    val showErrorEmpty: Boolean = false,
    val isFixed: Boolean = false,
    val courseBackup: CourseDetail = CourseDetail()
) {
    val isValid: Boolean
        get() = name.isNotEmpty()
}

sealed class UpdateState {
    data object Pending : UpdateState()
    data object Loading : UpdateState()
    data class Success(val courseDetail: CourseDetail) : UpdateState()
    data class Error(val message: String) : UpdateState()
}