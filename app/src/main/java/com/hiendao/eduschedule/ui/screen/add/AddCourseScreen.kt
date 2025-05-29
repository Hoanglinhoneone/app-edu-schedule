package com.hiendao.eduschedule.ui.screen.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.component.DayPickerModal
import com.hiendao.eduschedule.ui.component.DialogPickerModel
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.component.OutlineTextFieldCustom
import com.hiendao.eduschedule.ui.component.TimePickerModal
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.checkTime
import com.hiendao.eduschedule.utils.convertMillisToDate
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.Repeat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    startType: Int = Constants.ADD_COURSE,
    enable: Boolean = true,
    courseUiState: CourseUiState = CourseUiState(),
    onCourseAction: (CourseAction) -> Unit = {},
    tintIcon: Color,
    modifier: Modifier = Modifier
) {

    val isDarkMode = isSystemInDarkTheme()
    var isFocusedCredits by remember { mutableStateOf(false) }
    var isFocusedFullName by remember { mutableStateOf(false) }
    var isFocusedLocation by remember { mutableStateOf(false) }
    var isFocusSelectedNote by remember { mutableStateOf(false) }

    var showPickStartTime by remember { mutableStateOf(false) }
    var showPickEndTime by remember { mutableStateOf(false) }
    var showPickStartDay by remember { mutableStateOf(false) }
    var showPickEndDay by remember { mutableStateOf(false) }
    var showPickRepeat by remember { mutableStateOf(false) }

    var hintColor = if (courseUiState.showErrorEmpty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    val hintCredits = stringResource(R.string.txt_number_of_credits)
    val hintFullName = stringResource(R.string.hint_name_teacher)
    val hintNote = stringResource(R.string.hint_note)

    val inputFontSize = 18.sp
    val positionInput = TextAlign.End
    val textStyle = MaterialTheme.typography.bodyMedium
    val iconSize = 21.dp

    val repeats = listOf(
        Repeat.Daily.title, Repeat.Weekly.title,
        Repeat.Monthly.title, Repeat.Yearly.title,
        Repeat.None.title
    )
    Column(
        modifier = modifier
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = stringResource(R.string.infomation),

                )
        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.credits),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                BasicTextField(
                    value = courseUiState.credits,
                    onValueChange = {
                        onCourseAction.invoke(CourseAction.OnChangeCredits(it))
                    },
                    enabled = enable,
                    textStyle = TextStyle(
                        textAlign = positionInput,
                        fontSize = inputFontSize

                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocusedCredits = if (it.isFocused) true else false
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (courseUiState.credits.isEmpty() && !isFocusedCredits) {
                                Text(
                                    text = hintCredits,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.lecturer),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                BasicTextField(
                    value = courseUiState.teacher,
                    onValueChange = {
                        onCourseAction.invoke(CourseAction.OnChangeTeacher(it))
                        onCourseAction.invoke(CourseAction.OnChangeErrorEmpty(false))
                    },
                    enabled = enable,
                    singleLine = true,
                    textStyle = TextStyle(
                        textAlign = positionInput,
                        fontSize = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrectEnabled = true,
                        platformImeOptions = null,
                        keyboardType = KeyboardType.Text,
                        showKeyboardOnFocus = true,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocusedFullName = if (it.isFocused) true else false
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (courseUiState.teacher.isEmpty() && !isFocusedFullName) {
                                Text(
                                    text = hintFullName,
                                    color = hintColor
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.padding(top = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.location),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                BasicTextField(
                    value = courseUiState.addressLearning,
                    onValueChange = {
                        onCourseAction.invoke(CourseAction.OnChangeLocation(it))
                        onCourseAction.invoke(CourseAction.OnChangeErrorEmpty(false))
                    },
                    enabled = enable,
                    textStyle = TextStyle(
                        textAlign = TextAlign.End,
                        fontSize = inputFontSize
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocusedLocation = if (it.isFocused) true else false
                        },
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterEnd) {
                            if (courseUiState.addressLearning.isEmpty() && !isFocusedLocation) {
                                Text(
                                    text = "302-A2",
                                    color = hintColor
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        HorizontalItemDivider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(text = stringResource(R.string.time))
        }
        Column(
            modifier = Modifier.padding(start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.start_time),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = courseUiState.startDay,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if(startType == Constants.ADD_COURSE) {
                                showPickStartDay = true
                            }
                        },
                )
                if (showPickStartDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onCourseAction.invoke(
                                CourseAction.OnChangeStartDay(
                                    it.convertMillisToDate()
                                )
                            )
                        },
                        onDismiss = { showPickStartDay = false }
                    )
                }

            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.end_time),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = courseUiState.endDay,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if(startType == Constants.ADD_COURSE) {
                                showPickEndDay = true
                            }
                        },
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
                if (showPickEndDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onCourseAction.invoke(
                                CourseAction.OnChangeEndDay(
                                    it.convertMillisToDate()
                                )
                            )
                        },
                        onDismiss = { showPickEndDay = false }
                    )
                }
            }
        }

        HorizontalItemDivider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_repeat), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(text = stringResource(R.string.lesson_info))
        }
        Column(
            modifier = Modifier.padding(start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = stringResource(R.string.repeat),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = stringResource(courseUiState.repeatType.title),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if(startType == Constants.ADD_COURSE) {
                                showPickRepeat = true
                            }
                        },
                    textAlign = TextAlign.End
                )
                if (showPickRepeat) {
                    DialogPickerModel(
                        typeDialog = Constants.TypeDialog.REPEAT,
                        stateCurrent = courseUiState.repeatType.name,
                        options = listOf(
                            Repeat.Daily.name,
                            Repeat.Weekly.name,
                            Repeat.Monthly.name,
//                            Repeat.Yearly.name,
//                            Repeat.None.name
                        ),
                        onDismissRequest = { showPickRepeat = false },
                        onConfirm = {
                            onCourseAction.invoke(
                                CourseAction.OnChangeRepeatType(
                                    when (it) {
                                        Repeat.Daily.name -> Repeat.Daily
                                        Repeat.Weekly.name -> Repeat.Weekly
                                        Repeat.Monthly.name -> Repeat.Monthly
                                        Repeat.Yearly.name -> Repeat.Yearly
                                        else -> Repeat.None
                                    }
                                )
                            )
                            showPickRepeat = false
                        }
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (courseUiState.repeatType) {
                    Repeat.Daily -> {
                        DayPickerModal(
                            type = Constants.TypeDay.DAY,
                        )
                    }
                    Repeat.Weekly -> {
                        DayPickerModal(
                            type = Constants.TypeDay.WEEK,
                            days = courseUiState.dayOfWeeks,
                            onChangeDay = {
                                if(startType == Constants.ADD_COURSE) {
                                    onCourseAction.invoke(CourseAction.OnChangeDayOfWeek(it))
                                }
                            }
                        )
                    }
                    Repeat.Monthly -> {
                        DayPickerModal(
                            type = Constants.TypeDay.MONTH,
                            dayOfMonths = courseUiState.dayOfMonths,
                            onChangeDayOfMonth = {
                                if(startType == Constants.ADD_COURSE) {
                                    onCourseAction.invoke(CourseAction.OnChangeDayOfMonth(it))
                                }
                            }
                        )
                    }
                    Repeat.Yearly -> {}
                    Repeat.None -> {}
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.start_time),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = courseUiState.lessonStartTime,
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable {
                            if(startType == Constants.ADD_COURSE) {
                                showPickStartTime = true
                            }
                        }, textAlign = TextAlign.End
                )
                if (showPickStartTime) {
                    TimePickerModal(
                        onConfirm = {
                            onCourseAction.invoke(
                                CourseAction.OnChangeStartTime(
                                    it.hour.toString().checkTime() + ":" + it.minute.toString()
                                        .checkTime()
                                )
                            )
                            showPickStartTime = false
                        },
                        onDismiss = { showPickStartTime = false }
                    )
                }

            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.end_time),
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = courseUiState.lessonEndTime, modifier = Modifier
                        .weight(0.5f)
                        .clickable {
                            if(startType == Constants.ADD_COURSE) {
                                showPickEndTime = true
                            }
                        }, textAlign = TextAlign.End
                )
                if (showPickEndTime) {
                    TimePickerModal(
                        onConfirm = {
                            onCourseAction.invoke(
                                CourseAction.OnChangeEndTime(
                                    endTime = it.hour.toString()
                                        .checkTime() + ":" + it.minute.toString()
                                        .checkTime()
                                )
                            )
                            showPickEndTime = false
                        },
                        onDismiss = { showPickEndTime = false }
                    )
                }
            }
        }
        HorizontalItemDivider()


        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_note), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = stringResource(R.string.note)
            )
        }
        OutlineTextFieldCustom(
            modifierBox = Modifier
                .padding(start = 20.dp)
                .height(60.dp),
            contentAlignment = Alignment.TopStart,
            widthBorder = 1.dp,
            enabled = enable,
            borderColor = Color.Transparent,
            shape = RoundedCornerShape(4.dp),
            value = courseUiState.note,
            onValueChange = {
                onCourseAction.invoke(CourseAction.OnChangeNote(it))
            },
            maxLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(8.dp)
                .onFocusChanged {
                    isFocusSelectedNote = if (it.isFocused) true else false
                }
                .align(Alignment.Start)
                .fillMaxSize(),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.TopStart
                ) {
                    if (courseUiState.note.isEmpty() && !isFocusSelectedNote) {
                        Text(
                            text = hintNote,
                            color = MaterialTheme.colorScheme.outline,
                            style = textStyle,
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AddCoursePreview(modifier: Modifier = Modifier) {
    MaterialTheme {
//        AddCourseScreen(
//            tintIcon = secondaryLight
//        )
    }
}

