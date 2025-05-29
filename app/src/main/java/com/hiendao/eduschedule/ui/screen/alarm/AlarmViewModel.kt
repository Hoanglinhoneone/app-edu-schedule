package com.hiendao.eduschedule.ui.screen.alarm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.AlarmRepository
import com.hiendao.eduschedule.ui.screen.notification.NotificationUiState
import com.hiendao.eduschedule.ui.screen.notification.listNotificationItems
import com.hiendao.eduschedule.utils.Resource
import com.hiendao.eduschedule.utils.entity.Assignment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel  @Inject constructor(
    private val alarmRepository: AlarmRepository
): ViewModel() {

    private val _alarmUiState = MutableStateFlow(AlarmUiState())
    val alarmUiState: StateFlow<AlarmUiState> = _alarmUiState.asStateFlow()

    private val _updateSuccess = MutableSharedFlow<Resource<Boolean>>()
    val updateSuccess = _updateSuccess.asSharedFlow()

    private val _deleteSuccess = MutableSharedFlow<Resource<Boolean>>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    private val _addSuccess = MutableSharedFlow<Resource<Boolean>>()
    val addSuccess = _addSuccess.asSharedFlow()

    init {
        getAllAlarm()
    }

    fun getAllAlarm(){
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { result ->
                when(result){
                    is Resource.Loading -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                alarmList = result.data ?: emptyList(),
                                filterAlarmList = result.data ?: emptyList()
                            )
                        }
                    }
                    is Resource.Error -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    fun filterAlarm(filter: Int){
        _alarmUiState.update {
            it.copy(
                filterAlarmList = if(filter == 0) it.alarmList else it.alarmList.filter { it.alarmType == filter }
            )
        }
    }

    fun enableAlarm(alarmId: Int, isActive: Boolean, context: Context){
        val selectedAlarm = alarmUiState.value.alarmList.find { it.id == alarmId }
        selectedAlarm?.let { alarm ->
            alarmRepository.enableAlarm(alarm, isActive, context)
        }
    }

    fun selectAlarm(alarmId: Int){
        _alarmUiState.update {
            it.copy(
                selectedAlarm = it.alarmList.find { alarm -> alarm.id == alarmId }
            )
        }
    }

    fun addAlarm(alarmItem: AlarmItem){
        viewModelScope.launch {
            alarmRepository.addAlarm(alarmItem).collect { result ->
                when(result){
                    is Resource.Loading -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                isAddAlarmSuccess = true
                            )
                        }
                        _addSuccess.emit(Resource.Success(true))
                    }
                    is Resource.Error -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message.toString(),
                                isAddAlarmSuccess = false
                            )
                        }
                        _addSuccess.emit(Resource.Error(message = result.message.toString(), data = false))
                    }
                }
            }
        }
    }

    fun updateAlarm(alarmItem: AlarmItem){
        viewModelScope.launch {
            alarmRepository.updateAlarm(alarmItem).collect { result ->
                when(result){
                    is Resource.Loading -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                isUpdateSuccess = true
                            )
                        }
                        _updateSuccess.emit(Resource.Success(true))
                    }
                    is Resource.Error -> {
                        _alarmUiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message.toString(),
                                isUpdateSuccess = false
                            )
                        }
                        _updateSuccess.emit(Resource.Error(message = result.message.toString(), data = false))
                    }
                }
            }
        }
    }

    fun deleteAlarm(alarmId: Int){
        viewModelScope.launch {
            alarmUiState.value.selectedAlarm?.let { alarmItem ->
                alarmRepository.deleteAlarm(alarmId).collect { result ->
                    when(result){
                        is Resource.Loading -> {
                            _alarmUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        is Resource.Success -> {
                            _alarmUiState.update {
                                it.copy(isLoading = false)
                            }
                            _deleteSuccess.emit(Resource.Success(true))
                        }
                        is Resource.Error -> {
                            _alarmUiState.update {
                                it.copy(isLoading = false)
                            }
                            _deleteSuccess.emit(Resource.Error(message = result.message.toString(), data = false))
                        }
                    }
                }
            }
        }
    }
}