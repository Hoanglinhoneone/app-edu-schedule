package com.hiendao.eduschedule.utils

fun String.isEmailValid(): Boolean{
    return this.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))
}

fun String.isPasswordValid(): Boolean{
    // Minimum eight characters, at least one number
    return this.matches(Regex("^(?=.*\\d)[A-Za-z\\d@#\$%^&_+=!]{8,}\$"))
}