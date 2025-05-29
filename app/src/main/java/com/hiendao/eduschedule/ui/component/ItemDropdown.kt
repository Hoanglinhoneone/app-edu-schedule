package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hiendao.eduschedule.ui.theme.onPrimaryLight
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.entity.Course


@Composable
fun ItemDropdownOne(
    options: List<String> = listOf(
        "Tất cả khhóa học",
        "Giai đoạn 2020 - 2021",
        "Giai đoạn 2021 - 2022",
        "Giai đoạn 2023 - 2024"
    ),
    defaultSelected: String = "Tất cả khóa học",
    onTimeChange: (String) -> Unit = {}
) {
    var onItemSelected by remember { mutableStateOf(defaultSelected) }
    var expanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = onPrimaryLight,
        ),
        modifier = Modifier
            .size(width = 300.dp, height = 50.dp)
            .padding()
            .clickable {
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = onItemSelected,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onItemSelected = option
                        onTimeChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OutlineDropdown(
    options: List<Course> = listOf(
        Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
        Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Hệ thống chuyên sâu nâng cao vip pro "),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Web"),
        Course(1, "3 tín chỉ", "Lập trình Web")
    ),
    defaultCourse: Course = DataSource.course.get(0),
    onCourseChange: (Course) -> Unit = {}
) {
    var onItemSelected by remember { mutableStateOf(defaultCourse) }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(width = 300.dp, height = 50.dp)
            .padding()
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
            .clickable {
                expanded = !expanded
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = onItemSelected.name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = rememberScrollState(),
            modifier = Modifier.size(width = 250.dp, height = 250.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        expanded = false
                        onItemSelected = option
                        onCourseChange(option)
                    }
                )
            }
        }
    }
}
@Composable
fun ItemDropdownTwo(
    listItem: List<Course> = listOf(
        Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
        Course(1, "3 tín chỉ", "Lập trình phần mềm và kiểm thử"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Hệ thống chuyên sâu nâng cao vip pro "),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Android"),
        Course(1, "3 tín chỉ", "Lập trình Web"),
        Course(1, "3 tín chỉ", "Lập trình Web")
    ),
    defaultCourse: Course,
    onCourseChange: (Course) -> Unit = {}
) {
    val mutableList = listItem.toMutableList()
    mutableList.add(0, defaultCourse)
    var onItemSelected by remember { mutableStateOf(defaultCourse) }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(width = 300.dp, height = 50.dp)
            .padding()
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
            .clickable {
                expanded = !expanded
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = onItemSelected.name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = rememberScrollState(),
            modifier = Modifier.size(width = 250.dp, height = 250.dp)
        ) {
            mutableList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        expanded = false
                        onItemSelected = option
                        onCourseChange(option)
                    }
                )
            }
        }
    }
}

