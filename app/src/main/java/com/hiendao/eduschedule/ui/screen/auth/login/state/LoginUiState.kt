package com.hiendao.eduschedule.ui.screen.auth.login.state

import com.hiendao.eduschedule.utils.isEmailValid
import com.hiendao.eduschedule.utils.isPasswordValid

data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var showPassword: Boolean = false,
    var rememberMe: Boolean = true
){
    val isEmailValid: Boolean
        get() = email.isEmailValid()

    val isPasswordValid: Boolean
        get() = password.isPasswordValid()
}
