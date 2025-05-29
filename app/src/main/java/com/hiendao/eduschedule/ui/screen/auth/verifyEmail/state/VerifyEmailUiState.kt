package com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state

import com.hiendao.eduschedule.utils.isEmailValid

data class VerifyEmailUiState(
    var email: String = "",
    var code: String = "",
    var timeSeconds: Long = 0L,
    var isEnableSendMail: Boolean = true
){
    fun isValidEmail(): Boolean {
        return email.isNotEmpty() && email.isEmailValid()
    }

}
