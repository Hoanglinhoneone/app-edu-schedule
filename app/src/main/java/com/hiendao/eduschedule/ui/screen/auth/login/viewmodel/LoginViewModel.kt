package com.hiendao.eduschedule.ui.screen.auth.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.LoginRepository
import com.hiendao.eduschedule.ui.screen.auth.GoogleAuthUIClient
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginAction
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginState
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginUiState
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.LoginAccount
import dagger.hilt.android.UnstableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * File mẫu
 */
@UnstableApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val loginRepository: LoginRepository
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    // manage ui state of screen
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // manage login state to update ui
    private val _loginState = MutableSharedFlow<LoginState>()
    val loginState: SharedFlow<LoginState> = _loginState.asSharedFlow()

    private val _authenticateState = MutableStateFlow<Boolean>(false)
    val authenticateState: StateFlow<Boolean> = _authenticateState.asStateFlow()
    /* **********************************************************************
    * Function
    ********************************************************************** */

    init {
        authenticate()
    }

    fun onLoginAction(loginAction: LoginAction){
        viewModelScope.launch {
            when(loginAction){
                is LoginAction.OnEmailChange -> {
                    _uiState.value = _uiState.value.copy(email = loginAction.email)
                }
                LoginAction.OnLoginClick -> {
                    login(LoginAccount(_uiState.value.email, _uiState.value.password))
                }
                is LoginAction.OnPasswordChange -> {
                    _uiState.value = _uiState.value.copy(password = loginAction.password)
                }
                is LoginAction.OnRememberMeChange -> {
                    _uiState.value = _uiState.value.copy(rememberMe = loginAction.rememberMe)
                }
                LoginAction.ClearState -> {
                    _uiState.value = LoginUiState()
                    _authenticateState.value = false
                    withContext(Dispatchers.IO){
                        _loginState.emit(LoginState(false, null))
                    }
                }
            }
        }
    }

    fun login(user: LoginAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.login(user.email, user.password).collect { result ->
                _loginState.emit(result)
            }
        }
    }

    fun sendInfoLoginWithGoogle(credential: GoogleIdTokenCredential){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                loginRepository.loginWithGoogle(
                    fullName = credential.displayName ?: "",
                    email = credential.id,
                    phoneNumber = credential.phoneNumber ?: "",
                    imageUrl = credential.profilePictureUri.toString()
                ).collect {
                    _loginState.emit(it)
                }
            }
        }
    }

    fun signInWithGoogle(googleAuthUIClient: GoogleAuthUIClient){
        viewModelScope.launch {
            googleAuthUIClient.signIn()
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            if(localStorage.authToken.isNotEmpty()) {
                _authenticateState.value = true
            } else {
                _authenticateState.value = false
            }
//            loginRepository.authenticate(localStorage.authToken).let {
//                _authenticateState.value = it
//                if(!it.isSuccess && it.message == Constants.HttpErrorMessage.NEED_REFRESH_TOKEN){
//                    localStorage.authToken = ""
//                }
//            }
        }
    }

    //...
}


