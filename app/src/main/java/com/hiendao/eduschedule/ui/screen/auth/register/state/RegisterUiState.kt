package com.hiendao.eduschedule.ui.screen.auth.register.state

import com.hiendao.eduschedule.utils.isEmailValid
import com.hiendao.eduschedule.utils.isPasswordValid

data class RegisterUiState(
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var fullName: String = "",
    var phoneNumber: String = "",
    var showPassword: Boolean = false,
    var showConfirmPassword: Boolean = false,
    var otpCode: String = "",
    var agreePolicy: Boolean = false
){
    val isEmailValid: Boolean
        get() = email.isEmailValid()

    val isPasswordValid: Boolean
        get() = password.isPasswordValid()

    val isConfirmPasswordValid: Boolean
        get() = confirmPassword.isPasswordValid()
    val isPasswordMatch: Boolean
        get() =
            password == confirmPassword
    val isNotEmpty: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fullName.isNotEmpty() && phoneNumber.isNotEmpty() && otpCode.isNotEmpty()
    val isAgreePolicy: Boolean
        get() = agreePolicy
    val isValid: Boolean
        get() = isEmailValid && isPasswordValid && isConfirmPasswordValid && isPasswordMatch && isNotEmpty && isAgreePolicy
}
