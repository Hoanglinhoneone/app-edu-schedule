package com.hiendao.eduschedule.ui.screen.auth.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.RegisterRepository
import com.hiendao.eduschedule.control.repository.local.VerifyRepository
import com.hiendao.eduschedule.ui.screen.auth.GoogleAuthUIClient
import com.hiendao.eduschedule.ui.screen.auth.register.RegisterAction
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterModel
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterState
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterUiState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.AuthState
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.base.BaseViewModel
import dagger.hilt.android.UnstableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val registerRepository: RegisterRepository,
    private val verifyRepository: VerifyRepository
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    // manage ui state of screen
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // manage login state to update ui
    private val _registerState = MutableSharedFlow<RegisterState>()
    val registerState: SharedFlow<RegisterState> = _registerState.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _sendMailState = MutableSharedFlow<AuthState>()
    val sendMailState: SharedFlow<AuthState> = _sendMailState.asSharedFlow()
    /* **********************************************************************
    * Function
    ********************************************************************** */

    fun onRegisterAction(registerAction: RegisterAction){
        viewModelScope.launch {
            when(registerAction){
                is RegisterAction.OnEmailChange -> {
                    _uiState.value = _uiState.value.copy(email = registerAction.email)
                }
                RegisterAction.OnRegisterClick -> {
                    if(_uiState.value.otpCode.isEmpty()){
                        _toastMessage.emit("Vui lòng nhập mã xác thực")
                        return@launch
                    }
                    if(localStorage.otpCode.isEmpty() || _uiState.value.otpCode != localStorage.otpCode){
                        _toastMessage.emit("Mã xác thực không đúng")
                        return@launch
                    }
                    if(System.currentTimeMillis() - localStorage.timeGetOtpCode > Constants.TIME_EXPIRED_OTP_CODE){
                        _toastMessage.emit("Mã xác thực đã hết hạn")
                        return@launch
                    }
                    register()
                }
                is RegisterAction.OnPasswordChange -> {
                    _uiState.value = _uiState.value.copy(password = registerAction.password)
                }
                is RegisterAction.OnOtpCodeChange -> {
                    _uiState.value = _uiState.value.copy(otpCode = registerAction.otpCode)
                }
                is RegisterAction.OnAgreePolicyChange -> {
                    _uiState.value = _uiState.value.copy(agreePolicy = registerAction.agreePolicy)
                }
                RegisterAction.ClearState -> {
                    _uiState.value = RegisterUiState()
                    withContext(Dispatchers.IO){
                        _registerState.emit(RegisterState(false, null))
                    }
                }
                is RegisterAction.OnConfirmPasswordChange -> {
                    _uiState.value = _uiState.value.copy(confirmPassword = registerAction.confirmPassword)
                }
                is RegisterAction.OnFullNameChange -> {
                    _uiState.value = _uiState.value.copy(fullName = registerAction.fullName)
                }
                is RegisterAction.OnPhoneNumberChange -> {
                    _uiState.value = _uiState.value.copy(phoneNumber = registerAction.phoneNumber)
                }
                is RegisterAction.OnSendEmail -> {
                    sendEmailVerify()
                }
            }
        }
    }

    private fun sendEmailVerify(){
        viewModelScope.launch(Dispatchers.IO) {
            verifyRepository.sendMail(_uiState.value.email, Constants.VERIFY_USER).collect {
                _sendMailState.emit(it)
            }
        }
    }

    private fun register() {
        val user = _uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            registerRepository.register(
                RegisterModel(
                    email = user.email,
                    password = user.password,
                    fullName = user.fullName,
                    phoneNumber = user.phoneNumber
                )
            ).collect {
                _registerState.emit(it)
            }
//            loginRepository.login(user.email, user.password).let {
//                _registerState.value = it
//            }
        }
    }

    fun sendInfoLoginWithGoogle(credential: GoogleIdTokenCredential){
        viewModelScope.launch {
            registerRepository.registerWithGoogle(
                fullName = credential.displayName ?: "",
                email = credential.id,
                phoneNumber = credential.phoneNumber ?: "",
                imageUrl = credential.profilePictureUri.toString()
            ).collect {
                _registerState.emit(it)
            }
        }
    }

    fun signInWithGoogle(googleAuthUIClient: GoogleAuthUIClient){
        viewModelScope.launch {
            googleAuthUIClient.signIn()
        }
    }

    //...
}