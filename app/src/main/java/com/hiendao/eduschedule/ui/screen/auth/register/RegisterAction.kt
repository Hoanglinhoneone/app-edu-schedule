package com.hiendao.eduschedule.ui.screen.auth.register

sealed interface RegisterAction {
    data class OnFullNameChange(val fullName: String): RegisterAction
    data class OnPhoneNumberChange(val phoneNumber: String): RegisterAction
    data class OnEmailChange(val email: String): RegisterAction
    data class OnPasswordChange(val password: String): RegisterAction
    data class OnOtpCodeChange(val otpCode: String): RegisterAction
    data class OnConfirmPasswordChange(val confirmPassword: String): RegisterAction
    data class OnAgreePolicyChange(val agreePolicy: Boolean): RegisterAction
    data object OnRegisterClick: RegisterAction
    data object ClearState: RegisterAction
    data object OnSendEmail: RegisterAction
}