package com.hiendao.eduschedule.ui.screen.add

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.datasource.remote.api.PersonalWork
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkBody
import com.hiendao.eduschedule.control.repository.remote.AssignmentRepository
import com.hiendao.eduschedule.control.repository.remote.CourseRepository
import com.hiendao.eduschedule.control.repository.remote.PersonalWorkRepository
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepository
import com.hiendao.eduschedule.ui.notification.OfflineNotification
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleItem
import com.hiendao.eduschedule.ui.screen.study.assignment.AssignmentState
import com.hiendao.eduschedule.ui.screen.study.course.CoursesUiState
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.checkValidDate
import com.hiendao.eduschedule.utils.checkValidDateTime
import com.hiendao.eduschedule.utils.checkValidTime
import com.hiendao.eduschedule.utils.combineDate
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.AssignmentModel
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.hiendao.eduschedule.utils.entity.CourseModel
import com.hiendao.eduschedule.utils.entity.DayOfMonth
import com.hiendao.eduschedule.utils.entity.DayOfWeek
import com.hiendao.eduschedule.utils.entity.Repeat
import com.hiendao.eduschedule.utils.entity.ScheduleDTO
import com.hiendao.eduschedule.utils.entity.TypeAdd
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
class AddViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val assignmentRepository: AssignmentRepository,
    private val scheduleRepository: ScheduleRepository,
    private val personalWorkRepository: PersonalWorkRepository
) : BaseViewModel() {

    private val _shareUiState = MutableStateFlow(ShareUiState())
    val shareUiState: StateFlow<ShareUiState> = _shareUiState.asStateFlow()

    // course
    private val _courseUiState = MutableStateFlow(CourseUiState())
    val courseUiState: StateFlow<CourseUiState> = _courseUiState.asStateFlow()
    private val _addCourseState = MutableStateFlow<AddCourseState>(AddCourseState.Loading)
    val addCourseState: StateFlow<AddCourseState> = _addCourseState.asStateFlow()
    private val _courses = MutableStateFlow<CoursesUiState>(CoursesUiState.Loading)
    val courses: StateFlow<CoursesUiState> = _courses.asStateFlow()

    // assignment
    private val _assignmentUiState = MutableStateFlow(AssignmentUiState())
    val assignmentUiState: StateFlow<AssignmentUiState> = _assignmentUiState.asStateFlow()
    private val _addAssignmentState =
        MutableStateFlow<AddAssignmentState>(AddAssignmentState.Loading)
    val addAssignmentState: StateFlow<AddAssignmentState> = _addAssignmentState.asStateFlow()

    //event
    private val _eventUiState = MutableStateFlow(EventUiState())
    val eventUiState: StateFlow<EventUiState> = _eventUiState.asStateFlow()
    private val _addScheduleLearningState =
        MutableStateFlow<AddScheduleLearningState>(AddScheduleLearningState.NotSet)
    val addScheduleLearningState: StateFlow<AddScheduleLearningState> = _addScheduleLearningState.asStateFlow()
    private val _addPersonalWorkState =
        MutableStateFlow<AddPersonalWorkState>(AddPersonalWorkState.NotSet)
    val addPersonalWorkState: StateFlow<AddPersonalWorkState> = _addPersonalWorkState.asStateFlow()

    init {
        initCourses()
    }

    fun resetState(){
        _addCourseState.update { AddCourseState.Loading }
        _addAssignmentState.update { AddAssignmentState.Loading }
        _addScheduleLearningState.update { AddScheduleLearningState.NotSet }
        _addPersonalWorkState.update { AddPersonalWorkState.NotSet }
    }

    private fun initCourses() {
        Timber.i("initCourses")
        viewModelScope.launch(Dispatchers.IO) {
            _eventUiState.update { it.copy(courses = DataSource.user.listCourse ?: emptyList()) }
            _assignmentUiState.update { it.copy(courses = DataSource.user.listCourse ?: emptyList()) }
        }
    }

    fun onAddAction(action: AddAction) {
        when (action) {
            is AddAction.OnTitleChange -> {
                _shareUiState.update { it.copy(title = action.title) }
            }

            is AddAction.OnAdd -> {
                when (_shareUiState.value.typeSelected) {
                    TypeAdd.Course -> {
                        addCourse()
                    }

                    TypeAdd.Assignment -> {
                        addAssignment()
                    }

                    TypeAdd.Event -> {
                        if(eventUiState.value.courseId != 0){
                            addEvent()
                        } else {
//                            _eventUiState.update { it.copy(showErrorEmpty = true) }
                            addPersonalWork()
                        }
                    }

                    TypeAdd.Note -> {}
                }
            }

            is AddAction.OnTypeChange -> {
                Timber.i("change type to : ${action.type}")
                _shareUiState.update { it.copy(typeSelected = action.type) }
                Timber.d("shareUiState: ${shareUiState.value}")
            }
        }
    }

    fun onEventAction(action: EventAction) {
        when(action){
            is EventAction.OnChangeCourses -> {
                val course = action.courses
                val credits = if(course.credits.get(0) != '0') {
                    course.credits
                } else ""

                val teacher = if(course.teacher != "Trống"){
                    course.teacher
                } else ""
                val location = if(course.location != "Trống"){
                    course.location
                } else ""
                val notes = course.note
                _eventUiState.update {
                    it.copy(
                        courseId = action.courses.id,
                        credits = credits,
                        teacher = teacher,
                        addressEvent = location,
                        note = notes
                    )
                }
            }
            is EventAction.OnChangeCredits -> {
                _eventUiState.update { it.copy(credits = action.credits) }
            }
            is EventAction.OnChangeEndDay -> {
                _eventUiState.update { it.copy(endDay = action.endDay) }
            }
            is EventAction.OnChangeEndTime -> {
                _eventUiState.update { it.copy(endTime = action.endTime) }
            }
            is EventAction.OnChangeErrorEmpty -> {
                _eventUiState.update { it.copy(showErrorEmpty = action.errorEmpty) }
            }
            is EventAction.OnChangeLocation -> {
                _eventUiState.update { it.copy(addressEvent = action.location) }
            }
            is EventAction.OnChangeNote -> {
                _eventUiState.update { it.copy(note = action.note) }
            }
            is EventAction.OnChangeStartDay -> {
                _eventUiState.update { it.copy(startDay = action.startDay) }
            }
            is EventAction.OnChangeStartTime -> {
                _eventUiState.update { it.copy(startTime = action.startTime) }
            }
            is EventAction.OnChangeTeacher -> {
                _eventUiState.update { it.copy(teacher = action.teacher) }
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

    fun onAssignmentAction(action: AssignmentAction) {
        when (action) {
            is AssignmentAction.OnChangeCourseId -> {
                _assignmentUiState.update { it.copy(courseId = action.courseId) }
            }

            is AssignmentAction.OnChangeEndDay -> {
                _assignmentUiState.update { it.copy(endDay = action.endDay) }
            }

            is AssignmentAction.OnChangeEndTime -> {
                _assignmentUiState.update { it.copy(endTime = action.endTime) }
            }

            is AssignmentAction.OnChangeNote -> {
                _assignmentUiState.update { it.copy(note = action.note) }
            }
        }
    }


    private fun addCourse() {
        Timber.i("add Course: ${courseUiState.value}")

        viewModelScope.launch {
            courseRepository.createCourse(
                CourseModel(
                    name = shareUiState.value.title,
                    credits = courseUiState.value.credits,
                    teacher = courseUiState.value.teacher,
                    addressLearning = courseUiState.value.addressLearning,
                    timeStart = courseUiState.value.startDay,
                    timeEnd = courseUiState.value.endDay,
                    repeatType = courseUiState.value.repeatType.converter,
                    listDay = when (courseUiState.value.repeatType) {
                        Repeat.Daily -> ""
                        Repeat.Weekly -> convertDaysOfWeekToString()
                        Repeat.Monthly -> convertDaysOfMonthToString(courseUiState.value.startDay)
                        else -> ""
                    },
                    note = courseUiState.value.note
                )
            ).collect { state ->
                Timber.d("Update State: $state")
                when (state) {
                    is AddCourseState.Loading -> _addCourseState.update { AddCourseState.Loading }
                    is AddCourseState.Success -> {
                        _addCourseState.update {
                            clearData()
                            AddCourseState.Success(state.data)
                        }
                    }

                    is AddCourseState.Error -> _addCourseState.update { AddCourseState.Error(state.message) }
                }
            }
        }
    }

    private fun addAssignment() {
        viewModelScope.launch {
            assignmentRepository.createAssignment(
                AssignmentModel(
                    name = shareUiState.value.title,
                    endTime = combineDate(
                        assignmentUiState.value.endDay,
                        assignmentUiState.value.endTime
                    ),
                    note = assignmentUiState.value.note,
                    courseId = assignmentUiState.value.courseId
                )
            ).collect { state ->
                Timber.i("Update State: $state")
                when (state) {
                    is AssignmentState.Error -> _addAssignmentState.update {
                        AddAssignmentState.Error(
                            state.message
                        )
                    }

                    AssignmentState.Loading -> _addAssignmentState.update { AddAssignmentState.Loading }
                    is AssignmentState.Success -> {
                        _addAssignmentState.update { AddAssignmentState.Success(state.assignment) }
                        clearData()
                    }
                }
            }
        }
    }

    private fun addPersonalWork(){
        viewModelScope.launch {
            personalWorkRepository.createPersonalWork(
                PersonalWorkBody(
                    name = shareUiState.value.title,
                    description = eventUiState.value.note,
                    timeStart = combineDate(
                        eventUiState.value.startDay,
                        eventUiState.value.startTime
                    ),
                    timeEnd = combineDate(
                        eventUiState.value.endDay,
                        eventUiState.value.endTime
                    ),
                    workAddress = eventUiState.value.addressEvent,
                    repeatCycle = null
                )
            ).collect{state ->
                Timber.i("Update State: $state")
                when (state) {
                    is AddPersonalWorkState.Error -> _addPersonalWorkState.update {
                        AddPersonalWorkState.Error(
                            state.message
                        )
                    }

                    AddPersonalWorkState.Loading -> _addPersonalWorkState.update { AddPersonalWorkState.Loading }
                    is AddPersonalWorkState.Success -> {
                        _addPersonalWorkState.update { AddPersonalWorkState.Success(state.personalWork) }
                        clearData()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun addEvent() {
        viewModelScope.launch {
            scheduleRepository.addSchedule(
                ScheduleItem(
                    name = shareUiState.value.title,
                    timeStart = combineDate(
                        eventUiState.value.startDay,
                        eventUiState.value.startTime
                    ),
                    timeEnd = combineDate(
                        eventUiState.value.endDay,
                        eventUiState.value.endTime
                    ),
                    note = eventUiState.value.note,
                    teacher = eventUiState.value.teacher,
                    learningAddresses = eventUiState.value.addressEvent,
                    state = "ABSENT",
                    courseId = eventUiState.value.courseId,
                )
            ).collect { state ->
                Timber.i("Update State: $state")
                when (state) {
                    is AddScheduleLearningState.Error -> _addScheduleLearningState.update {
                        AddScheduleLearningState.Error(state.message)
                    }

                    AddScheduleLearningState.Loading -> _addScheduleLearningState.update {
                        AddScheduleLearningState.Loading
                    }

                    is AddScheduleLearningState.Success -> {
                        _addScheduleLearningState.update { AddScheduleLearningState.Success(state.scheduleLearning) }
                        clearData()
                    }
                    else -> Unit
                }
            }
        }
    }

    fun addNote() {

    }

    private fun clearData() {
        _shareUiState.update { it.copy(title = "") }
        _courseUiState.update {
            it.copy(
                credits = "",
                teacher = "",
                addressLearning = "",
                note = "",
                startDay = "01/01/2025",
                endDay = "01/01/2026",
                repeatType = Repeat.Weekly,
                dayOfWeeks = DataSource.days,
                dayOfMonths = emptyList(),
                lessonStartTime = "13:00",
                lessonEndTime = "15:00",
                showErrorEmpty = false
            )
        }
        _assignmentUiState.update {
            it.copy(
                name = "",
                courses = emptyList(),
                note = "",
                endDay = "17/09/2025",
                endTime = "17:00",
                courseId = 1,
            )
        }
    }

    private fun convertDaysOfWeekToString(): String {
        val selectedDays = courseUiState.value.dayOfWeeks.filter { it.isSelected }
        val dayNames: List<String> = selectedDays.map { dayOfWeek -> dayOfWeek.day.name }
        return dayNames.joinToString(",")
    }

    private fun convertDaysOfMonthToString(startDay: String): String {
        val selectedDays = courseUiState.value.dayOfMonths.filter { it.isSelected }
        val dayNames: List<String> =
            selectedDays.map { dayOfMonth -> dayOfMonth.day.toString() + "/" + startDay.substring(3) }
        return dayNames.joinToString(",")
    }
//
//    private fun setNotification(itemId: Int, time: Long, title: String, content: String, context: Context){
//        val offlineNotification = OfflineNotification(context)
//        offlineNotification.setRepeatingNotification()
//    }
}

sealed class AddCourseState {
    data object Loading : AddCourseState()
    data class Success(val data: CourseDetail) : AddCourseState()
    data class Error(val message: String) : AddCourseState()
}

sealed class AddAssignmentState {
    data object Loading : AddAssignmentState()
    data class Success(val assignment: Assignment) : AddAssignmentState()
    data class Error(val message: String) : AddAssignmentState()
}

sealed class AddScheduleLearningState {
    data object NotSet : AddScheduleLearningState()
    data object Loading : AddScheduleLearningState()
    data class Success(val scheduleLearning: ScheduleDTO) : AddScheduleLearningState()
    data class Error(val message: String) : AddScheduleLearningState()
}

sealed class AddPersonalWorkState {
    data object NotSet : AddPersonalWorkState()
    data object Loading : AddPersonalWorkState()
    data class Success(val personalWork: PersonalWork) : AddPersonalWorkState()
    data class Error(val message: String) : AddPersonalWorkState()
}

data class ShareUiState(
    val title: String = "",
    val typeSelected: TypeAdd = TypeAdd.Assignment,
) {
    val isNoEmpty: Boolean
        get() = title.isNotEmpty()
}

data class EventUiState(
    val courses: List<Course> = emptyList(),
    val courseId: Int = 0,
    val credits: String = "",
    val teacher: String = "",
    val addressEvent: String = "",
    val startDay: String = "01/01/2025",
    val endDay: String = "01/01/2026",
    val startTime: String = "13:00",
    val endTime: String = "15:00",
    val note: String = "",
    val showErrorEmpty: Boolean = false
) {
    val isNoEmpty: Boolean
        get() = teacher.isNotEmpty() && addressEvent.isNotEmpty()
    val isValidTime: Boolean
        get() = checkValidTime(startTime, endTime)
    val isValidDay: Boolean
        get() = checkValidDate(startDay, endDay)
    val isValid: Boolean
        get() = isNoEmpty && isValidTime && isValidDay
}

data class CourseUiState(
    val credits: String = "",
    val teacher: String = "",
    val addressLearning: String = "",
    val note: String = "",
    val startDay: String = "01/01/2025",
    val endDay: String = "01/01/2026",
    val repeatType: Repeat = Repeat.Weekly,
    val dayOfWeeks: List<DayOfWeek> = DataSource.days,
    val dayOfMonths: List<DayOfMonth> = DataSource.dayOfMonth,
    val lessonStartTime: String = "13:00",
    val lessonEndTime: String = "15:00",
    val showErrorEmpty: Boolean = false
) {
    val isNoEmpty: Boolean
        get() = teacher.isNotEmpty() && addressLearning.isNotEmpty()
    val isValidTime: Boolean
        get() = checkValidTime(lessonStartTime, lessonEndTime)
    val isValidDay: Boolean
        get() = checkValidDate(startDay, endDay)
    val isValid: Boolean
        get() = isNoEmpty && isValidTime && isValidDay
}

data class AssignmentUiState(
    val name: String = "",
    val courses: List<Course> = emptyList(),
    val note: String = "",
    val endDay: String = "17/09/2025",
    val endTime: String = "17:00",
    val courseId: Int = 1,
) {
    val isValidDateTime: Boolean
        get() = checkValidDateTime(endDay = endDay, endTime = endTime)
}

