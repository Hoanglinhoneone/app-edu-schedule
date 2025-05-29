package com.hiendao.eduschedule.ui.screen.auth.login.ui

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
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
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginAction
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginUiState
import com.hiendao.eduschedule.utils.isEmailValid

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(
    modifier: Modifier = Modifier
) {
    LoginScreen(
        modifier = modifier,
        loginScreenState = LoginUiState()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginScreenState: LoginUiState,
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onLoginAction: (LoginAction) -> Unit = {}
) {
    val context = LocalContext.current
    var showPassword by remember {
        mutableStateOf(false)
    }
    var showPasswordError by remember {
        mutableStateOf(false)
    }
    var showEmailError by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp, start = 18.dp, end = 18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {

            OutlinedTextField(
                value = loginScreenState.email,
                onValueChange = {
                    onLoginAction.invoke(LoginAction.OnEmailChange(it))
                },
                label = {
                    Text(text = stringResource(R.string.email_address), fontSize = 14.sp, color = Color(0xFFA4A4A4))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Email",
                        tint = Color(0xFFA4A4A4)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                shape = RoundedCornerShape(18.dp)
            )

            OutlinedTextField(
                value = loginScreenState.password,
                onValueChange = {
                    onLoginAction.invoke(LoginAction.OnPasswordChange(it))
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                shape = RoundedCornerShape(18.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        Checkbox(
                            checked = loginScreenState.rememberMe,
                            onCheckedChange = {
                                onLoginAction.invoke(LoginAction.OnRememberMeChange(it))
                            },
                            colors = CheckboxDefaults.colors().copy(
                                checkedCheckmarkColor = Color.White,
                                checkedBoxColor = Color(0xFFBA68C8),
                                checkedBorderColor = Color(0xFFBA68C8)
                            ),
                            modifier = Modifier.padding(4.dp)
                        )
                    }

                    Text(text = stringResource(R.string.remember_password), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                }

                TextButton(
                    onClick = { onForgotPasswordClick.invoke() },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        fontSize = 14.sp,
                        color = Color(0xFFA4A4A4)
                    )
                }
            }

            //Login Button
            OutlinedButton(
                onClick = {
                    if(loginScreenState.email.isNotEmpty() && loginScreenState.password.isNotEmpty()){
                        onLoginAction.invoke(LoginAction.OnLoginClick)
                    } else {
                        Toast.makeText(context, context.getString(R.string.pls_all_fields), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonColors(
                    containerColor = Color(0xFFBA68C8),
                    contentColor = Color.White,
                    disabledContentColor = Color(0xFFA4A4A4),
                    disabledContainerColor = Color(0xFFBA68C8)
                ),
                border = BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
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
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = {
                    onGoogleLoginClick.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
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
                    text = stringResource(R.string.login_w_google),
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                )

                Text(
                    text = stringResource(R.string.create_account),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            onRegisterClick.invoke()
                        }
                        .padding(8.dp)
                )
            }
        }

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.first_policy))
                withLink(
                    LinkAnnotation.Url(
                        "https://developer.android.com/",
                        TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium))
                    )
                ) {
                    append(stringResource(R.string.terms))
                }
                append(stringResource(R.string.and))
                withLink(
                    LinkAnnotation.Url(
                        "https://developer.android.com/jetpack/compose",
                        TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium))
                    )
                ) {
                    append(stringResource(R.string.privacy_policy))
                }
                append(stringResource(R.string.of_mine))
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}