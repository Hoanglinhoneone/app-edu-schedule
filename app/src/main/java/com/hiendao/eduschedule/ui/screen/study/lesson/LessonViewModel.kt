package com.hiendao.eduschedule.ui.screen.study.lesson

import com.hiendao.eduschedule.control.repository.remote.LessonRepository
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepository: LessonRepository

) : BaseViewModel() {
    private val _uiState = MutableStateFlow<LessonState>(LessonState.Loading)
    val uiState: MutableStateFlow<LessonState> = _uiState

}

sealed class LessonState {
    data object Loading : LessonState()
    data class Success(val lessons: List<Lesson>) : LessonState()
    data class Error(val message: String) : LessonState()
}