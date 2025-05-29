package com.hiendao.eduschedule.ui.screen.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.utils.entity.CourseStatsInformation
import com.hiendao.eduschedule.utils.entity.ScheduleLearning
import com.hiendao.eduschedule.utils.getTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCourseStatScreen(
    courseStatsInformation: CourseStatsInformation,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = {
            onBack()
        },
        containerColor = Color.White,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        sheetMaxWidth = Dp.Unspecified,
        shape = RoundedCornerShape(0.dp)
    ) {
        CourseDetailView(
            courseStatsInformation = courseStatsInformation,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailView(
    courseStatsInformation: CourseStatsInformation,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = courseStatsInformation.name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(modifier = Modifier.padding(top = 12.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Tiến trình môn học
            CourseInfoRow(
                title = stringResource(R.string.course_progress),
                value = "${courseStatsInformation.scheduleLearningPresent + courseStatsInformation.scheduleLearningAbsent}/${courseStatsInformation.totalScheduleLearning}"
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Số buổi vắng
            AbsenceRow(
                lessons = courseStatsInformation.scheduleLearningList,
                title = stringResource(R.string.txt_absent_number),
                value = courseStatsInformation.scheduleLearningAbsent.toString(),
                courseName = courseStatsInformation.name,
                state = courseStatsInformation.reviewSchedule
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Tiến trình bài tập
            CourseInfoRow(
                title = stringResource(R.string.assignment_process),
                value = "${courseStatsInformation.totalAssignmentCurrent}/${courseStatsInformation.totalAssignment}"
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Số bài tập đã nộp trễ/thiếu
            ItemRow(
                title = stringResource(R.string.number_of_assignment_miss),
                value = courseStatsInformation.assignmentOverdue.toString(),
                state = courseStatsInformation.reviewAssignment
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Đánh giá hiệu suất chung
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.overall_performance_review),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${courseStatsInformation.percentReviewAll}%",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
            Text(
                text = "(${courseStatsInformation.textReview})",
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CourseInfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun AbsenceRow(
    lessons: List<ScheduleLearning>,
    title: String, value: String,
    courseName: String,
    state: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ItemRow(title = title, value = value, state = state)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lessons) { lesson ->
                Text(
                    text = lesson.name,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                ScheduleCard(schedule = lesson, courseName = courseName)
            }
        }
    }
}

@Composable
fun ScheduleCard(schedule: ScheduleLearning, courseName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F1F1)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vertical colored line
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(35.dp)
                    .background(Color(0xFFDA47F0))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Time and content
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Time range
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${schedule.timeStart.getTime()} - ${schedule.timeEnd.getTime()}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = schedule.learningAddresses,
                            color = Color.Black.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Course name or lesson title
                Text(
                    text = courseName.ifEmpty { "Đảm bảo chất lượng phần mềm" },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}


@Composable
fun ItemRow(title: String, value: String, state: String) {

    val icon = when (state) {
        "FAIL" -> R.drawable.ic_error
        "GOOD" -> R.drawable.ic_excellence
        else -> R.drawable.ic_warning
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image (
                painter = painterResource(icon),
                contentDescription = "state",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ScheduleCardPreview() {
    MaterialTheme {
        ScheduleCard(
            schedule = ScheduleLearning(
                id = 0,
                name = "Lesson 1",
                timeStart = "02/01/2025T14:00:00",
                timeEnd = "02/01/2025T16:00:00",
                teacher = "Mr. A",
                learningAddresses = "Room 101",
                state = "",
                note = "",
                courseId = 1
            ),
            courseName = "Lập trình Android"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CourseDetailScreenPreview() {
    EduScheduleTheme {
        CourseDetailView(
            courseStatsInformation = CourseStatsInformation(
                id = 1,
                name = "Lập trình Android",
                assignmentOverdue = 0,
                scheduleLearningAbsent = 0,
                totalScheduleLearning = 12,
                totalAssignment = 8,
                totalAssignmentCurrent = 3,
                percentReviewAll = 56,
                textReview = "Không đạt số buổi học tham gia yêu cầu!"
            )
        )
    }
}
