package com.hiendao.eduschedule.ui.screen.study.coursedetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.screen.study.course.CourseViewModel


@Composable
fun CourseDetailRoute(
    courseId: Int,
    navController: NavController,
    shareViewModel: CourseViewModel,
) {
    val context = LocalContext.current
    val viewmodel: CourseDetailViewModel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val shareUiState by shareViewModel.shareUiState.collectAsStateWithLifecycle()
    val courseDetailUiState by viewmodel.courseDetailUiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewmodel, context) {
        viewmodel.getCourseById(courseId)
    }
    LaunchedEffect(courseDetailUiState) {
        if(courseDetailUiState is CourseDetailUiState.Error) {
            Toast.makeText(context, (courseDetailUiState as CourseDetailUiState.Error).exception.toString(),
                Toast.LENGTH_LONG).show()
        }
    }
    shareViewModel.updateCourseSelected(courseDetail = uiState.course)
    CourseDetailScreen(
        uiState = uiState,
        onClickLesson = { lesson ->
            shareViewModel.updateLessonSelected(lesson = lesson)
            navController.navigate("${AppScreen.LessonDetail.name}/${1}")
        },
        onClickSeeMore = {
            shareViewModel.updateCourseSelected(courseDetail = it)
            navController.navigate(AppScreen.CourseInfo.name)
        },
        onClickAssignment = {
            navController.navigate(route = "${AppScreen.Assignment.name}/${courseId}")

        },
        onRefresh = {
            viewmodel.getCourseById(courseId)
        }
    )

}