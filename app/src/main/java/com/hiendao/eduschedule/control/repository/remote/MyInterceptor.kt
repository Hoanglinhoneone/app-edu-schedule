package com.hiendao.eduschedule.control.repository.remote

import android.content.SharedPreferences
import android.util.Log
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class MyInterceptor @Inject constructor(
    private val localStorage: LocalStorage
//    private val tokenApi: TokenApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = localStorage.authToken
        if (accessToken.isNotEmpty()) {
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            return chain.proceed(request)
        }
        return chain.proceed(chain.request())
    }
}