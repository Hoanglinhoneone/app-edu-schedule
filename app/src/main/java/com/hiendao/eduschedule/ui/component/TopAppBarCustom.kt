package com.hiendao.eduschedule.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.onSecondaryLight
import com.hiendao.eduschedule.ui.theme.secondaryLight
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    @StringRes title: Int,
    currentRoute: String = "",
    scrollBehavior: TopAppBarScrollBehavior,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onIconOneClicked: () -> Unit = {}, // icon button 1 example : assignment in top app bar
    onIconTwoClicked: () -> Unit = {}, // icon button 2
    modifier: Modifier = Modifier
) {
    val scrollFraction = scrollBehavior.state.collapsedFraction
    Timber.i("Current route: $currentRoute")
    Column(
        modifier = modifier
            .then(
                if(currentRoute == AppScreen.CourseDetail.name) {
                    Modifier.background(Color.Transparent)
                } else {
                    Modifier
                }
            )
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        CenterAlignedTopAppBar (
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            modifier = modifier
                .fillMaxWidth(),
            title = { Text(stringResource(title), color = MaterialTheme.colorScheme.secondary) },
            navigationIcon = {
                // TODO: Dựa vào current Route or canNavigateBack
                if (canNavigateBack && currentRoute != AppScreen.HomeTab.name
                    && currentRoute != AppScreen.Utilities.name
                    && currentRoute != AppScreen.Notification.name
                    && currentRoute != AppScreen.Setting.name
                    && currentRoute != AppScreen.Login.name
//                    && currentRoute != AppScreen.Courses.name
                    ) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                }
            },
            actions = {
                // TODO: Check theo màn hình mới add icon
                if (currentRoute == AppScreen.CourseDetail.name) {
                    IconButton(onClick = { onIconOneClicked() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_assignment_real),
                            contentDescription = stringResource(R.string.back),
                            tint = onSecondaryLight
                        )
                    }
                }
                if(currentRoute == AppScreen.Courses.name) {
                    IconButton(onClick = {
                        onIconOneClicked()
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            },
            scrollBehavior = scrollBehavior,
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.Transparent,
//                scrolledContainerColor = Color.Transparent
//            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    EduScheduleTheme {
        CustomTopAppBar(
            title = R.string.app_name,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}