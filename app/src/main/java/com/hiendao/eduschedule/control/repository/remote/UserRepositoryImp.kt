package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.AllApi
import com.hiendao.eduschedule.ui.screen.profile.UpdateUserState
import com.hiendao.eduschedule.utils.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val allApi: AllApi
) : UserRepository{

    override suspend fun updateUser(user: User): Flow<UpdateUserState> = flow {

        Timber.i("update user with: $user")
        emit(UpdateUserState.Loading)
        try {
            val response = allApi.updateUserInfo(user)
            emit(UpdateUserState.Success)
            if(response.statusCode == "200") {
                Timber.i("update user success")
                emit(UpdateUserState.Success)
            } else {
                Timber.i("update user error: ${response.errorMessage}")
                emit(UpdateUserState.Error(response.errorMessage ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(UpdateUserState.Error(e.message ?: "Unknown error"))
            e.printStackTrace()
        }
    }
}

interface UserRepository {
    suspend fun updateUser(user: User) : Flow<UpdateUserState>
}

