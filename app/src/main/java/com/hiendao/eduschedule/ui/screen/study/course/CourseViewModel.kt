package com.hiendao.eduschedule.ui.screen.study.course

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.CourseRepository
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.normalizeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : BaseViewModel() {

    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _coursesUiState = MutableStateFlow<CoursesUiState>(CoursesUiState.Loading)
    val coursesUiState: StateFlow<CoursesUiState> = _coursesUiState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _shareUiState = MutableStateFlow(ShareUiState())
    val shareUiState: StateFlow<ShareUiState> = _shareUiState.asStateFlow()

    /* **********************************************************************
     * Init
     ***********************************************************************/
    init {
        refreshCourses()
    }

    fun refreshCourses() {
        _uiState.update { it.copy(isRefresh = true) }
        Timber.i("initCourses")
        viewModelScope.launch {
            courseRepository.getCourses().collect { state ->
                _coursesUiState.value = state
                if (state is CoursesUiState.Success) {
                    _uiState.update { uiState ->
                        uiState.copy(courses = state.courses, coursesBackup = state.courses)
                    }
                }
                if (state !is CoursesUiState.Loading) _uiState.update { it.copy(isRefresh = false) }
            }
        }
    }

    /* **********************************************************************
     * Function Set/Get
     ***********************************************************************/
    fun updateFilter(filterCourse: String) {
        Timber.i("updateFilter: $filterCourse")
        _uiState.update { it.copy(filterSelected = filterCourse) }
        searchCourseByFilter()
    }


    fun updateSearchInput(searchInput: String) {
        _uiState.update { it.copy(searchInput = searchInput) }
        searchCourseByText()
        if (searchInput.isEmpty()) _uiState.update { it.copy(courses = it.coursesBackup) }
    }

    fun updateCourseSelected(courseDetail: CourseDetail) {
        _shareUiState.update { it.copy(courseDetail = courseDetail) }
    }

    fun updateLessonSelected(lesson: Lesson) {
        _shareUiState.update { it.copy(lessonSelected = lesson) }
    }

    private fun searchCourseByText() {
        _uiState.update { state ->
            state.copy(courses = state.coursesBackup.filter { course ->
                course.name.normalizeString()
                    .contains(uiState.value.searchInput.normalizeString(), true)
            })
        }
    }

    private fun searchCourseByFilter() {
        if (uiState.value.filterSelected == "ALL") {
            _uiState.update { it.copy(courses = it.coursesBackup) }
        } else {
            _uiState.update { ui ->
                ui.copy(courses = ui.coursesBackup.filter { course ->
                    course.state == uiState.value.filterSelected
                })
            }
        }
    }
}

data class UiState(
    val filterSelected: String = "ALL",
    val searchInput: String = "",
    val lessonSelected: Lesson? = null,
    val isRefresh: Boolean = false,
    val courses: List<Course> = emptyList(),
    val coursesBackup: List<Course> = emptyList(),
)

sealed class CoursesUiState {
    data object Loading : CoursesUiState()
    data class Success(val courses: List<Course>) : CoursesUiState()
    data class Error(val exception: Throwable) : CoursesUiState()
}

data class ShareUiState(
    val courses: List<Course> = emptyList(),
    val courseDetail: CourseDetail = CourseDetail(),
    val lessonSelected: Lesson = Lesson(),
)