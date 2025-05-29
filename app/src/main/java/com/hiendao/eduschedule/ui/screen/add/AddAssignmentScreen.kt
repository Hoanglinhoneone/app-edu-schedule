package com.hiendao.eduschedule.ui.screen.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.component.OutlineDropdown
import com.hiendao.eduschedule.ui.component.OutlineTextFieldCustom
import com.hiendao.eduschedule.ui.component.TimePickerModal
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.checkTime
import com.hiendao.eduschedule.utils.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(
    tintIcon: Color,
    assignmentUiState: AssignmentUiState,
    onAssignmentAction: (AssignmentAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocusSelectedNote by remember { mutableStateOf(false) }
    val hintNote = stringResource(R.string.hint_note)

    var showPickEndTime by remember { mutableStateOf(false) }
    var showPickEndDay by remember { mutableStateOf(false) }

    val iconSize = 21.dp
    val textStyle = MaterialTheme.typography.bodyMedium
    Column(
        modifier = modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = stringResource(R.string.infomation_course)
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlineDropdown(
                options = assignmentUiState.courses,
                onCourseChange = {
                    onAssignmentAction.invoke(AssignmentAction.OnChangeCourseId(it.id))
                }
            )
        }
        Row(
            modifier = Modifier.padding(top = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock), contentDescription = null,
                tint = tintIcon,
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = stringResource(R.string.time),
            )
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
                    text = stringResource(R.string.end_time),
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = assignmentUiState.endDay,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showPickEndDay = true
                        },
                )
                if (showPickEndDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onAssignmentAction.invoke(AssignmentAction.OnChangeEndDay(it.convertMillisToDate()))
                        },
                        onDismiss = { showPickEndDay = false }
                    )
                }
                Text(
                    text = assignmentUiState.endTime,
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable {
                            showPickEndTime = true
                        }, textAlign = TextAlign.End
                )
                if (showPickEndTime) {
                    TimePickerModal(
                        onConfirm = {
                            onAssignmentAction.invoke(AssignmentAction.OnChangeEndTime(it.hour.toString().checkTime() + ":" + it.minute.toString()
                                .checkTime()))
                            showPickEndTime = false
                        },
                        onDismiss = { showPickEndTime = false }
                    )
                }
            }
        }
        HorizontalDivider()
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
            borderColor = Color.Transparent,
            shape = RoundedCornerShape(4.dp),
            value = assignmentUiState.note,
            onValueChange = {
                onAssignmentAction.invoke(AssignmentAction.OnChangeNote(it))
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
                    if (assignmentUiState.note.isEmpty() && !isFocusSelectedNote) {
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
fun AddAssignmentScreenPreview() {
    AddAssignmentScreen(
        assignmentUiState = AssignmentUiState(),
        onAssignmentAction = {},
        tintIcon = Color.Black,
        )
}