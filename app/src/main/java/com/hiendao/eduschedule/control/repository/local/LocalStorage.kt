package com.hiendao.eduschedule.control.repository.local

import kotlin.reflect.KClass

interface LocalStorage {

    fun putString(key: String, value: String?)
    fun getString(key: String): String?
    fun remove(key: String)

    var authorization: String?

    fun <T : Any> putData(key: String, t: T?)

    fun <T : Any> getData(key: String): T?

    fun <T : Any> getData(key: String, clazz: KClass<T>): T?

    var countPostNoti: Int
    var countErrorNoti: Int
    var errorNoti: String
    var isSignedIn: Boolean
    var authToken: String
    var otpCode: String
    var timeGetOtpCode: Long
    var langCode: String
}
