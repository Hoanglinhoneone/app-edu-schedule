package com.hiendao.eduschedule.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.study.lessondetail.UpdateState
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.secondaryLight
import timber.log.Timber

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier
) {

    val viewmodel: ProfileViewModel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val updateUserState by viewmodel.updateUserState.collectAsStateWithLifecycle()
    ProfileScreen(
        updateUserState = updateUserState,
        profileAction = {
            viewmodel::onAction.invoke(it)
        },
        uiState = uiState,
        onUpdateUser = { viewmodel.updateUser() },
        modifier = modifier
    )
//    LaunchedEffect(updateUserState) {
//        if(updateUserState is UpdateUserState.Error) {
//
//     }
//    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    profileAction: (ProfileAction) -> Unit = {},
    updateUserState: UpdateUserState,
    uiState: UiState,
    onUpdateUser: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var userName by remember { mutableStateOf("Hoàng Ngọc Linh") }
    var birthDate by remember { mutableStateOf("30/05/2003") }
    var profession by remember { mutableStateOf("Developer") }
    var expand by remember { mutableStateOf(false)}
    val options = listOf<String>(
        "MALE",
        "FEMALE",
        "OTHER"
    )
    // Quản lý focus
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var showUpdateDialog by remember { mutableStateOf(false)}
    val purpleColor = Color(0xFF8A3FFC)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState), // Cho phép kéo trang
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(
                                color = Color(0xFFD9D9D9).copy(0.15f),
                                shape = CircleShape
                            )

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.my_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier.align(Alignment.BottomEnd),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF9747FF)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pen),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    uiState.email,
                    modifier = Modifier.padding(top = 20.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Họ và tên",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { profileAction.invoke(ProfileAction.UpdateFullName(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Ngày sinh",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = uiState.dateOfBirth,
                            onValueChange = { profileAction.invoke(ProfileAction.UpdateDateOfBirth(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray,
                                focusedBorderColor = Color.Gray
                            )
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f)

                    ) {
                    Column(
                    ) {
                        Text(
                            text = "Giới tính",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedButton(
                            onClick = {
                                expand = true
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.LightGray
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiState.gender,
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_selected),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    }
                    DropdownMenu(
                        expanded = expand,
                        onDismissRequest = { expand = false },
                        scrollState = rememberScrollState(),
                        modifier = Modifier.size(width = 250.dp, height = 250.dp)
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    expand = false
                                    profileAction.invoke(ProfileAction.UpdateGender(option))
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Số điện thoại",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = uiState.mobilePhone,
                    onValueChange = { profileAction.invoke(ProfileAction.UpdateMobilePhone(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray
                    )
                )

                Button(
                    onClick = {
                        keyboardController?.hide()
                        onUpdateUser()
                        showUpdateDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purpleColor
                    )
                ) {
                    Text(
                        text = "Lưu",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            if(showUpdateDialog) {
                UpdateLessonDialog(
                    updateState = updateUserState,
                    onDismiss = { showUpdateDialog = false },
                    onContinueClick = { showUpdateDialog = false }
                )
            }
        }
    }
}

@Composable
fun UpdateLessonDialog(
    updateState: UpdateUserState,
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Timber.i("show update course dialog: $updateState")
    val textNotification = when (updateState) {
        is UpdateUserState.Success -> R.string.update_user_success
        is UpdateUserState.Error -> R.string.update_user_error
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
                    is UpdateUserState.Success -> {
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

                    is UpdateUserState.Error -> {
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
                            is UpdateUserState.Success -> R.string.success_state
                            is UpdateUserState.Error -> R.string.error_state
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
fun ProfileScreenPreview() {
    EduScheduleTheme {
        ProfileScreen(
            uiState = UiState(),
            onUpdateUser = {},
            updateUserState = UpdateUserState.Success
        )
    }
}