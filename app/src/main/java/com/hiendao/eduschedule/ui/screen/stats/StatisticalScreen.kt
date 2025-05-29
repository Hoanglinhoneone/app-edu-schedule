package com.hiendao.eduschedule.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.EmptyScreen
import com.hiendao.eduschedule.ui.screen.study.course.EmptyDataView
import com.hiendao.eduschedule.utils.entity.CourseStatsInformation
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticalScreen(
    uiState: UiState = UiState(),
    courseStatsState: CourseStatsState = CourseStatsState.Loading,
    onClickCourse: (CourseStatsInformation) -> Unit = {},
    onFilterSelected: (Int) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, { onRefresh()})
    PullRefreshLayout(state = pullRefreshState) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressCircle(
                            progress = 0.9f,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                        Text(
                            text = stringResource(R.string.current_month, uiState.currentMonth),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LegendItem(
                            color = Color(0xFFFF4081),
                            text = stringResource(R.string.number_of_present, uiState.present)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LegendItem(
                            color = Color(0xFFFFAED0),
                            text = stringResource(R.string.number_of_lesson_absent, uiState.absent)
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.course_process),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Box (
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Filters (
                        modifier = Modifier,
                        onFilterSelected = {
                            onFilterSelected(it)
                        }
                    )
                }
            }

            items(uiState.courseStatsInformation) { course ->
                CourseCard(
                    courseName = course.name ?: "",
                    totalLectures = course.totalScheduleLearning ?: 0,
                    completedTests = course.totalAssignmentCurrent ?: 0,
                    totalTests = course.totalAssignment ?: 0,
                    attended = course.scheduleLearningPresent ?: 0,
                    missed = course.scheduleLearningAbsent ?: 0,

                    status = when (course.reviewSchedule) {
                        "FAIL" -> CourseStatus.POOR
                        "GOOD" -> CourseStatus.EXCELLENT
                        else -> CourseStatus.WARNING
                    },
                    onClickCourse = {
                        onClickCourse(course)
                    }
                )
            }
            item {
                if(uiState.courseStatsInformation.isEmpty()) {
                    EmptyDataView(
                        fraction = 0.7f,
                        text = stringResource(R.string.empty_course_stats)
                    )
                }
            }
        }
    }

}

enum class CourseStatus {
    EXCELLENT, POOR, WARNING
}

@Composable
fun ProgressCircle(progress: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasSize = min(size.width, size.height)
        val radius = canvasSize / 2
        val strokeWidth = canvasSize * 0.1f

        drawArc(
            color = Color(0xFFFFAED0),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = Color(0xFFFF4081),
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Inner white circle
        drawCircle(
            color = Color.White,
            radius = radius * 0.8f,
            center = center
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun CourseCard(
    onClickCourse: () -> Unit,
    courseName: String,
    totalLectures: Int,
    completedTests: Int,
    totalTests: Int,
    attended: Int,
    missed: Int,
    status: CourseStatus
) {
    val textState = when (status) {
        CourseStatus.EXCELLENT -> stringResource(R.string.excellent)
        CourseStatus.POOR -> stringResource(R.string.poor)
        CourseStatus.WARNING -> stringResource(R.string.warning_course)
    }
    val iconStateCourse = when (status) {
        CourseStatus.EXCELLENT -> R.drawable.ic_excellence
        CourseStatus.POOR -> R.drawable.ic_error
        CourseStatus.WARNING -> R.drawable.ic_warning
    }

    val background = Color(0xFFF1F1F1)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickCourse()
            },
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        shape = RoundedCornerShape(28.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = courseName,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(lineHeight = 16.sp),
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(32.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ProgressStat(
                        iconRes = R.drawable.ic_graduation_cap,
                        text = "${attended + missed}/$totalLectures"
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    ProgressStat(
                        iconRes = R.drawable.ic_book,
                        text = "$completedTests/$totalTests",
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (missed + attended).toFloat() / (totalLectures))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF44336))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = attended.toFloat() / totalLectures)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50))
                    )


                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AttendanceIndicator(
                        attended = true,
                        count = attended
                    )
                    AttendanceIndicator(
                        attended = false,
                        count = missed
                    )
                }
            }
            Column(
                modifier = Modifier.padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(iconStateCourse),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    text = textState,
                    fontSize = 12.sp,
                )

            }
        }
    }
}

@Composable
fun ProgressStat(iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            modifier = Modifier
        )
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp).padding(start = 4.dp),
            tint = Color.Black.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun AttendanceIndicator(attended: Boolean, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (attended) Color(0xFF00C07F) else Color(0xFFFF4444))
        )
        Text(
            text = if (attended) stringResource(R.string.present_number, count) else stringResource(
                R.string.absent_number,
                count
            ),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
fun Filters(
    modifier: Modifier = Modifier,
    onFilterSelected: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val selectedChip = rememberSaveable { mutableIntStateOf(0) }
    val listChipItems =
        listOf(
            context.getString(R.string.txt_all),
            context.getString(R.string.done),
            context.getString(R.string.learning)
        )
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(listChipItems.size) { index ->
            FilterChip(
                selected = selectedChip.intValue == index,
                onClick = {
                    selectedChip.intValue = index
                    onFilterSelected(index)
                },
                label = { Text(listChipItems[index]) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun StatisticalPreview() {
    StatisticalScreen()
}
//Row(

@Preview(showBackground = true)
@Composable
fun CourseCardPreview() {
    CourseCard(
        onClickCourse = { /*TODO*/ },
        courseName = "Design Pattern",
        totalLectures = 15,
        completedTests = 4,
        totalTests = 5,
        attended = 9,
        missed = 3,
        status = CourseStatus.EXCELLENT
    )
}

//horizontalArrangement = Arrangement.SpaceBetween,
//verticalAlignment = Alignment.CenterVertically
//) {
//
//    val (statusColor, statusText, bgColor) = when (status) {
//        CourseStatus.EXCELLENT -> Triple(Color(0xFF00C07F), "Tuyệt vời", Color(0xFFE6F7F0))
//        CourseStatus.POOR -> Triple(Color(0xFFFF4444), "Chưa đạt", Color(0xFFFFEAEA))
//        CourseStatus.WARNING -> Triple(Color(0xFFFFAE00), "Chú ý", Color(0xFFFFF8E6))
//    }
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(4.dp))
//            .background(bgColor)
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    ) {
//        Text(
//            text = statusText,
//            color = statusColor,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}