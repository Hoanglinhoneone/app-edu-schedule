package com.hiendao.eduschedule.ui.screen.auth.changePassword.state

import com.hiendao.eduschedule.utils.isPasswordValid

data class ChangePasswordUiState(
    var email: String = "",
    var newPassword: String = "",
    var confirmPassword: String = ""
) {
    fun isPasswordValid(): Boolean {
        return newPassword.isNotEmpty() && newPassword.isPasswordValid()
    }

    fun isConfirmPasswordValid(): Boolean {
        return confirmPassword.isNotEmpty() && confirmPassword.isPasswordValid()
    }

    fun isPasswordMatch(): Boolean {
        return newPassword == confirmPassword
    }

    fun isFormValid(): Boolean {
        return isPasswordValid() && isConfirmPasswordValid() && isPasswordMatch()
    }
}
