package com.hiendao.eduschedule.ui.screen.study.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.entity.StateLesson
import com.hiendao.eduschedule.utils.getTime

@Composable
fun LessonScreen(
    onClickLesson: (Lesson) -> Unit,
    listLesson: List<Lesson> = emptyList(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(listLesson) { lesson ->
            ItemLesson(
                lesson = lesson,
                onClickLesson = { onClickLesson(lesson) }
            )
        }
    }
}

@Composable
fun ItemLesson(
    lesson: Lesson,
    onClickLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = when (lesson.state) {
        StateLesson.Present.converter -> StateLesson.Present
        StateLesson.Absent.converter -> StateLesson.Absent
        else -> StateLesson.NotYet
    }
    Box(
        modifier = Modifier.clickable {
            onClickLesson()
        }
    ) {
        BoxLesson(
            lesson = lesson,
            modifier = modifier
        )
        StateLesson(
            stateLesson = state,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
fun BoxLesson(
    lesson: Lesson,
    tintColor: Color = secondaryLight,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pen_note),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column(

            ) {
                Text(
                    "${lesson.name}",
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row() {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
//                        tint = tintColor
                        )
                        Text(
                            text = lesson.startTime.getTime() + " - " + lesson.endTime.getTime(),
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_location),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
//                        tint = tintColor
                        )
                        Text(
                            text = lesson.location,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StateLesson(
    stateLesson: StateLesson,
    modifier: Modifier = Modifier
) {
    val stateContent = when (stateLesson.name) {
        "Present" -> stringResource(StateLesson.Present.title)
        "Absent" -> stringResource(StateLesson.Absent.title)
        else -> stringResource(StateLesson.NotYet.title)
    }

    val backgroundColor = when (stateLesson.name) {
        StateLesson.Present.name -> Color(0xFFD9FCE4)
        StateLesson.Absent.name -> Color(0xFFFCE2E3)
        else -> Color(0xFFEFEFEF)
    }

    val borderColor = when (stateLesson.name) {
        StateLesson.Present.name -> Color(0xFF04381A)
        StateLesson.Absent.name -> MaterialTheme.colorScheme.error
        else -> Color(0xFF400303)
    }

    val contentColor = when (stateLesson.name) {
        StateLesson.Present.name -> Color(0xFF0F5134)
        StateLesson.Absent.name -> Color(0xFF7E1824)
        else -> Color(0xFF616161)
    }
    Box(
        modifier = modifier
            .size(width = 90.dp, height = 30.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
            .border(1.dp, borderColor.copy(alpha = 0.3f), shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(stateLesson.title),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LessonPreview() {
    EduScheduleTheme {
        LessonScreen(
            onClickLesson = {},
            listLesson = DataSource.lesson
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemLessonPreview() {
    EduScheduleTheme {
        ItemLesson(
            onClickLesson = {},
            lesson = Lesson(
                name = "buoi hoc 123",
                startTime = "30/10/2003",
                endTime = "13/10/2003",
                location = "301 A2, BCVT"
            )
        )
    }
}
