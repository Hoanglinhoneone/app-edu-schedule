package com.hiendao.eduschedule.ui.screen.auth.changePassword.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.auth.changePassword.state.ChangePasswordAction
import com.hiendao.eduschedule.ui.screen.auth.changePassword.state.ChangePasswordUiState
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    changePasswordUiState: ChangePasswordUiState = ChangePasswordUiState(),
    onChangePasswordAction: (ChangePasswordAction) -> Unit = {}
) {
    val context = LocalContext.current
    
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showInvalidPassword by remember { mutableStateOf(false) }
    var showInvalidConfirmPassword by remember { mutableStateOf(false) }
    var showDontMatchPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp, start = 18.dp, end = 18.dp)
    ) {
        Text(
            text = stringResource(R.string.pls_input_new_password),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = changePasswordUiState.newPassword,
            onValueChange = {
                showInvalidPassword = false
                onChangePasswordAction(ChangePasswordAction.OnPasswordChange(it))
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
                Text(text = stringResource(R.string.new_password), fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Password",
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
            value = changePasswordUiState.confirmPassword,
            onValueChange = {
                showInvalidConfirmPassword = false
                showDontMatchPassword = false
                onChangePasswordAction(ChangePasswordAction.OnConfirmPasswordChange(it))
            },
            isError = showInvalidConfirmPassword || showDontMatchPassword,
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
                    contentDescription = "Confirm Password",
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
                .padding(top = 16.dp),
            shape = RoundedCornerShape(18.dp)
        )

        OutlinedButton(
            onClick = {
                if (!changePasswordUiState.isPasswordValid()) {
                    showInvalidPassword = true
                    return@OutlinedButton
                }
                if (!changePasswordUiState.isConfirmPasswordValid()) {
                    showInvalidConfirmPassword = true
                    return@OutlinedButton
                }
                if (!changePasswordUiState.isPasswordMatch()) {
                    showDontMatchPassword = true
                    return@OutlinedButton
                }
                if (changePasswordUiState.isFormValid()) {
                    onChangePasswordAction(ChangePasswordAction.OnChangePasswordClick)
                    Toast.makeText(context, context.getString(R.string.change_password_successfully), Toast.LENGTH_SHORT).show()
                }
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
                text = stringResource(R.string.change_password),
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
private fun ChangePasswordPreview() {
    EduScheduleTheme {
        ChangePasswordScreen()
    }
}