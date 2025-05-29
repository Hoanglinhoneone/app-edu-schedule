package com.hiendao.eduschedule.ui.screen.study.assigndetail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.hiendao.eduschedule.ui.component.OutlineTextFieldCustom
import com.hiendao.eduschedule.ui.component.TimePickerModal
import com.hiendao.eduschedule.ui.screen.study.assignment.CustomCheckbox
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.ui.theme.errorLight
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.checkTime
import com.hiendao.eduschedule.utils.convertMillisToDate
import com.hiendao.eduschedule.utils.entity.StateAssignment
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import timber.log.Timber

@Composable
fun AssignmentDetailRoute(
    navController: NavController,
    assignmentId: Int,
    modifier: Modifier = Modifier
) {
    val viewModel: AssignmentDetailViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val assignmentState by viewModel.assignmentState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.initAssignment(assignmentId)
    }

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            Toast.makeText(context, "Xóa nhiệm vụ thành công", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel::onAssignmentAction.invoke(AssignmentDetailAction.OnResetState)
        }
        if (deleteState is DeleteState.Error) {
            Toast.makeText(context, (deleteState as DeleteState.Error).message, Toast.LENGTH_SHORT)
                .show()
        }
    }
    LaunchedEffect(assignmentState) {
        if (assignmentState is AssignmentDetailState.Error) {
            Toast.makeText(
                context,
                (assignmentState as AssignmentDetailState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    AssignmentDetailScreen(
        onAssignmentAction = {
            viewModel::onAssignmentAction.invoke(it)
        },
        uiState = uiState,
        assignmentState = assignmentState,
        deleteState = deleteState,
        updateState = updateState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentDetailScreen(
    onAssignmentAction: (AssignmentDetailAction) -> Unit,
    uiState: UiState,
    assignmentState: AssignmentDetailState,
    deleteState: DeleteState,
    onRefresh: () -> Unit = {},
    updateState: UpdateAssignmentState,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, { onRefresh() })
    val state = when (uiState.state) {
        "COMPLETE" -> StateAssignment.Complete
        "INCOMPLETE" -> StateAssignment.Incomplete
        else -> StateAssignment.Overdue
    }
    val context = LocalContext.current
    var showUpdateDialog by remember { mutableStateOf(false) }
    val textButton =
        if (uiState.isFixed) stringResource(R.string.save) else stringResource(R.string.fixed)
    val tintColor = if (isSystemInDarkTheme()) secondaryLight else secondaryDark
    val textButtonColor = Color.White
    val padHorizontalButton = 8.dp
    val errorColor = MaterialTheme.colorScheme.error
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showPickEndDay by remember { mutableStateOf(false) }
    var showPickEndTime by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodyMedium
    val hintNote = "Ghi chú"
    var isFocusSelectedNote by remember { mutableStateOf(false) }
    val borderColor = if (isSystemInDarkTheme()) secondaryLight else secondaryDark
    val textTimeColor = if (state == StateAssignment.Overdue) Color.Red else Color.Black
    val tintIcon = when (state) {
        StateAssignment.Complete -> Color.Black
        StateAssignment.Incomplete -> Color.Black
        StateAssignment.Overdue -> errorLight
        else -> Color.Black
    }
    var showEmptyTitle by remember { mutableStateOf(false) }
    var isFocusedTitle by remember { mutableStateOf(false) }
    val hintColor =
        if (showEmptyTitle) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    val hintTitle = "Thêm tiêu đề"


    PullRefreshLayout(pullRefreshState) {


        Box(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
        ) {
//            Image(
//                painter = painterResource(R.drawable.img_background_course_detail),
//                contentDescription = "",
//                alpha = 1f,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
//                Text(
//                    text = uiState.name,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.align(Alignment.Center)
//                )
                    BasicTextField(
                        value = uiState.name,
                        onValueChange = {
                            onAssignmentAction.invoke(AssignmentDetailAction.OnChangeTitle(it))
                            showEmptyTitle = false
                        },
                        enabled = uiState.isFixed,
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
//                color = Color.White,
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
                            .align(Alignment.Center),
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
                }

                Spacer(modifier = Modifier.height(32.dp))

                AppItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_state_lesosn),
                            contentDescription = "State assignment",
                            tint = Color.Black
                        )
                    },
                    title = stringResource(R.string.state),
                    subtitle = "",
                    trailingIcon = {
                        CustomCheckbox(
                            onChangeState = {
                                if (uiState.isFixed) onAssignmentAction.invoke(
                                    AssignmentDetailAction.OnChangeState
                                )
                            },
                            state = state,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppItem(
                    isFixed = uiState.isFixed,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = "Clock",
                            tint = Color.Black
                        )
                    },
                    title = "Thời hạn",
                    subtitle = uiState.endDay,
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.endTime,
                                modifier = Modifier.clickable {
                                    if (uiState.isFixed) {
                                        showPickEndTime = true
                                    }
                                }
                            )
                        }
                        if (showPickEndTime) {
                            TimePickerModal(
                                onConfirm = { it ->
                                    onAssignmentAction.invoke(
                                        AssignmentDetailAction.OnChangeEndTime(
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
                    },
                    onClickSubtitle = {
                        if (uiState.isFixed) showPickEndDay = true
                    }
                )
                if (showPickEndDay) {
                    DatePickerModal(
                        onDateSelected = {
                            onAssignmentAction.invoke(AssignmentDetailAction.OnChangeEndDay(it.convertMillisToDate()))
                        },
                        onDismiss = { showPickEndDay = false }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .height(250.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    Row(
                        modifier = Modifier.padding(top = 12.dp, start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
//                    Icon(
//                        painter = painterResource(R.drawable.ic_note),
//                        contentDescription = null,
//                        tint = tintIcon,
//                        modifier = Modifier
//                                .size(iconSize)
//                            .padding(start = 8.dp)
//
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    Color.LightGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_note),
                                contentDescription = "note",
                                tint = Color.Black
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
                            onAssignmentAction.invoke(AssignmentDetailAction.OnChangeNote(it))
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
                                if (uiState.note.isEmpty() && !isFocusSelectedNote) {
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
                            onAssignmentAction.invoke(AssignmentDetailAction.OnDelete)
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
                                onAssignmentAction.invoke(AssignmentDetailAction.OnDelete)
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
                        if(uiState.isFixed) {
                            if (uiState.isEmpty) {
                                Toast.makeText(context, "Vui lòng nhập tên bài tập", Toast.LENGTH_SHORT)
                                    .show()
                                showEmptyTitle = true
                            } else {
                                onAssignmentAction.invoke(AssignmentDetailAction.OnFix)
                                showUpdateDialog = true
                            }
//                    if(!uiState.isValidDateTime) {
//                        Toast.makeText(context, "Thời gian tới hạn đang nhỏ hơn thời điểm", Toast.LENGTH_SHORT).show()
//                    }
                        } else {
                            onAssignmentAction.invoke(AssignmentDetailAction.OnFix)
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
                if (showUpdateDialog && updateState !is UpdateAssignmentState.Pending) {
                    UpdateAssignmentDialog(
                        updateState = updateState,
                        onDismiss = { },
                        onContinueClick = {
                            onAssignmentAction.invoke(AssignmentDetailAction.OnResetState)
                            showUpdateDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AssignmentDetailScreenPreview() {
    val uiState = UiState(
        id = "1",
        name = "Assignment 1",
        note = "Some notes",
        endDay = "30/4/1975",
        endTime = "10:00",
        state = StateAssignment.Complete.name,
        courseId = "1",
    )
    val assignmentState =
        AssignmentDetailState.Success(com.hiendao.eduschedule.utils.entity.Assignment())
    val deleteState = DeleteState.Pending
    val updateState = UpdateAssignmentState.Pending
    AssignmentDetailScreen(
        onAssignmentAction = {},
        uiState = uiState,
        assignmentState = assignmentState,
        deleteState = deleteState,
        updateState = updateState
    )
}

@Composable
fun AppItem(
    isFixed: Boolean = false,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    trailingIcon: @Composable (() -> Unit)?,
    onClickSubtitle: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable { onClickSubtitle() }
                    ) {
                        Text(
                            text = subtitle,
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                        if (isFixed) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_selected),
                                contentDescription = "Lock",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}

@Composable
fun UpdateAssignmentDialog(
    updateState: UpdateAssignmentState,
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Timber.i("show update assignment dialog: $updateState")
    val textNotification = when (updateState) {
        is UpdateAssignmentState.Success -> R.string.update_assignment_success
        is UpdateAssignmentState.Error -> R.string.update_assignment_error
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
                    is UpdateAssignmentState.Success -> {
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

                    is UpdateAssignmentState.Error -> {
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
                            is UpdateAssignmentState.Success -> R.string.success_state
                            is UpdateAssignmentState.Error -> R.string.error_state
                            else -> R.string.loading_state
                        }
                    ),
                    color = secondaryLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (updateState != UpdateAssignmentState.Loading) {
                    Text(
                        text = stringResource(textNotification),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                if (updateState == UpdateAssignmentState.Loading) {
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
