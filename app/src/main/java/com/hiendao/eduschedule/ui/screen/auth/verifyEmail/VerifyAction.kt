package com.hiendao.eduschedule.ui.screen.auth.verifyEmail


sealed interface VerifyAction {
    data class OnEmailChange(val email: String): VerifyAction
    data class OnCodeChange(val code: String): VerifyAction
    data object OnSendEmailClick: VerifyAction
    data object OnContinueClick: VerifyAction
    data object ClearState: VerifyAction
}