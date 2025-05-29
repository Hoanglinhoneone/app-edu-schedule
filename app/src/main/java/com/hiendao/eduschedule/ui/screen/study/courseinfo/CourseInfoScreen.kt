package com.hiendao.eduschedule.ui.screen.study.courseinfo

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.hiendao.eduschedule.ui.component.DialogConfirmDelete
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.screen.add.AddAction
import com.hiendao.eduschedule.ui.screen.add.AddCourseScreen
import com.hiendao.eduschedule.ui.screen.add.CourseAction
import com.hiendao.eduschedule.ui.screen.add.CourseUiState
import com.hiendao.eduschedule.ui.screen.study.course.CourseViewModel
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.Constants
import timber.log.Timber

@Composable
fun CourseInfoRoute(
    navController: NavController,
    shareViewModel: CourseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val shareUiState by shareViewModel.shareUiState.collectAsStateWithLifecycle()
    val viewModel: CourseInfoViewModel = hiltViewModel()
    Timber.i("CourseInfo: ${shareUiState.courseDetail}")
    LaunchedEffect(viewModel, context) {
        viewModel.updateCourseInfo(shareUiState.courseDetail)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val courseUiState by viewModel.courseUiState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    CourseInfoScreen(
        deleteState = deleteState,
        updateState = updateState,
        onCourseAction = { viewModel::onCourseAction.invoke(it) },
        onCourseInfoAction = { viewModel::onCourseInfoAction.invoke(it) },
        uiState = uiState,
        courseUiState = courseUiState,
        modifier = Modifier
    )
    LaunchedEffect(deleteState) {
        if (deleteState is DeleteState.Success) {
            Toast.makeText(context, "Xóa buổi học thành công", Toast.LENGTH_SHORT).show()
            navController.navigate(route = AppScreen.Courses.route) {
                shareViewModel.refreshCourses()
                popUpTo(AppScreen.Courses.route) { inclusive = true }
                launchSingleTop = true
                viewModel::onCourseInfoAction.invoke(CourseInfoAction.OnResetState)

            }
        }
    }
}

@Composable
fun CourseInfoScreen(
    deleteState: DeleteState,
    updateState: UpdateState,
    onCourseAction: (CourseAction) -> Unit = {},
    onCourseInfoAction: (CourseInfoAction) -> Unit = {},
    uiState: UiState = UiState(),
    courseUiState: CourseUiState = CourseUiState(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showEmptyTitle by remember { mutableStateOf(false) }
    val hintTitle = stringResource(R.string.add_course_name)
    val hintColor =
        if (showEmptyTitle) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    var isFocusedTitle by remember { mutableStateOf(false) }
    val tintColor = if (isSystemInDarkTheme()) secondaryLight else secondaryDark
    val textButtonColor = Color.White
    val padHorizontalButton = 8.dp
    val errorColor = MaterialTheme.colorScheme.error
    var showConfirmDelete by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    val textButton =
        if (uiState.isFixed) stringResource(R.string.save) else stringResource(R.string.fixed)
    Column(
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = uiState.name,
                enabled = uiState.isFixed,
                onValueChange = {
                    onCourseInfoAction.invoke(CourseInfoAction.OnChangeName(it))
                    showEmptyTitle = false
                },
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
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocusedTitle = if (it.isFocused) true else false
                    },
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
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalItemDivider(modifier = Modifier.padding(horizontal = 50.dp))
            Spacer(modifier = Modifier.height(12.dp))
        }
        AddCourseScreen(
            startType = Constants.UPDATE_COURSE,
            tintIcon = tintColor,
            courseUiState = courseUiState,
            onCourseAction = onCourseAction,
            enable = uiState.isFixed,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 19.dp, end = 19.dp)
        ) {
            if (uiState.isFixed) {
                OutlinedButton(
                    onClick = {
                        onCourseInfoAction.invoke(CourseInfoAction.OnDelete)
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
                        text = stringResource(R.string.cancel),
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
                        text = stringResource(R.string.delete),
                        modifier = Modifier.padding(horizontal = padHorizontalButton)
                    )
                }
                if (showConfirmDelete) {
                    DialogConfirmDelete(
                        title = uiState.name,
                        deleteState = deleteState,
                        onConfirm = {
                            onCourseInfoAction.invoke(CourseInfoAction.OnDelete)
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
                        if(!courseUiState.isNoEmpty) {
                            Timber.i("courseUiState: $courseUiState")
                            showEmptyTitle = true
                            onCourseAction.invoke(CourseAction.OnChangeErrorEmpty(true))
                            Toast.makeText(context,
                                context.getString(R.string.message_warning_course), Toast.LENGTH_SHORT).show()
                        }
                        if(!courseUiState.isValidDay) {
                            Toast.makeText(context,
                                context.getString(R.string.message_error_date), Toast.LENGTH_SHORT).show()
                        }
                        if(!courseUiState.isValidTime) {
                            Toast.makeText(context,
                                context.getString(R.string.message_error_time), Toast.LENGTH_SHORT).show()
                        }
                        if(uiState.isValid && courseUiState.isValid) {
                            onCourseInfoAction.invoke(CourseInfoAction.OnUpdate)
                            showUpdateDialog = true
                            Toast.makeText(context,
                                "Đang tiến hành cập nhật khóa học", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        onCourseInfoAction.invoke(CourseInfoAction.OnUpdate)
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
                UpdateDialog(
                    updateState = updateState,
                    onDismiss = { },
                    onContinueClick = {
                        onCourseInfoAction.invoke(CourseInfoAction.OnResetState)
                        showUpdateDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun UpdateDialog(
    updateState: UpdateState,
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Timber.i("show update course dialog: $updateState")
    val textNotification = when (updateState) {
        is UpdateState.Success -> R.string.update_course_success
        is UpdateState.Error -> R.string.update_course_error
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
fun CourseInfoPreview(modifier: Modifier = Modifier) {
    CourseInfoScreen(
        deleteState = DeleteState.Pending,
        updateState = UpdateState.Loading,
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
fun UpdateDialogPreview(modifier: Modifier = Modifier) {
    UpdateDialog(
        updateState = UpdateState.Loading,
        onDismiss = {},
        onContinueClick = {},
        modifier = modifier
    )
}

