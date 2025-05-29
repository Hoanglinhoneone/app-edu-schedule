package com.hiendao.eduschedule.ui.screen.study.coursedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.ButtonAdd
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.screen.add.AddScreen
import com.hiendao.eduschedule.ui.screen.study.course.EmptyDataView
import com.hiendao.eduschedule.ui.screen.study.lesson.LessonScreen
import com.hiendao.eduschedule.ui.screen.study.lesson.StateLesson
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.entity.CourseDetail
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.entity.StateCourse
import com.hiendao.eduschedule.utils.entity.TypeAdd
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import timber.log.Timber

@Composable
fun CourseDetailScreen(
    uiState: CourseUiState,
    onClickSeeMore: (CourseDetail) -> Unit = {},
    onClickLesson: (Lesson) -> Unit = {},
    onClickAssignment: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, { onRefresh()})

    Timber.i(" open DetailCourse Screen with course: ${uiState.course}")
    val stateCourse = when (uiState.state) {
        StateCourse.Ongoing -> stringResource(StateCourse.Ongoing.title)
        StateCourse.Ended -> stringResource(StateCourse.Ended.title)
        StateCourse.NotYet -> stringResource(StateCourse.NotYet.title)
    }

    val tintColor = if (isSystemInDarkTheme()) secondaryDark else secondaryLight
    PullRefreshLayout(
        state = pullRefreshState
    ) {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                maxLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )

//                        Text(
//                            text = "(${uiState.credits} tín chỉ)",
//                            fontSize = 14.sp,
//                            color = Color.Gray,
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
                            Text(
                                text = when (uiState.state) {
                                    StateCourse.Ongoing -> stringResource(StateCourse.Ongoing.title)
                                    StateCourse.NotYet -> stringResource(StateCourse.NotYet.title)
                                    else -> stringResource(StateCourse.Ended.title)
                                },

                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        HorizontalItemDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(
                                start = 80.dp,
                                end = 80.dp,
                                top = 12.dp,
                                bottom = 8.dp
                            ),
                            color = Color.Black.copy(alpha = 1f)
                        )

                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_lessons),
                                contentDescription = "Course Code",
                            )
                            Text(
                                text = stringResource(
                                    R.string.number_of_lesson,
                                    uiState.numberOfLesson
                                ),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.clickable {
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_teacher),
                                    contentDescription = "Instructor",
                                )

                                Text(
                                    text = stringResource(R.string.teacher_name, uiState.teacher),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Text(
                                text = "Xem thêm >",
                                fontSize = 14.sp,
                                color = secondaryLight,
                                modifier = Modifier
                                    .clickable { onClickSeeMore(uiState.course) }
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
//                        start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Transparent
//                    ),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable { onClickAssignment() }
//                        .size(width = 140.dp, height = 35.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(
                                        Float.POSITIVE_INFINITY,
                                        0f
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.assignment),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 24.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveCircleOutline,
                            contentDescription = "Absent",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Red
                        )
                        Text(
                            text = stringResource(
                                R.string.number_of_absent,
                                uiState.numberOfAbsent
                            ),
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }

                if(uiState.lessons.isNotEmpty()) {
                    LessonScreen(
                        listLesson = uiState.lessons,
                        onClickLesson = { onClickLesson(it) },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                } else {
                    EmptyDataView(0.6f)
                }
//            ButtonAdd(onClick = { isBottomSheetVisible = true })
//            if (isBottomSheetVisible) {
//                AddScreen(
//                    typeSelected = TypeAdd.Course,
//                    onCloseSheet = { isBottomSheetVisible = false }
//                )
//            }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailCoursePreview(modifier: Modifier = Modifier) {
    EduScheduleTheme {
        CourseDetailScreen(
            uiState = CourseUiState(),
            onClickSeeMore = {},
            onClickLesson = {},
            onClickAssignment = {},
        )
    }
}