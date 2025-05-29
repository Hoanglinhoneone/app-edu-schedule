package com.hiendao.eduschedule.ui.screen.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.component.ItemDropdownTwo
import com.hiendao.eduschedule.ui.component.TimePickerModal
import com.hiendao.eduschedule.utils.AppConfig
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.checkTime
import com.hiendao.eduschedule.utils.convertMillisToDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    eventUiState: EventUiState,
    modifier: Modifier = Modifier,
    selectedTime: Long = System.currentTimeMillis(),
    tintIcon: Color,
    onEventAction: (EventAction) -> Unit = {}
    ) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Chuyển thời gian millis thành đối tượng Date
    val date = Date(selectedTime)

    // Định dạng ngày tháng theo kiểu "Tháng 1 - 2025"
    val selectedDate = sdf.format(date)
    val currentTime = timeFormat.format(date)

    var showPickStartTime by remember { mutableStateOf(false) }
    var showPickEndTime by remember { mutableStateOf(false) }
    var showPickStartDay by remember { mutableStateOf(false) }
    var showPickEndDay by remember { mutableStateOf(false) }
    val hintCredits = "3"
    val hintFullName = "Nguyễn Văn A"
    var hintAddress = stringResource(R.string.hint_add_address)
    var hintNotes = stringResource(R.string.add_notes)
    val inputFontSize = 18.sp
    val positionInput = TextAlign.End

    LaunchedEffect(true) {
        onEventAction(EventAction.OnChangeStartTime(currentTime))
        onEventAction(EventAction.OnChangeEndTime(currentTime))
        onEventAction(EventAction.OnChangeStartDay(selectedDate))
        onEventAction(EventAction.OnChangeEndDay(selectedDate))
    }

    Column(
        modifier = modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {

        Text(
            text = stringResource(R.string.select_courses)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ItemDropdownTwo(
                listItem = eventUiState.courses,
                defaultCourse = DataSource.course[0],
                onCourseChange = { course ->
//                    if(course.credits.get(0) != '0'){
//                        credits = course.credits
//                    }
//                    if(course.teacher != "Trống"){
//                        fullName = course.teacher
//                    }
//                    if(course.location != "Trống"){
//                        address = course.location
//                    }
//                    notes = course.note
                    onEventAction(EventAction.OnChangeCourses(course))
                }
            )
        }

        Row(
            modifier = Modifier.padding(top = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info), contentDescription = null, Modifier.size(21.dp), tint = tintIcon
            )
            Text(text = stringResource(R.string.infomation))
        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(bottom = 19.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.credits))
                BasicTextField(
                    value = eventUiState.credits,
                    onValueChange = {
                        onEventAction(EventAction.OnChangeCredits(it))
                    },
                    textStyle = TextStyle(
                        textAlign = positionInput,
                        fontSize = inputFontSize

                    ),
                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (eventUiState.credits.isEmpty()) {
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
                Text(text = stringResource(R.string.lecturer))
                BasicTextField(
                    value = eventUiState.teacher,
                    onValueChange = {
                        onEventAction(EventAction.OnChangeTeacher(it))
                    },
                    enabled = true,
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
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (eventUiState.teacher.isEmpty()) {
                                Text(
                                    text = hintFullName,
                                    color = MaterialTheme.colorScheme.outline
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
                painter = painterResource(R.drawable.ic_clock), contentDescription = null, Modifier.size(21.dp), tint = tintIcon
            )
            Text(text = stringResource(R.string.time))
        }
        Column(
            modifier = Modifier.padding(start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.start_time),
                    modifier = Modifier.width(70.dp),
                    fontSize = 14.sp
                )
                Text(
                    text = eventUiState.startDay,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            showPickStartDay = true
                        }
                        .padding(start = 10.dp)
                )
                if (showPickStartDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onEventAction(EventAction.OnChangeStartDay(it.convertMillisToDate()))
//                            startDay = it.convertMillisToDate()
                        },
                        onDismiss = { showPickStartDay = false }
                    )
                }
                Text(
                    text = eventUiState.startTime,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            showPickStartTime = true
                        }
                        .padding(end = 10.dp)
                    , textAlign = TextAlign.End
                )
                if(showPickStartTime) {
                    TimePickerModal(
                        onConfirm = {
                            onEventAction(EventAction.OnChangeStartTime(it.hour.toString().checkTime() + ":" + it.minute.toString().checkTime()))
//                            startTime = it.hour.toString() + ":" + it.minute.toString()
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
                    modifier = Modifier.width(70.dp),
                    fontSize = 14.sp
                )
                Text(
                    text = eventUiState.endDay,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            showPickEndDay = true
                        }
                        .padding(start = 10.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
                if(showPickEndDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onEventAction(EventAction.OnChangeEndDay(it.convertMillisToDate()))
//                            endDay = it.convertMillisToDate()
                        },
                        onDismiss = { showPickEndDay = false }
                    )
                }
                Text(
                    text = eventUiState.endTime, modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showPickEndTime = true }
                        .padding(end = 10.dp),
                    textAlign = TextAlign.End
                )
                if (showPickEndTime) {
                    TimePickerModal(
                        onConfirm = {
                            onEventAction(EventAction.OnChangeEndTime(it.hour.toString().checkTime() + ":" + it.minute.toString().checkTime()))
//                            endTime = it.hour.toString() + ":" + it.minute.toString()
                            showPickEndTime = false
                        },
                        onDismiss = { showPickEndTime = false }
                    )
                }
            }
        }
        HorizontalItemDivider()
        Row(
            modifier = Modifier.padding(top = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location), contentDescription = null, Modifier.size(21.dp), tint = tintIcon
            )

            BasicTextField(
                value = eventUiState.addressEvent,
                onValueChange = {
                    onEventAction(EventAction.OnChangeLocation(it))
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    platformImeOptions = null,
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (eventUiState.addressEvent.isEmpty()) {
                            Text(
                                text = hintAddress,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        HorizontalItemDivider()

        Row(
            modifier = Modifier.padding(top = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = "Add Notes", Modifier.size(21.dp), tint = tintIcon
            )

            BasicTextField(
                value = eventUiState.note,
                onValueChange = {
                    onEventAction(EventAction.OnChangeNote(it))
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
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
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (eventUiState.note.isEmpty()) {
                            Text(
                                text = hintNotes,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}