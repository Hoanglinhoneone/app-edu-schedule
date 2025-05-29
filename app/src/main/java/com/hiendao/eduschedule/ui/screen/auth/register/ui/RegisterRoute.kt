package com.hiendao.eduschedule.ui.screen.auth.register.ui

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
import com.hiendao.eduschedule.ui.screen.auth.register.viewmodel.RegisterViewModel
import dagger.hilt.android.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isSigningInWithGoogle by remember {
        mutableStateOf(false)
    }
    val viewModel: RegisterViewModel = hiltViewModel()
    val registerState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val googleAuthUIClient = GoogleAuthUIClient(
        context,
        doSignIn = { credential ->
            //Navigate to Home Screen
            isSigningInWithGoogle = true
            viewModel.sendInfoLoginWithGoogle(credential)
//            navController.navigate(TypeGraph.Home.route) {
//                popUpTo(TypeGraph.Auth.route) { inclusive = true }
//            }
        }
    )

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
        viewModel.toastMessage.collect { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(viewModel, context) {
        viewModel.registerState.collect { state ->
            if(state.isSuccess){
                Toast.makeText(context,
                    context.getString(R.string.register_successfully), Toast.LENGTH_SHORT).show()
                if(isSigningInWithGoogle){
                    navController.navigate(AppScreen.Login.name){
                        popUpTo(AppScreen.SignUp.name) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppScreen.Login.name){
                        popUpTo(AppScreen.SignUp.name) { inclusive = true }
                    }                }
            } else {
                isSigningInWithGoogle = false
                if(!state.message.isNullOrEmpty()){
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SignUpScreen(
        modifier = modifier,
        registerUiState = registerState,
        onGoogleRegisterClick = {
            viewModel.signInWithGoogle(googleAuthUIClient)
        },
        onGoToLogin = {
            navController.navigateUp()
        },
        onRegisterAction = {
            viewModel::onRegisterAction.invoke(it)
        },
        startCountdownState = startCountdown.value
    )
}