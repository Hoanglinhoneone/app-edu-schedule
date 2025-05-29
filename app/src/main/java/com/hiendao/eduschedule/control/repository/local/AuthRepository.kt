package com.hiendao.eduschedule.control.repository.local

import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginState
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterModel
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.AuthState
import kotlinx.coroutines.flow.Flow

interface LoginRepository{
    suspend fun login(username: String, password: String): Flow<LoginState>
    suspend fun loginWithGoogle(fullName: String, email: String, phoneNumber: String, imageUrl: String): Flow<LoginState>
}

interface RegisterRepository{
    suspend fun register(registerModel: RegisterModel): Flow<RegisterState>
    suspend fun registerWithGoogle(fullName: String, email: String, phoneNumber: String, imageUrl: String): Flow<RegisterState>
}

interface VerifyRepository {
    suspend fun sendMail(email: String, event: String): Flow<AuthState>
    suspend fun updatePassword(email: String, password: String): Flow<AuthState>
}