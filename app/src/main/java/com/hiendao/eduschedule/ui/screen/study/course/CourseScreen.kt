package com.hiendao.eduschedule.ui.screen.study.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.ButtonAdd
import com.hiendao.eduschedule.ui.component.CustomSearchBar
import com.hiendao.eduschedule.ui.screen.add.AddScreen
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.onPrimaryLight
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.TypeAdd
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
    onChangeSearchInput: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onClickCourse: (Int) -> Unit = {},
    onClickFilter: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, { onRefresh()})
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    PullRefreshLayout(state = pullRefreshState) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//        ItemDropdownOne(
//            defaultSelected = stringResource(FilterCourse.Now.title),
//            options = listOf(
//                stringResource(FilterCourse.Now.title),
//                stringResource(FilterCourse.One.title),
//                stringResource(FilterCourse.Two.title),
//                stringResource(FilterCourse.Three.title),
//                stringResource(FilterCourse.All.title)
//            )
//        )
            CustomSearchBar(
                value = uiState.searchInput,
                onValueChange = { onChangeSearchInput(it) },
                onClearClick = { onClearClick() },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Box(

            ) {
                CoursesView(
                    courses = uiState.courses,
                    onClickCourse = {
                        onClickCourse(it)
                    }
                )
                ButtonAdd(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { isBottomSheetVisible = true })
            }
            if (isBottomSheetVisible) {
                AddScreen(
                    typeSelected = TypeAdd.Course,
                    onCloseSheet = { isBottomSheetVisible = false },
                    onAddSuccess = {
                        onRefresh()
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyDataView(
    fraction: Float = 0.7f,
    text: String = stringResource(R.string.empty),
    modifier: Modifier = Modifier
) {
    Box (
       modifier = Modifier.padding(start = 12.dp, end = 12.dp).fillMaxSize()
        ,
        contentAlignment = Alignment.TopCenter
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight(fraction),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.img_empty_course),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
                contentDescription = "Empty Course"
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = text
            )
        }
    }
}

@Composable
fun CoursesView(
    onClickCourse: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    courses: List<Course> =
        listOf(
            Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
            Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(
                1,
                "3 tín chỉ",
                "Lập trình Hệ thống chuyên sâu nâng cao vip pro Hoang ngoc linh"
            ),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Android"),
            Course(1, "3 tín chỉ", "Lập trình Web"),
            Course(1, "3 tín chỉ", "Lập trình Web"),
        )
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            )
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .border(
                1.dp,
                onPrimaryLight,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )

    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(courses) { course ->
                ItemCourse(
                    course = course,
                    onClickCourse = { onClickCourse(it) }
                )
            }
        }
        if (courses.isEmpty()) EmptyDataView()
    }
}


//@Preview(showBackground = true)
//@Composable
//fun SearchPreview(modifier: Modifier = Modifier) {
//
//    CustomSearchBar(
//        value = "",
//        onValueChange = {},
//        onClearClick = {}
//    )
//}

@Composable
@Preview(showBackground = true)
fun CourseScreenPreview() {
    EduScheduleTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
            ) {
                CourseScreen(
                    uiState = UiState()
                )
            }
        }
    }
}

