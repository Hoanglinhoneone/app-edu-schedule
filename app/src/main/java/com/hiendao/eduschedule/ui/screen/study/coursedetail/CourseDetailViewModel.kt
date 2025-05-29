package com.hiendao.eduschedule.ui.screen.study.coursedetail

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.CourseRepository
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.entity.StateCourse
import com.hiendao.eduschedule.utils.entity.StateLesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : BaseViewModel() {

    private val _courseDetailUiState =
        MutableStateFlow<CourseDetailUiState>(CourseDetailUiState.Loading)
    val courseDetailUiState: StateFlow<CourseDetailUiState> = _courseDetailUiState

    private val _deleteCourseState = MutableStateFlow<DeleteState>(DeleteState.Pending)
    val deleteCourseState: StateFlow<DeleteState> = _deleteCourseState

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    fun getCourseById(courseId: Int) {
        Timber.i("get CourseDetail with courseId: $courseId")
        _uiState.update { it.copy(isRefresh = true) }
        viewModelScope.launch {
            courseRepository.getCourseById(courseId).collect {
                Timber.d("result $it")
                _courseDetailUiState.value = it
                if (it is CourseDetailUiState.Success) {
                    _uiState.update { uiState ->
                        uiState.copy(
                            name = it.course.name,
                            credits = it.course.credits,
                            state = when (it.course.state) {
                                "ONGOING" -> StateCourse.Ongoing
                                "END" -> StateCourse.Ended
                                else -> StateCourse.NotYet
                            },
                            numberOfLesson = it.course.numberOfLesson ?: "",
                            numberOfAssignment = it.course.numberOfAssignment,
                            lessons = it.course.lessons,
                            course = it.course,
                            teacher = it.course.teacher,
                            numberOfAbsent =it.course.lessons.filter { lesson -> lesson.state == StateLesson.Absent.converter }.size
                        )
                    }
                }
                if(it !is CourseDetailUiState.Loading) {
                    _uiState.update { ui -> ui.copy(isRefresh = false) }
                }
            }
        }
    }
}

data class CourseUiState(
    val name: String = "Khóa học lùa chicken",
    val credits: String = "3",
    val state: StateCourse = StateCourse.NotYet,
    val numberOfLesson: String = "",
    val numberOfAssignment: String? = "",
    val numberOfAbsent: Int = 0,
    val lessons: List<Lesson> = emptyList(),
    val course: CourseDetail = CourseDetail(),
    val teacher: String = "",
    val isRefresh: Boolean = false
)


sealed class CourseDetailUiState {
    data object Loading : CourseDetailUiState()
    data class Success(val course: CourseDetail) : CourseDetailUiState()
    data class Error(val exception: String) : CourseDetailUiState()
}


