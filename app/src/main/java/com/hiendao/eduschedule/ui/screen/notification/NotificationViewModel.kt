package com.hiendao.eduschedule.ui.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.NotificationRepository
import com.hiendao.eduschedule.utils.Resource
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {

    private val _notificationUiState = MutableStateFlow(NotificationUiState())
    val notificationUiState: StateFlow<NotificationUiState> = _notificationUiState.asStateFlow()

    init {
        getAllNotification()
    }

    fun getAllNotification(){
        viewModelScope.launch {
            notificationRepository.getAllNotification().collect { result ->
                when(result){
                    is Resource.Loading -> {
                        _notificationUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Error -> {
                        _notificationUiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message.toString()
                            )
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let {
                            _notificationUiState.update {
                                it.copy(
                                    isLoading = false,
                                    notificationList = result.data,
                                    filteredNotificationList = result.data
                                )
                            }
                        }
                    }
                }
            }
        }
//        _notificationUiState.update {
//            it.copy(
//                notificationList = listNotificationItems
//            )
//        }
    }

    fun filterNotificationByTime(type: Int){
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        when(type){
            0 -> {
                // ALl
                _notificationUiState.update {
                    it.copy(
                        filteredNotificationList = it.notificationList
                    )
                }
            }
            1 -> {
                // Today
                val startOfDay = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val endOfDay = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis

                val listNotificationItems = _notificationUiState.value.notificationList.filter {
                    it.timeNoti.convertToTimeInMillis() in startOfDay..endOfDay
                }
                _notificationUiState.update {
                    it.copy(filteredNotificationList = listNotificationItems)
                }
            }
            2 -> {
                // This week
                calendar.firstDayOfWeek = Calendar.MONDAY
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                val startOfWeek = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val endOfWeek = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis
                val listNotificationItems = _notificationUiState.value.notificationList.filter {
                    it.timeNoti.convertToTimeInMillis() in startOfWeek..endOfWeek
                }
                _notificationUiState.update {
                    it.copy(filteredNotificationList = listNotificationItems)
                }
            }
            3 -> {
                // This month
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                val startOfMonth = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val endOfMonth = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis
                val listNotificationItems = _notificationUiState.value.notificationList.filter {
                    it.timeNoti.convertToTimeInMillis() in startOfMonth..endOfMonth
                }
                _notificationUiState.update {
                    it.copy(filteredNotificationList = listNotificationItems)
                }
            }
        }

    }
}