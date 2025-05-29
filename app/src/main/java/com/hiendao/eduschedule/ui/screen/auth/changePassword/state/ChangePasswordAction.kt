package com.hiendao.eduschedule.ui.screen.auth.changePassword.state

sealed interface ChangePasswordAction {
    data class OnPasswordChange(val password: String): ChangePasswordAction
    data class OnConfirmPasswordChange(val confirmPassword: String): ChangePasswordAction
    data object OnChangePasswordClick: ChangePasswordAction
}