package com.hiendao.eduschedule.ui.screen.home_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepository
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.Resource
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.getMonthYearFromMillis
import com.hiendao.eduschedule.utils.getStartOfDayTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
): ViewModel() {

    private val _scheduleUiState : MutableStateFlow<ScheduleUiState> = MutableStateFlow(ScheduleUiState())
    val scheduleUiState: StateFlow<ScheduleUiState> = _scheduleUiState.asStateFlow()

    private val _changeStateEvent: MutableSharedFlow<UpdateStateEventResource?> = MutableSharedFlow()
    val changeStateEvent: SharedFlow<UpdateStateEventResource?> = _changeStateEvent.asSharedFlow()


    fun getAllSchedules(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                scheduleRepository.getAllSchedules().collect { result ->
                    when(result){
                        is Resource.Loading -> {
                            _scheduleUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        is Resource.Success -> {
                            val startOfDay = getStartOfDayTimestamp()
                            _scheduleUiState.update {
                                it.copy(
                                    isLoading = false,
                                    listSchedule = result.data?.toMutableList() ?: mutableListOf(),
                                    currentEvents = sortItemsByTime(result.data?.filterEventsOnSameDay(startOfDay) ?: emptyList()) ,
                                    filterEvent = result.data?.toMutableList() ?: mutableListOf(),
                                    listTimeDate = result.data?.map { it.timeStart?.convertToTimeInMillis() ?: 0 }?.toMutableList() ?: mutableListOf(),
                                    selectedTime = startOfDay
                                )
                            }
                        }
                        is Resource.Error -> {
                            _scheduleUiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMsg = result.message
                                )
                            }
                            Timber.tag("ScheduleViewModel").e("Error fetching schedules: ${result.message}")
                        }
                    }
                }
            }
        }
    }

    fun sortItemsByTime(events: List<Event>): MutableList<Event> {
        return events.sortedBy { event ->
            val startTime = event.timeStart?.convertToTimeInMillis() ?: 0L
            val endTime = event.timeEnd?.convertToTimeInMillis() ?: 0L
            if (startTime != 0L) {
                startTime
            } else {
                endTime
            }
        }.toMutableList()
    }

    fun get7DaysFromNow() {
        val startTime = getStartOfDayTimestamp()
        val currentMonthAndYear = getMonthYearFromMillis(startTime)
        get7DaysFromDate(startTime).let { listDate ->
            _scheduleUiState.update {
                it.copy(
                    currentTimeList = listDate.toMutableList(),
                    currentMonthAndYear = currentMonthAndYear,
                    selectedTime = startTime
                )
            }
        }
    }

    fun updateTimes(time: Long){
        val currentMonthAndYear = getMonthYearFromMillis(time)
        get7DaysFromDate(time).let { listDate ->
            _scheduleUiState.update {
                it.copy(
                    currentTimeList = listDate.toMutableList(),
                    currentMonthAndYear = currentMonthAndYear,
                    currentEvents = sortItemsByTime(it.filterEvent.filterEventsOnSameDay(time)),
                    selectedTime = time
                )
            }
        }
    }

    fun filterByCategory(type: Int){
        _scheduleUiState.update {
            it.copy(
                filterEvent = it.listSchedule.filterByCategory(type).toMutableList(),
                currentEvents = sortItemsByTime(it.listSchedule.filterByCategory(type).filterEventsOnSameDay(it.selectedTime))
            )
        }
    }

    fun filterByTime(time: Long){
        _scheduleUiState.update {
            it.copy(
                currentEvents = sortItemsByTime(it.filterEvent.filterEventsOnSameDay(time)),
                selectedTime = time
            )
        }
    }

    private fun get7DaysFromDate(date: Long): List<DateItem> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        val listDate = mutableListOf<DateItem>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dayFormat = SimpleDateFormat("E", Locale.getDefault())
        for (i in 0 until 7) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val currentDate = calendar.timeInMillis
            listDate.add(DateItem(currentDate, dateFormat.format(Date(currentDate)), dayFormat.format(
                Date(currentDate)
            )))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        println("List Date: $listDate")
        return listDate
    }

    private fun List<Event>.filterEventsOnSameDay(currentDate: Long): List<Event> {
        val startOfDay = currentDate
        val endOfDay = currentDate + 86_399_999 // 1 ngày trừ 1ms

        return this.filter { event ->
            event.date in startOfDay..endOfDay || event.endDate in startOfDay..endOfDay
        }
    }

    fun List<Event>.filterByCategory(index: Int): List<Event> {
        return when (index) {
            0 -> this // All
            1 -> this.filter { it.type == ScheduleType.LESSON }
            2 -> this.filter { it.type == ScheduleType.ASSIGNMENT }
            3 -> this.filter { it.type == ScheduleType.PERSONAL_WORK }
            else -> emptyList() // nếu index không hợp lệ
        }
    }

    fun updateEvent(event: Event, isJoined: Boolean){
        viewModelScope.launch {
            val type = event.type
            val state = if(isJoined){
                when(type){
                    ScheduleType.LESSON -> Constants.ScheduleLearningState.PRESENT
                    ScheduleType.ASSIGNMENT -> Constants.AssignmentPersonalState.COMPLETE
                    ScheduleType.PERSONAL_WORK -> Constants.AssignmentPersonalState.COMPLETE
                }
            } else {
                when(type){
                    ScheduleType.LESSON -> Constants.ScheduleLearningState.ABSENT
                    ScheduleType.ASSIGNMENT -> Constants.AssignmentPersonalState.INCOMPLETED
                    ScheduleType.PERSONAL_WORK -> Constants.AssignmentPersonalState.INCOMPLETED
                }
            }
            scheduleRepository.updateStateEvent(event.id, state, type.toString()).collect { result ->
                when(result){
                    is Resource.Loading -> {
                        _changeStateEvent.emit(UpdateStateEventResource(isLoading = true))
                    }
                    is Resource.Error -> {
                        _changeStateEvent.emit(UpdateStateEventResource(isLoading = false, errorMsg = result.message))
                        Timber.tag("ScheduleViewModel").e("Error updating event state: ${result.message}")
                    }
                    is Resource.Success -> {
                        _changeStateEvent.emit(UpdateStateEventResource(isLoading = false, isSuccess = result.data))
                    }
                }
            }
        }
    }
}

data class UpdateStateEventResource(
    val isLoading: Boolean? = null,
    val isSuccess: Boolean? = null,
    val errorMsg: String? = null
)