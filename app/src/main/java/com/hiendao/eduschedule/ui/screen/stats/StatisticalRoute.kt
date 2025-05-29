package com.hiendao.eduschedule.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticalRoute(
    navController: NavController
) {
    val viewModel : StatisticalViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val courseStatsState by viewModel.courseStatsState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    StatisticalScreen(
        uiState = uiState,
        courseStatsState = courseStatsState,
        onClickCourse = {
            viewModel.updateCourseSelected(it)
            showBottomSheet = true
        },
        onFilterSelected = {
            viewModel.updateFilter(it)
        },
        onRefresh = {
            viewModel.refreshCourseStats()
        }
    )
    if (showBottomSheet) {
        DetailCourseStatScreen(
            courseStatsInformation = uiState.courseSelected,
            onBack = {
                showBottomSheet = false
            }
        )
    }
}

