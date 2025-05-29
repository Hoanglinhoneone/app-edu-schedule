package com.higherstudio.calculatorlauncher.calculatorvault.vault.hidemedia.hideapp.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.PreferenceInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.reflect.KClass

class LocalData @Inject constructor(
    @ApplicationContext context: Context, @PreferenceInfo val fileName: String
) : LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun putString(key: String, value: String?) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override var authorization: String?
        get() = getString("authorization")
        set(value) {
            putString("authorization", value)
        }

    override fun <T : Any> putData(key: String, t: T?) {
        if (t != null) {
            val str = Gson().toJson(t)
            putString(key, str)
        } else putString(key, null)
    }

    override fun <T : Any> getData(key: String): T? {
        val string = getString(key) ?: return null
        try {
            return Gson().fromJson(string, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun <T : Any> getData(key: String, clazz: KClass<T>): T? {
        val string = getString(key) ?: return null
        try {
            return Gson().fromJson(string, clazz.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override var countPostNoti: Int
        get() = sharedPreferences.getInt("COUNT_NOTI", 0)
        set(value) = sharedPreferences.edit().putInt("COUNT_NOTI", value).apply()
    override var countErrorNoti: Int
        get() = sharedPreferences.getInt("COUNT_ERROR_NOTI", 0)
        set(value) {
            sharedPreferences.edit().putInt("COUNT_ERROR_NOTI", value).apply()
        }
    override var errorNoti: String
        get() = sharedPreferences.getString("ERROR_NOTI", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("ERROR_NOTI", value).apply()
        }
    override var isSignedIn: Boolean
        get() = sharedPreferences.getBoolean("IS_SIGNED_IN", false)
        set(value) {
            sharedPreferences.edit().putBoolean("IS_SIGNED_IN", value).apply()
        }
    override var authToken: String
        get() = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("AUTH_TOKEN", value).apply()
        }
    override var otpCode: String
        get() = sharedPreferences.getString("OTP_CODE", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("OTP_CODE", value).apply()
        }
    override var timeGetOtpCode: Long
        get() = sharedPreferences.getLong("TIME_GET_OTP_CODE", 0L)
        set(value) {
            sharedPreferences.edit().putLong("TIME_GET_OTP_CODE", value).apply()
        }
    override var langCode: String
        get() = sharedPreferences.getString("LANG_CODE", "vi") ?: "vi"
        set(value) {
            sharedPreferences.edit().putString("LANG_CODE", value).apply()
        }
}
