package com.hiendao.eduschedule.ui.screen.profile

interface ProfileAction {
    data class UpdateFullName(val fullName: String) : ProfileAction
    data class UpdateEmail(val email: String) : ProfileAction
    data class UpdateMobilePhone(val mobilePhone: String) : ProfileAction
    data class UpdateGender(val gender: String) : ProfileAction
    data class UpdateDateOfBirth(val dateOfBirth: String) : ProfileAction
    object UpdateUser : ProfileAction
}