package com.hiendao.eduschedule.ui.screen.profile

import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.UserRepository
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.base.BaseViewModel
import com.hiendao.eduschedule.utils.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _updateUserState = MutableStateFlow<UpdateUserState>(UpdateUserState.Pending)
    val updateUserState: StateFlow<UpdateUserState> = _updateUserState.asStateFlow()

    init {
        initUser()
    }

    private fun initUser() {
        val user = DataSource.user
        _uiState.update {
            it.copy(
                id = user.id,
                fullName = user.fullName,
                email = user.email,
                mobilePhone = user.mobilePhone,
                gender = user.gender,
                dateOfBirth = if(user.dateOfBirth.isNotEmpty()) user.dateOfBirth.substring(0,10) else ""
            )
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            val user = User(
                id = uiState.value.id,
                fullName = uiState.value.fullName,
                email = uiState.value.email,
                mobilePhone = uiState.value.mobilePhone,
                gender = uiState.value.gender,
                dateOfBirth = "${uiState.value.dateOfBirth}T00:00:00"
            )
            userRepository.updateUser(user).collect { state ->
                when (state) {
                    is UpdateUserState.Pending -> {
                        _updateUserState.value = UpdateUserState.Pending
                    }

                    is UpdateUserState.Loading -> {
                        _updateUserState.value = UpdateUserState.Loading
                    }

                    is UpdateUserState.Success -> {
                        _updateUserState.value = UpdateUserState.Success
                        DataSource.user = user
                    }

                    is UpdateUserState.Error -> {
                        _updateUserState.value = UpdateUserState.Error(state.message)
                    }
                }
            }
        }
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.UpdateFullName -> {
                _uiState.update { it.copy(fullName = action.fullName) }
            }

            is ProfileAction.UpdateEmail -> {
                _uiState.update { it.copy(email = action.email) }
            }

            is ProfileAction.UpdateMobilePhone -> {
                _uiState.update { it.copy(mobilePhone = action.mobilePhone) }
            }

            is ProfileAction.UpdateGender -> {
                _uiState.update { it.copy(gender = action.gender) }
            }

            is ProfileAction.UpdateDateOfBirth -> {
                _uiState.update { it.copy(dateOfBirth = action.dateOfBirth) }
            }

            is ProfileAction.UpdateUser -> {
                updateUser()
            }
        }
    }
}

data class UiState(
    val id: Int = 1,
    val fullName: String = "Hoàng Ngọc Linh",
    val email: String = "quangteo3k@gmail.com",
    val mobilePhone: String = "0987654321",
    val gender: String = "MALE",
    val dateOfBirth: String = "30/05/2003",
) {
    val isNoEmpty: Boolean
        get() = fullName.isNotEmpty() && dateOfBirth.isNotEmpty() && mobilePhone.isNotEmpty()
}

sealed class UpdateUserState {
    data object Pending : UpdateUserState()
    data object Loading : UpdateUserState()
    data object Success : UpdateUserState()
    data class Error(val message: String) : UpdateUserState()
}