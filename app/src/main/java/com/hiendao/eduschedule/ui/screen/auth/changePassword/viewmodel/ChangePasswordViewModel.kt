package com.hiendao.eduschedule.ui.screen.auth.changePassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.local.VerifyRepository
import com.hiendao.eduschedule.ui.screen.auth.changePassword.state.ChangePasswordAction
import com.hiendao.eduschedule.ui.screen.auth.changePassword.state.ChangePasswordUiState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val verifyRepository: VerifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _changePassState = MutableSharedFlow<AuthState>()
    val changePassState: SharedFlow<AuthState> = _changePassState.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    fun onChangePasswordAction(event: ChangePasswordAction) {
        when (event) {
            ChangePasswordAction.OnChangePasswordClick -> {
                changePassword()
            }
            is ChangePasswordAction.OnConfirmPasswordChange -> {
                _uiState.value = _uiState.value.copy(confirmPassword = event.confirmPassword)
            }
            is ChangePasswordAction.OnPasswordChange -> {
                _uiState.value = _uiState.value.copy(newPassword = event.password)
            }
        }
    }

    private fun changePassword(){
        val password = _uiState.value.newPassword
        val email = _uiState.value.email
        viewModelScope.launch(Dispatchers.IO) {
            verifyRepository.updatePassword(email, password).collect {
                _changePassState.emit(it)
            }
        }
    }

}