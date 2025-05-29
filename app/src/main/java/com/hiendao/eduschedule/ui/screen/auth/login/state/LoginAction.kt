package com.hiendao.eduschedule.ui.screen.auth.login.state

sealed interface LoginAction {
    data class OnEmailChange(val email: String): LoginAction
    data class OnPasswordChange(val password: String): LoginAction
    data class OnRememberMeChange(val rememberMe: Boolean): LoginAction
    data object OnLoginClick: LoginAction
    data object ClearState: LoginAction
}