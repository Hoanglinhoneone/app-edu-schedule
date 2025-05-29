package com.hiendao.eduschedule.ui.screen.auth.verifyEmail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.navigation.TypeGraph
import com.hiendao.eduschedule.ui.screen.auth.register.RegisterAction
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.VerifyEmailUiState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.viewModel.VerifyEmailViewModel
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import kotlinx.coroutines.delay

@Composable
fun VerifyEmailRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    isChangePassword: Boolean = false,
) {
    val viewModel: VerifyEmailViewModel = hiltViewModel()
    val verifyEmailUiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val startCountdown = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel, context) {
        viewModel.sendMailState.collect { state ->
            if(!state.message.isNullOrEmpty() && !state.isSuccess) {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            } else if(state.isSuccess){
                startCountdown.value = true
            }
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.verifyCodeState.collect { state ->
            if (state.isSuccess) {
                Toast.makeText(context,
                    context.getString(R.string.authenticate_successfully), Toast.LENGTH_SHORT).show()
//                navController.navigate(AppScreen.Login.name) {
//                    popUpTo(TypeGraph.Auth.route) { inclusive = true }
//                }
                navController.navigate("forgotPassword/${verifyEmailUiState.email}/false") {
                    popUpTo(AppScreen.VerifyEmail.name) { inclusive = true }
                }
            } else {
                Toast.makeText(context,
                    context.getString(R.string.authenticate_unsuccessfully), Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.toastMessage.collect { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    VerifyEmailScreen(
        modifier = modifier,
        verifyEmailUiState = verifyEmailUiState,
        onVerifyEmailAction = {
            viewModel.onEvent(it)
        },
        onGoToLogin = {
            navController.navigateUp()
        },
        startCountdownState = startCountdown.value
    )
}

@Composable
fun VerifyEmailScreen(
    modifier: Modifier = Modifier,
    verifyEmailUiState: VerifyEmailUiState,
    onVerifyEmailAction: (VerifyAction) -> Unit = {},
    onGoToLogin: () -> Unit = {},
    startCountdownState: Boolean = false
) {
    val context = LocalContext.current
    var waitingTimeSendingCode by remember { mutableIntStateOf(0) }
    var showInvalidEmail by remember { mutableStateOf(false) }
    var startCountdown by remember(startCountdownState) { mutableStateOf(startCountdownState) }
    LaunchedEffect(startCountdown) {
        if (startCountdown) {
            waitingTimeSendingCode = 120
            while (waitingTimeSendingCode > 0) {
                delay(1000L)
                waitingTimeSendingCode -= 1
            }
            startCountdown = false
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp, start = 18.dp, end = 18.dp)
    ) {
        Text(
            text = stringResource(R.string.verify_email_message) +
                    "Hãy kiểm tra email và cả thư mục spam nhé!!!",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = verifyEmailUiState.email,
            onValueChange = {
                showInvalidEmail = false
                onVerifyEmailAction.invoke(VerifyAction.OnEmailChange(it))
            },
            isError = showInvalidEmail,
            label = {
                Text(text = stringResource(R.string.email_address), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            supportingText = {
                if (showInvalidEmail) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.email_invalid),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Email",
                    tint = Color(0xFFA4A4A4)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = verifyEmailUiState.code,
                onValueChange = {
                    onVerifyEmailAction.invoke(VerifyAction.OnCodeChange(it))
                },
                label = {
                    Text(text = stringResource(R.string.code), fontSize = 16.sp, color = Color(0xFFA4A4A4))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = "Mã",
                        tint = Color(0xFFA4A4A4)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                shape = RoundedCornerShape(18.dp)
            )

            OutlinedButton(
                onClick = {
                    if(verifyEmailUiState.isValidEmail()){
                        onVerifyEmailAction.invoke(VerifyAction.OnSendEmailClick)
                        return@OutlinedButton
                    }
                    if(verifyEmailUiState.email.isNotEmpty()){
                        Toast.makeText(context, context.getString(R.string.email_invalid), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, context.getString(R.string.pls_input_email), Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = startCountdown.not(),
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if(waitingTimeSendingCode != 0) stringResource(R.string.send_code_with_time, waitingTimeSendingCode) else stringResource(R.string.send_code),
                    fontSize = 14.sp,
                    color = if(waitingTimeSendingCode != 0) Color.Gray else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 10.dp),
                    maxLines = 1
                )
            }
        }

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.check_email_and_get_code))
                if(waitingTimeSendingCode != 0){
                    append(stringResource(R.string.you_can_send_email_in))
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFBA68C8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("${waitingTimeSendingCode}s")
                    }
                    append(".")
                }
            },
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.remember_password_question),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = stringResource(R.string.login),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBA68C8),
                modifier = Modifier
                    .clickable {
                        onGoToLogin.invoke()
                    })
        }

        OutlinedButton(
            onClick = {
                onVerifyEmailAction.invoke(VerifyAction.OnContinueClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = Color(0xFFA4A4A4),
                disabledContainerColor = Color(0xFFBA68C8)
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_continue),
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerifyEmail(
    modifier: Modifier = Modifier
) {
    EduScheduleTheme {
        VerifyEmailScreen(modifier = modifier, verifyEmailUiState = VerifyEmailUiState())
    }
}