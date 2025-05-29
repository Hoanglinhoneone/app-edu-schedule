package com.hiendao.eduschedule.ui.screen.auth.register.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.auth.register.RegisterAction
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterUiState
import com.hiendao.eduschedule.utils.isEmailValid
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    registerUiState: RegisterUiState,
    onGoogleRegisterClick: () -> Unit = {},
    onGoToLogin: () -> Unit = {},
    onRegisterAction: (RegisterAction) -> Unit = {},
    startCountdownState: Boolean = false
) {
    val context = LocalContext.current
    val textFieldColor = OutlinedTextFieldDefaults.colors().copy(
        unfocusedIndicatorColor = Color.Gray,
        focusedIndicatorColor = Color.Gray,
        focusedTextColor = Color.Black
    )
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var waitingTimeSendingCode by remember { mutableIntStateOf(0) }
    var showInvalidEmail by remember { mutableStateOf(false) }
    var showInvalidPassword by remember { mutableStateOf(false) }
    var showInvalidConfirmPassword by remember { mutableStateOf(false) }
    var showDontMatchPassword by remember { mutableStateOf(false) }
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
            .padding(horizontal = 18.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = registerUiState.fullName,
            onValueChange = {
                onRegisterAction.invoke(RegisterAction.OnFullNameChange(it))
            },
            label = {
                Text(text = stringResource(R.string.fullname), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Email",
                    tint = Color(0xFFA4A4A4)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(18.dp)
        )

        OutlinedTextField(
            value = registerUiState.email,
            onValueChange = {
                showInvalidEmail = false
                onRegisterAction.invoke(RegisterAction.OnEmailChange(it))
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

        OutlinedTextField(
            value = registerUiState.phoneNumber,
            onValueChange = {
                onRegisterAction.invoke(RegisterAction.OnPhoneNumberChange(it))
            },
            label = {
                Text(text = stringResource(R.string.phone_number), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "PhoneNumber",
                    tint = Color(0xFFA4A4A4)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(18.dp)
        )

        OutlinedTextField(
            value = registerUiState.password,
            onValueChange = {
                showInvalidPassword = false
                onRegisterAction.invoke(RegisterAction.OnPasswordChange(it))
            },
            isError = showInvalidPassword,
            supportingText = {
                if (showInvalidPassword) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.password_invalid),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = {
                Text(text = stringResource(R.string.password), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Email",
                    tint = Color(0xFFA4A4A4)
                )
            },
            trailingIcon = {
                val icon = if (showPassword) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                }

                IconButton(
                    onClick = {
                        showPassword = !showPassword
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (showPassword) "Ẩn mật khẩu" else "Hiện mật khẩu",
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(18.dp)
        )

        OutlinedTextField(
            value = registerUiState.confirmPassword,
            onValueChange = {
                showInvalidConfirmPassword = false
                onRegisterAction.invoke(RegisterAction.OnConfirmPasswordChange(it))
            },
            isError = showInvalidConfirmPassword,
            supportingText = {
                if (showInvalidConfirmPassword) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.password_invalid),
                        color = MaterialTheme.colorScheme.error
                    )
                } else if(showDontMatchPassword) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.password_not_match),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = {
                Text(text = stringResource(R.string.confirm_password), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Xác nhận mật khẩu",
                    tint = Color(0xFFA4A4A4)
                )
            },
            trailingIcon = {
                val icon = if (showConfirmPassword) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                }

                IconButton(
                    onClick = {
                        showConfirmPassword = !showConfirmPassword
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (showConfirmPassword) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = registerUiState.otpCode,
                onValueChange = {
                    onRegisterAction.invoke(RegisterAction.OnOtpCodeChange(it))
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
                    if(registerUiState.email.isEmailValid()){
                        onRegisterAction.invoke(RegisterAction.OnSendEmail)
                        startCountdown = true
                        return@OutlinedButton
                    }
                    if(registerUiState.email.isNotEmpty()){
                        Toast.makeText(context, context.getString(R.string.email_invalid), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,
                            context.getString(R.string.pls_input_email), Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = startCountdown.not(),
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if(waitingTimeSendingCode != 0) stringResource(
                        R.string.send_code_with_time,
                        waitingTimeSendingCode
                    ) else stringResource(R.string.send_code),
                    fontSize = 14.sp,
                    color = if(waitingTimeSendingCode != 0) Color.Gray else MaterialTheme.colorScheme.onBackground,
                    modifier = if(waitingTimeSendingCode != 0){
                        Modifier
                            .padding(vertical = 10.dp)
                            .width(100.dp)
                    } else {
                        Modifier.padding(vertical = 10.dp)
                    },
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = registerUiState.agreePolicy,
                    onCheckedChange = {
                        onRegisterAction.invoke(RegisterAction.OnAgreePolicyChange(it))
                    },
                    colors = CheckboxDefaults.colors().copy(
                        checkedCheckmarkColor = Color.White,
                        checkedBoxColor = Color(0xFFBA68C8),
                        checkedBorderColor = Color(0xFFBA68C8)
                    ),
                    modifier = Modifier.padding(4.dp)
                )
            }

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.agree_with))
                    withLink(
                        LinkAnnotation.Url(
                            "https://developer.android.com/",
                            TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        append(stringResource(R.string.terms))
                    }
                    append("và ")
                    withLink(
                        LinkAnnotation.Url(
                            "https://developer.android.com/jetpack/compose",
                            TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        append(stringResource(R.string.service))
                    }
                    append(".")
                },
                fontSize = 12.sp
            )
        }

        //Register Button
        OutlinedButton(
            onClick = {
                if(!registerUiState.isNotEmpty){
                    Toast.makeText(context,
                        context.getString(R.string.pls_all_fields), Toast.LENGTH_SHORT).show()
                }
                if(!registerUiState.isEmailValid){
                    showInvalidEmail = true
                }
                if(!registerUiState.isPasswordValid){
                    showInvalidPassword = true
                }
                if(!registerUiState.isConfirmPasswordValid){
                    showInvalidConfirmPassword = true
                } else if(!registerUiState.isPasswordMatch){
                    showDontMatchPassword = true
                }
                if(!registerUiState.isAgreePolicy && registerUiState.isNotEmpty){
                    Toast.makeText(context,
                        context.getString(R.string.pls_agree_policy), Toast.LENGTH_SHORT).show()
                }
                if(registerUiState.isValid){
                    onRegisterAction.invoke(RegisterAction.OnRegisterClick)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = stringResource(R.string.continue_with),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            onClick = {
                onGoogleRegisterClick.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = "Login with Google",
                modifier = Modifier.padding(end = 10.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.register_w_google),
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.already_had_account),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )

            Text(
                text = stringResource(R.string.login),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        onGoToLogin.invoke()
                    }
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    EduScheduleTheme {
        SignUpScreen(
            registerUiState = RegisterUiState()
        )
    }
}