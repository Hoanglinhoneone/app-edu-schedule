package com.hiendao.eduschedule.ui.screen.auth.changePassword.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.screen.auth.changePassword.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    email: String,
    isUpdatePassword: Boolean = false
) {
    val viewModel: ChangePasswordViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.uiState.value.email = email
    }

    LaunchedEffect(viewModel, context) {
        viewModel.toastMessage.collect { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.changePassState.collect {state ->
            if(state.isSuccess) {
                Toast.makeText(context,
                    context.getString(R.string.change_password_successfully), Toast.LENGTH_SHORT).show()
                if(isUpdatePassword){
                    navController.navigate(AppScreen.ChangePassword.name) {
                        popUpTo(AppScreen.ForgotPassword.name) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppScreen.Login.name) {
                        popUpTo(AppScreen.ForgotPassword.name) { inclusive = true }
                    }
                }
            } else {
                Toast.makeText(context, state.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    ChangePasswordScreen(
        modifier = modifier,
        changePasswordUiState = uiState,
        onChangePasswordAction = { action ->
            viewModel.onChangePasswordAction(action)
        }
    )

}