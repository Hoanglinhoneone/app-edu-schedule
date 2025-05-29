package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.auth.AuthApi
import com.hiendao.eduschedule.control.datasource.remote.api.convert
import com.hiendao.eduschedule.control.model.body.LoginBody
import com.hiendao.eduschedule.control.model.body.RegisterBody
import com.hiendao.eduschedule.control.model.body.VerifyUserBody
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.LoginRepository
import com.hiendao.eduschedule.control.repository.local.RegisterRepository
import com.hiendao.eduschedule.control.repository.local.VerifyRepository
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginState
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterModel
import com.hiendao.eduschedule.ui.screen.auth.register.state.RegisterState
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.state.AuthState
import com.hiendao.eduschedule.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImp @Inject constructor(
    private val localStorage: LocalStorage,
    private val authApi: AuthApi
) : LoginRepository {
    override suspend fun login(username: String, password: String): Flow<LoginState> {
        return flow {
            try {
                val response = authApi.login(LoginBody(username, password))
                println("Login response isSuccess: ${response}")
                if (!response.token.isNullOrEmpty()) {
                    localStorage.authToken = response.token
                    emit(LoginState(true, Constants.HttpErrorMessage.OK))
                } else {
                    emit(LoginState(false, Constants.HttpErrorMessage.UNAUTHORIZED))
                }
            } catch (e: HttpException) {
                val errorConverter = e.convert()
                if (errorConverter != null) {
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    emit(LoginState(false, errorConverter.message))
                } else {
                    println(e.message)
                    emit(LoginState(false, Constants.HttpErrorMessage.UNKNOWN))
                }
            } catch (e: Exception){
                println(e.message)
                emit(LoginState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun loginWithGoogle(
        fullName: String,
        email: String,
        phoneNumber: String,
        imageUrl: String
    ): Flow<LoginState> {
        return flow {
            try {
                val response = authApi.loginWithGoogle(
                    fullName, email, phoneNumber
                )
                println("Login response isSuccess: ${response}")
                if(!response.token.isNullOrEmpty()){
                    localStorage.authToken = response.token
                    emit(LoginState(true, Constants.HttpErrorMessage.OK))
                } else {
                    emit(LoginState(false, Constants.HttpErrorMessage.UNAUTHORIZED))
                }
            } catch (e : HttpException){
                val errorConverter = e.convert()
                if(errorConverter != null){
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    emit(LoginState(false, errorConverter.message))
                } else {
                    println(e.message)
                    emit(LoginState(false, Constants.HttpErrorMessage.UNKNOWN))
                }
            } catch (e : Exception){
                println(e.message)
                emit(LoginState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }
}

class RegisterRepositoryImp @Inject constructor(
    private val localStorage: LocalStorage,
    private val authApi: AuthApi
): RegisterRepository {
    override suspend fun register(registerModel: RegisterModel): Flow<RegisterState> {
        return flow {
            try {
                val response = authApi.register(
                    RegisterBody(
                        fullName = registerModel.fullName,
                        email = registerModel.email,
                        password = registerModel.password,
                        mobilePhone = registerModel.phoneNumber
                    )
                )
                println("Register response success")
                emit(RegisterState(true, Constants.HttpErrorMessage.OK))
            } catch (e : HttpException){
                val errorConverter = e.convert()
                if(errorConverter != null){
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    emit(RegisterState(false, errorConverter.message))
                } else {
                    println(e.message)
                    emit(RegisterState(false, Constants.HttpErrorMessage.UNKNOWN))
                }
            } catch (e : Exception){
                println(e.message)
                emit(RegisterState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun registerWithGoogle(
        fullName: String,
        email: String,
        phoneNumber: String,
        imageUrl: String
    ): Flow<RegisterState> {
        return flow {
            try {
                val response = authApi.loginWithGoogle(
                    fullName, email, phoneNumber
                )
                println("Login response isSuccess: ${response}")
                if(!response.token.isNullOrEmpty()){
                    localStorage.authToken = response.token
                    emit(RegisterState(true, Constants.HttpErrorMessage.OK))
                } else {
                    emit(RegisterState(false, Constants.HttpErrorMessage.UNAUTHORIZED))
                }
            } catch (e : HttpException){
                val errorConverter = e.convert()
                if(errorConverter != null){
                    println("Error code: ${errorConverter.code} and message: ${errorConverter.message}")
                    emit(RegisterState(false, errorConverter.message))
                } else {
                    println(e.message)
                    emit(RegisterState(false, Constants.HttpErrorMessage.UNKNOWN))
                }
            } catch (e : Exception){
                println(e.message)
                emit(RegisterState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }
}

class VerifyEmailRepositoryImp @Inject constructor(
    private val localStorage: LocalStorage,
    private val authApi: AuthApi
): VerifyRepository {
    override suspend fun sendMail(email: String, event: String): Flow<AuthState> {
        return flow {
            try{
                val response = authApi.sendEmail(email, event)
                if(!response.otpCode.isNullOrEmpty()){
                    localStorage.otpCode = response.otpCode
                    localStorage.timeGetOtpCode = System.currentTimeMillis()
                    emit(AuthState(true, Constants.HttpErrorMessage.OK, token = response.otpCode))
                } else {
                    emit(AuthState(false, Constants.HttpErrorMessage.UNAUTHORIZED))
                }
            } catch (e : HttpException){
                println(e.message)
                emit(AuthState(false, e.message))
            } catch (e : Exception){
                println(e.message)
                emit(AuthState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }

    override suspend fun updatePassword(email: String, password: String): Flow<AuthState> {
        return flow {
            try{
                val response = authApi.updatePassword(email, password)
                emit(AuthState(true, Constants.HttpErrorMessage.OK))
            } catch (e : HttpException){
                println(e.message)
                emit(AuthState(false, e.message))
            } catch (e : Exception){
                println(e.message)
                emit(AuthState(false, Constants.HttpErrorMessage.UNKNOWN))
            }
        }
    }
}