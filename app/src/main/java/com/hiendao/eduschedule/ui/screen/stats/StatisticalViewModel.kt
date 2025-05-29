package com.hiendao.eduschedule.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.StatisticalRepository
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.CourseStats
import com.hiendao.eduschedule.utils.entity.CourseStatsInformation
import com.hiendao.eduschedule.utils.entity.StateCourse
import com.hiendao.eduschedule.utils.getCurrentMonth
import com.hiendao.eduschedule.utils.getFirstDayOfCurrentMonth
import com.hiendao.eduschedule.utils.getLastDayOfCurrentMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StatisticalViewModel @Inject constructor(
    private val statisticalRepository: StatisticalRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _courseStatsState = MutableStateFlow<CourseStatsState>(CourseStatsState.Loading)
    val courseStatsState: StateFlow<CourseStatsState> = _courseStatsState.asStateFlow()

    init {
        refreshCourseStats()
    }


    fun refreshCourseStats() {
        _uiState.update { it.copy(isRefresh = true, currentMonth = getCurrentMonth()) }
        viewModelScope.launch(Dispatchers.IO) {
            statisticalRepository.getCoursesStats(getFirstDayOfCurrentMonth(), getLastDayOfCurrentMonth())
                .collect { state ->
                    when (state) {
                        is CourseStatsState.Loading -> {
                            _courseStatsState.update { CourseStatsState.Loading }
                        }

                        is CourseStatsState.Success -> {
                            _courseStatsState.update { CourseStatsState.Success(state.courseStats) }
                            _uiState.update {
                                it.copy(
                                    courseStatsInformation = state.courseStats.courseStatsInformation,
                                    courseBackup = state.courseStats.courseStatsInformation,
                                    present = state.courseStats.present,
                                    absent = state.courseStats.absent
                                )
                            }
                            _uiState.update { it.copy(isRefresh = false) }
                        }

                        is CourseStatsState.Error -> {
                            _courseStatsState.update { CourseStatsState.Error(state.message) }
                            _uiState.update { it.copy(isRefresh = false) }
                        }
                    }
                }
        }
    }
    fun updateCourseSelected(courseStatsInformation: CourseStatsInformation) {
        _uiState.update {
            it.copy(courseSelected = courseStatsInformation)
        }
    }

    fun updateFilter(filter: Int) {
        when(filter) {
            0 -> {
                _uiState.update {
                    it.copy(courseStatsInformation = it.courseBackup)
                }
            }
            1 -> {
                _uiState.update {
                    it.copy(courseStatsInformation = it.courseBackup.filter { course ->
                        course.state == StateCourse.Ongoing.converter
                    })
                }
            }
            2 -> {
                _uiState.update {
                    it.copy(courseStatsInformation = it.courseBackup.filter { course ->
                        course.state == StateCourse.Ended.converter
                    })
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class UiState (
    val timeStart: String = "",
    val timeEnd: String = "",
    val courseStatsInformation: List<CourseStatsInformation> = emptyList(),
    val courseBackup: List<CourseStatsInformation> = emptyList(),
    val present: Int = 0,
    val absent: Int = 0,
    val courseSelected: CourseStatsInformation = CourseStatsInformation(),
    val currentMonth: Int = 1,
    val isRefresh: Boolean = false,
)

sealed class CourseStatsState {
    data object Loading : CourseStatsState()
    data class Success(val courseStats: CourseStats) : CourseStatsState()
    data class Error(val message: String) : CourseStatsState()
}