package com.hiendao.eduschedule.ui.screen.study.lessondetail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.component.DialogConfirmDelete
import com.hiendao.eduschedule.ui.component.DialogPickerModel
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.component.OutlineTextFieldCustom
import com.hiendao.eduschedule.ui.component.TimePickerModal
import com.hiendao.eduschedule.ui.screen.study.course.CourseViewModel
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.checkTime
import com.hiendao.eduschedule.utils.convertMillisToDate
import com.hiendao.eduschedule.utils.entity.StateLesson
import timber.log.Timber

@Composable
fun DetailLessonRoute(
    sharedViewModel: CourseViewModel,
    lessonId: Int = 0,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: LessonDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val shareUiState by sharedViewModel.shareUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.updateLesson(shareUiState.lessonSelected)
    }
    DetailLessonScreen(
        updateState = updateState,
        deleteState = deleteState,
        onLessonAction = viewModel::onLessonAction,
        uiState = uiState,
        modifier = modifier
    )
    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            Toast.makeText(context, "Xóa buổi học thành công", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        if (deleteState is DeleteState.Error) {
            Toast.makeText(context, (deleteState as DeleteState.Error).message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLessonScreen(
    updateState: UpdateState,
    onLessonAction: (LessonAction) -> Unit = {},
    deleteState: DeleteState,
    uiState: UiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFocusedLocation by remember { mutableStateOf(false) }

    var showDialogPicker by remember { mutableStateOf(false) }
    var showPickStartTime by remember { mutableStateOf(false) }
    var showPickEndTime by remember { mutableStateOf(false) }
    var showPickStartDay by remember { mutableStateOf(false) }
    var showPickEndDay by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }

    var stateLesson by remember { mutableStateOf(StateLesson.Absent) }
    val tintColor = if (isSystemInDarkTheme()) secondaryLight else secondaryDark
    val textButtonColor = Color.White
    val padHorizontalButton = 8.dp
    val errorColor = MaterialTheme.colorScheme.error
    var showConfirmDelete by remember { mutableStateOf(false) }
    val textButton =
        if (uiState.isFixed) stringResource(R.string.save) else stringResource(R.string.fixed)
    var isFocusSelectedNote by remember { mutableStateOf(false) }
    val hintNote = "Ghi chú"
    val textStyle = MaterialTheme.typography.bodyMedium
    val iconState = when (uiState.stateLesson.name) {
        StateLesson.Present.name -> R.drawable.ic_ass_success
        StateLesson.Absent.name -> R.drawable.ic_error
        else -> null
    }
    var isFocusedTitle by remember { mutableStateOf(false) }
    var showEmptyTitle by remember { mutableStateOf(false) }
    val hintColor =
        if (showEmptyTitle) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    val hintTitle = "Thêm tiêu đề"

    val tintIcon = Color.Black
    val iconSize = 21.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
            ) {
                Row {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.ic_clock),
//                        contentDescription = null,
//                        tint = tintIcon,
//                        modifier = Modifier.size(iconSize)
//                    )
//                    Text(text = stringResource(R.string.time))
//                }
                    Column(
                        modifier = Modifier.padding(bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BasicTextField(
                            value = uiState.name,
                            onValueChange = {
                                onLessonAction.invoke(LessonAction.OnChangeName(it))
                                showEmptyTitle = false
                            },
                            enabled = uiState.isFixed,
                            textStyle = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 16.dp)
                                .fillMaxWidth()
                                .onFocusChanged {
                                    isFocusedTitle = if (it.isFocused) true else false
                                }
                                .align(Alignment.CenterHorizontally),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (uiState.name.isEmpty() && !isFocusedTitle) {
                                        Text(
                                            text = hintTitle,
                                            color = hintColor
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = uiState.startTime,
                                        color = Color.Black, // Đặt màu text thành trắng
                                        modifier = Modifier
                                            .clickable {
                                                if (uiState.isFixed) {
                                                    showPickStartTime = true
                                                }
                                            },
                                        textAlign = TextAlign.End
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color.Transparent, // Nền trong suốt hoặc màu mong muốn
                                    labelColor = Color.White // Đảm bảo màu label là trắng
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color.White
                                )
                            )
                            if (showPickStartTime) {
                                TimePickerModal(
                                    onConfirm = { it ->
                                        onLessonAction.invoke(
                                            LessonAction.OnChangeStartTime(
                                                it.hour.toString()
                                                    .checkTime() + ":" + it.minute.toString()
                                                    .checkTime()
                                            )
                                        )
                                        showPickStartTime = false
                                    },
                                    onDismiss = { showPickStartTime = false }
                                )
                            }
                            HorizontalItemDivider(
                                modifier = Modifier.fillMaxWidth(0.2f),
                                color = Color.White
                            )
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = uiState.endTime,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .clickable {
                                                if (uiState.isFixed) {
                                                    showPickEndTime = true
                                                }
                                            },
                                        textAlign = TextAlign.End
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color.Transparent, // Nền trong suốt hoặc màu mong muốn
                                    labelColor = Color.White // Đảm bảo màu label là trắng
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
//                                color = Color.White // Đặt màu border thành trắng
                                    color = Color.White // Đặt màu border thành trắng

                                )
                            )
                            if (showPickEndTime) {
                                TimePickerModal(
                                    onConfirm = {
                                        onLessonAction.invoke(
                                            LessonAction.OnChangeEndTime(
                                                it.hour.toString()
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
                        Text(
                            text = uiState.startDay,
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            maxLines = 1,
                            modifier = Modifier
//                                .weight(1f)
//                                .padding(start = 10.dp)
                                .clickable {
                                    if (uiState.isFixed) {
                                        showPickStartDay = true
                                    }
                                },
                        )
                        if (showPickStartDay) {
                            DatePickerModal(
                                onDateSelected = {
                                    onLessonAction.invoke(LessonAction.OnChangeStartDay(it.convertMillisToDate()))
                                },
                                onDismiss = { showPickStartDay = false }
                            )
                        }
                    }
                }
            }
            Box(
                modifier = modifier
                    .background(
                        Color.Transparent,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                    .fillMaxHeight(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(19.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 20.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                ) {

//                HorizontalItemDivider()
                    Row(
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(28.dp)
                                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_state_lesosn),
                                contentDescription = null,
                                tint = tintIcon,
                                modifier = Modifier
//                            .size(iconSize)
                            )
                        }
                        Text(
                            text = stringResource(R.string.state),
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                        Row(
                            modifier = Modifier
                                .clickable {
                                    if (uiState.isFixed) {
                                        showDialogPicker = !showDialogPicker
                                    }
                                }
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            iconState?.let {
                                Image(
                                    painter = painterResource(it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(iconSize)
                                )
                            }
                            Text(
                                text = stringResource(uiState.stateLesson.title),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            if (showDialogPicker) {
                                DialogPickerModel(
                                    typeDialog = Constants.TypeDialog.STATE_LESSON,
                                    stateCurrent = stateLesson.name,
                                    onDismissRequest = { showDialogPicker = false },
                                    options = listOf(
                                        StateLesson.Absent.name,
                                        StateLesson.Present.name,
                                        StateLesson.NotYet.name
                                    ),
                                    onConfirm = {
                                        onLessonAction.invoke(
                                            LessonAction.OnChangeStateLesson(
                                                when (it) {
                                                    StateLesson.Absent.name -> StateLesson.Absent
                                                    StateLesson.Present.name -> StateLesson.Present
                                                    else -> StateLesson.NotYet
                                                }
                                            )
                                        )
                                        showDialogPicker = false
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(28.dp)
                                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_location),
                                contentDescription = null,
                                tint = tintIcon,
                                modifier = Modifier
//                            .size(iconSize)
                            )
                        }
                        Text(
                            text = stringResource(R.string.location),
                            modifier = Modifier.padding(vertical = 20.dp)

                        )
                        BasicTextField(
                            value = uiState.address,
                            onValueChange = {
                                onLessonAction.invoke(LessonAction.OnChangeLocation(it))
                            },
                            enabled = uiState.isFixed,
                            textStyle = TextStyle(
                                textAlign = TextAlign.End,
                                fontSize = 16.sp
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp)
                                .onFocusChanged {
                                    isFocusedLocation = if (it.isFocused) true else false
                                },
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.CenterEnd) {
                                    if (uiState.address.isEmpty() && !isFocusedLocation) {
                                        Text(
                                            text = "302-A2",
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
//                HorizontalItemDivider()
                    Column(
                        modifier = Modifier
                            .height(250.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp)),
                    ) {
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(28.dp)
                                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_note),
                                    contentDescription = null,
                                    tint = tintIcon,
                                    modifier = Modifier
//                                .size(iconSize)
                                )
                            }
                            Text(
                                text = stringResource(R.string.note)
                            )
                        }
                        OutlineTextFieldCustom(
                            modifierBox = Modifier
                                .padding(start = 20.dp, end = 8.dp),

                            contentAlignment = Alignment.TopStart,
                            widthBorder = 1.dp,
                            enabled = uiState.isFixed,
                            borderColor = Color.Transparent,
                            shape = RoundedCornerShape(4.dp),
                            value = uiState.note ?: "",
                            onValueChange = {
                                onLessonAction.invoke(LessonAction.OnChangeNote(it))
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
                                    if (uiState.note?.isEmpty() == true && !isFocusSelectedNote) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 19.dp, end = 19.dp)
                ) {
                    if (uiState.isFixed) {
                        OutlinedButton(
                            onClick = {
                                onLessonAction.invoke(LessonAction.OnDelete)
                            },
                            border = BorderStroke(
                                width = 1.dp,
                                color = tintColor
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = tintColor,
                            ),
                            modifier = modifier
                        ) {
                            Text(
                                text = "Hủy",
                                color = tintColor,
                                modifier = Modifier.padding(horizontal = padHorizontalButton)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                showConfirmDelete = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = errorColor,
                                contentColor = textButtonColor,
                            ),
                            modifier = Modifier
                        ) {
                            Text(
                                text = "Xóa",
                                modifier = Modifier.padding(horizontal = padHorizontalButton)
                            )
                        }
                        if (showConfirmDelete) {
                            DialogConfirmDelete(
                                type = "buổi học",
                                title = uiState.name,
                                deleteState = deleteState,
                                onConfirm = {
                                    onLessonAction.invoke(LessonAction.OnDelete)
                                    if (deleteState is DeleteState.Success) {
                                        showConfirmDelete = false
                                    }
                                },
                                onDismiss = {
                                    showConfirmDelete = false
                                }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (uiState.isFixed) {
                                if (!uiState.isNoEmpty) {
                                    showEmptyTitle = true
                                    Toast.makeText(
                                        context,
                                        "Vui lòng đảm bảo tên buổi học và địa điểm không để trống",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                if (!uiState.isValidTime) {
                                    Toast.makeText(
                                        context,
                                        "Vui lòng đảm bảo thời gian bắt đầu bé hơn thời gian kết thúc",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                if(uiState.stateLesson == StateLesson.NotYet && !uiState.isValidState) {
                                    Toast.makeText(
                                        context,
                                        "Thời gian diễn ra buổi học đã qua rồi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                if (uiState.isValid) {
                                    onLessonAction.invoke(LessonAction.OnFix)
                                    showUpdateDialog = true
                                }
                            } else {
                                onLessonAction.invoke(LessonAction.OnFix)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tintColor,
                            contentColor = textButtonColor,
                        ),
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = textButton,
                            modifier = Modifier.padding(horizontal = padHorizontalButton)
                        )
                    }
                    if (showUpdateDialog && updateState !is UpdateState.Pending) {
                        UpdateLessonDialog(
                            updateState = updateState,
                            onDismiss = { },
                            onContinueClick = {
                                onLessonAction.invoke(LessonAction.OnResetState)
                                showUpdateDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateLessonDialog(
    updateState: UpdateState,
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Timber.i("show update course dialog: $updateState")
    val textNotification = when (updateState) {
        is UpdateState.Success -> R.string.update_lesson_success
        is UpdateState.Error -> R.string.update_lesson_error
        else -> R.string.loading_state
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (updateState) {
                    is UpdateState.Success -> {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(secondaryLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    is UpdateState.Error -> {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(secondaryLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Error",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(secondaryLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(16.dp),
                                color = Color.White,
                                strokeWidth = 4.dp
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(
                        when (updateState) {
                            is UpdateState.Success -> R.string.success_state
                            is UpdateState.Error -> R.string.error_state
                            else -> R.string.loading_state
                        }
                    ),
                    color = secondaryLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (updateState != UpdateState.Loading) {
                    Text(
                        text = stringResource(textNotification),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                if (updateState == UpdateState.Loading) {
                    Text(
                        text = "Vui lòng đảm bảo đường truyền mạng ổn định trong lúc hệ thống xử lý...",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryLight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_continue),
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailLessonPreview() {
    EduScheduleTheme {
        DetailLessonScreen(
            updateState = UpdateState.Loading,
            onLessonAction = {},
            deleteState = DeleteState.Loading,
            uiState = UiState()
        )
    }
}