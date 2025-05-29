package com.hiendao.eduschedule.ui.screen.study.course

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.primaryLight
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.entity.Course

@Composable
fun ItemCourse(
    onClickCourse: (Int) -> Unit = {},
    course: Course,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .size(width = 120.dp, height = 110.dp)
            .padding(8.dp)
            .clickable {
                onClickCourse(course.id)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(
                        color = secondaryLight,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
            )
            Box() {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    // line 1
                    val path1 = Path().apply {
                        // start point
                        moveTo(0f, height)
                        quadraticBezierTo(
                            // down point
                            width * 0.1f, height * 0.8f,
                            // end point
                            width * 0.2f, height * 1f
                        )
                    }

                    // line 2:
                    val path2 = Path().apply {
                        // padding with line one
                        moveTo(0f, height - 20.dp.toPx())
                        quadraticBezierTo(
                            width * 0.15f, height * 0.75f,
                            width * 0.25f, height * 1f
                        )
                    }
                    drawPath(
                        path = path1,
                        color = Color(0xFFADD8E6),
                        style = Stroke(width = 1.dp.toPx())
                    )

                    drawPath(
                        path = path2,
                        color = Color(0xFFADD8E6),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = primaryLight,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "${course.credits} tín chỉ" ,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    Text(
                        text = course.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_book_open),
                        contentDescription = null,
                        tint = secondaryLight.copy(alpha = 0.7f),
                    )
                    Text(
                        text = "${course.numberOfLesson} buổi học",
                        style = TextStyle(
                            color = secondaryLight.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ItemCoursePreview() {
    EduScheduleTheme {
        ItemCourse(
            course = Course(
                id = 3,
                name = "Công nghệ phần mềm hoàng ngọc linh 123",
                credits = "3 tín chỉ",
            )
        )
    }
}
