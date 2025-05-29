package com.hiendao.eduschedule.ui.screen.auth.verifyEmail.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.VerifyRepository
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.VerifyAction
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.AuthState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.VerifyEmailUiState
import com.hiendao.eduschedule.utils.Constants
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

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val verifyRepository: VerifyRepository,
    private val localStorage: LocalStorage
): ViewModel() {
    private var _uiState = MutableStateFlow(VerifyEmailUiState())
    val uiState: StateFlow<VerifyEmailUiState> = _uiState.asStateFlow()

    private val _verifyCodeState = MutableSharedFlow<AuthState>()
    val verifyCodeState: SharedFlow<AuthState> = _verifyCodeState.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _sendMailState = MutableSharedFlow<AuthState>()
    val sendMailState: SharedFlow<AuthState> = _sendMailState.asSharedFlow()

    fun onEvent(event: VerifyAction) {
        viewModelScope.launch {
            when (event) {
                is VerifyAction.OnEmailChange -> {
                    _uiState.update { it.copy(email = event.email) }
                }
                is VerifyAction.OnCodeChange -> {
                    _uiState.update { it.copy(code = event.code) }
                }
                is VerifyAction.OnSendEmailClick -> {
                    sendEmailVerify()
                }
                is VerifyAction.OnContinueClick -> {
                    if(_uiState.value.code.isEmpty()){
                        _toastMessage.emit("Vui lòng nhập mã xác thực")
                        return@launch
                    }
                    if(localStorage.otpCode.isEmpty() || _uiState.value.code != localStorage.otpCode){
                        _toastMessage.emit("Mã xác thực không đúng")
                        return@launch
                    }
                    if(System.currentTimeMillis() - localStorage.timeGetOtpCode > Constants.TIME_EXPIRED_OTP_CODE){
                        _toastMessage.emit("Mã xác thực đã hết hạn")
                        return@launch
                    }
                    _verifyCodeState.emit(
                        AuthState(
                            isSuccess = true,
                            message = "Xác thực thành công"
                        )
                    )
                }
                is VerifyAction.ClearState -> {
                    _uiState.update { VerifyEmailUiState() }
                }
            }
        }
    }

    private fun sendEmailVerify(){
        viewModelScope.launch(Dispatchers.IO) {
            verifyRepository.sendMail(_uiState.value.email, Constants.FORGOT_PASSWORD).collect {
                _sendMailState.emit(it)
            }
        }
    }
}