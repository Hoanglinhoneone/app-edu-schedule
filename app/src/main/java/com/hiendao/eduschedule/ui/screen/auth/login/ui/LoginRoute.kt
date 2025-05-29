package com.hiendao.eduschedule.ui.screen.auth.login.ui

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.navigation.TypeGraph
import com.hiendao.eduschedule.ui.screen.auth.GoogleAuthUIClient
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginAction
import com.hiendao.eduschedule.ui.screen.auth.login.viewmodel.LoginViewModel
import com.hiendao.eduschedule.utils.Constants
import dagger.hilt.android.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val loginState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val googleAuthUIClient = GoogleAuthUIClient(
        context,
        doSignIn = { credential ->
            //Navigate to Home Screen
            //localStorage.isSignedIn = true
            viewModel.sendInfoLoginWithGoogle(credential)
        }
    )

    LaunchedEffect(viewModel, context) {
        viewModel.authenticateState.collect { authenticateState ->
            if (authenticateState) {
                navController.navigate(TypeGraph.Home.route) {
                    popUpTo(TypeGraph.Auth.route) { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.loginState.collect { state ->
            if (state.isSuccess) {
                Toast.makeText(context,
                    context.getString(R.string.login_successfullly), Toast.LENGTH_SHORT).show()
                navController.navigate(TypeGraph.Home.route) {
                    popUpTo(TypeGraph.Auth.route) { inclusive = true }
                }
            } else {
                if (!state.message.isNullOrEmpty()) {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        loginScreenState = loginState,
        onLoginAction = viewModel::onLoginAction,
        onForgotPasswordClick = {
            //Navigate to Forgot password screen
            viewModel.onLoginAction(LoginAction.ClearState)
            navController.navigate("${AppScreen.VerifyEmail.name}/false")
        },
        onRegisterClick = {
            //Navigate to Register screen
            viewModel.onLoginAction(LoginAction.ClearState)
            navController.navigate(AppScreen.SignUp.name)
        },
        onGoogleLoginClick = {
            viewModel::signInWithGoogle.invoke(googleAuthUIClient)
        }
    )
}