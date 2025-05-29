package com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state

data class AuthState(val isSuccess: Boolean, val message: String?, val token: String? = null)
