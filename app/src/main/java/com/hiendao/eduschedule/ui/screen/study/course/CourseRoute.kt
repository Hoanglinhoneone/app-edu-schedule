package com.hiendao.eduschedule.ui.screen.study.course

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.hiendao.eduschedule.ui.component.DialogPickerModel
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.entity.StateCourse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRoute(
    padding: PaddingValues,
    navController: NavController,
    sharedViewModel: CourseViewModel,
    showDialogPickFilter: Boolean,
    onCloseDialog: () -> Unit = {},
    modifier: Modifier = Modifier,

    ) {
    val context = LocalContext.current
    val courseUiState by sharedViewModel.coursesUiState.collectAsState()
    val uiState by sharedViewModel.uiState.collectAsState()
    CourseScreen(
        uiState = uiState,
        onClickCourse = { it ->
            navController.navigate(route = "${AppScreen.CourseDetail.name}/$it")
        },
        onChangeSearchInput = {
            sharedViewModel.updateSearchInput(it)
        },
        onClearClick = {
            sharedViewModel.updateSearchInput("")
        },
        onClickFilter = {},
        onRefresh = {
            sharedViewModel.refreshCourses()
        },
        modifier = Modifier.padding(padding),
    )
    if(showDialogPickFilter) {
        DialogPickerModel(
            typeDialog = Constants.TypeDialog.STATE_COURSE,
            stateCurrent = uiState.filterSelected,
            options = listOf(
                "ALL",
                StateCourse.Ongoing.converter,
                StateCourse.NotYet.converter,
                StateCourse.Ended.converter,
            ),
            onConfirm = {
                onCloseDialog ()
                sharedViewModel.updateFilter(it) },
            onDismissRequest = { onCloseDialog() }
        )
    }
    LaunchedEffect(courseUiState) {
        if(courseUiState is CoursesUiState.Error) {
            Toast.makeText(context, (courseUiState as CoursesUiState.Error).exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
