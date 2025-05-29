package com.hiendao.eduschedule.control.datasource.remote.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginState
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

data class ErrorConverter(
    @SerializedName("apiPath")
    val path: String,
    @SerializedName("errorCode")
    val code: Int,
    @SerializedName("errorMessage")
    val message: String,
    @SerializedName("errorTime")
    val time: String
)


fun Exception.convert(): ErrorConverter?{
    if(this is HttpException){
        val body = this.response()?.errorBody()
        val gson = Gson()
        val adapter: TypeAdapter<ErrorConverter> = gson.getAdapter(ErrorConverter::class.java)
        try {
            val error: ErrorConverter = adapter.fromJson(body?.string())
            Timber.tag("HttpException").e(" code =  ${error.code} and message = + ${error.message}")
            return error
        } catch (e: IOException) {
            return null
        }
    }
    return null
}