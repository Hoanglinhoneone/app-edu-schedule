package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.AllApi
import com.hiendao.eduschedule.control.datasource.remote.api.schedule.CreateScheduleBody
import com.hiendao.eduschedule.control.datasource.remote.api.schedule.ScheduleApi
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.ui.screen.home_schedule.Event
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleItem
import com.hiendao.eduschedule.ui.screen.home_schedule.toEvent
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.Resource
import com.hiendao.eduschedule.utils.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ScheduleRepositoryImp @Inject constructor(
    private val allApi: AllApi,
    private val scheduleApi: ScheduleApi
) : ScheduleRepository {
    override suspend fun getAllSchedules(): Flow<Resource<List<Event>>> {
        return flow {

            try {
                emit(Resource.Loading())
                val listItem = allApi.getAllUserInfo()
                Timber.tag("ScheduleRepository").d("received: ${listItem}")
                val listEvents = mutableListOf<Event>()
                listEvents.addAll(listItem.scheduleLearningList.map { it.toEvent() })
                listEvents.addAll(listItem.personalWorkList.map { it.toEvent() })
                listEvents.addAll(listItem.assignmentList.map { it.toEvent() })
                emit(Resource.Success(data = listEvents))
                DataSource.user = User(
                    id = listItem.id,
                    fullName = listItem.fullName ?: "",
                    email = listItem.email ?: "",
                    mobilePhone = listItem.mobilePhone ?: "",
                    gender = listItem.gender ?: "",
                    dateOfBirth = listItem.dateOfBirth ?: "",
                    listAlarm = listItem.alarmList,
                    listCourse = listItem.courseList
                )
            } catch (e: Exception) {
                Timber.tag("ScheduleRepository").e("Error fetching schedules: ${e.message}")
                emit(Resource.Error(message = e.message.toString()))
            }
        }
    }

    override suspend fun getScheduleById(id: Int): ScheduleItem? {
        return null
    }

    override suspend fun addSchedule(scheduleItem: ScheduleItem): Flow<AddScheduleLearningState> {
        return flow {
            try {
                val response = scheduleApi.createSchedule(
                    CreateScheduleBody(
                        name = scheduleItem.name,
                        note = scheduleItem.note,
                        timeStart = scheduleItem.timeStart,
                        timeEnd = scheduleItem.timeEnd,
                        teacher = scheduleItem.teacher,
                        learningAddresses = scheduleItem.learningAddresses
                    )
                )
                Timber.d("response: $response")
                if (response.scheduleLearning != null) {
                    Timber.d(" create schedule learning success - emit: ${response.scheduleLearning}")
                    emit(AddScheduleLearningState.Success(response.scheduleLearning))
                } else {
                    Timber.d("create schedule learning error - emit: No courses found")
                    emit(AddScheduleLearningState.Error("No courses found"))
                }
            } catch (e: Exception) {
                Timber.tag("ScheduleRepository").e("Error adding schedule: ${e.message}")
                emit(AddScheduleLearningState.Error(e.message.toString()))
            }
        }
    }

    override suspend fun updateSchedule(
        scheduleId: Int,
        scheduleItem: ScheduleItem
    ): Flow<AddScheduleLearningState> {
        return flow {
            try {
                val response = scheduleApi.updateSchedule(
                    id = scheduleId,
                    CreateScheduleBody(
                        name = scheduleItem.name,
                        note = scheduleItem.note,
                        timeStart = scheduleItem.timeStart,
                        timeEnd = scheduleItem.timeEnd,
                        teacher = scheduleItem.teacher,
                        learningAddresses = scheduleItem.learningAddresses,
                        state = scheduleItem.state
                    )
                )
                Timber.d("response: $response")
                if (response.scheduleLearning != null) {
                    Timber.d(" update schedule learning success - emit: ${response.scheduleLearning}")
                    emit(AddScheduleLearningState.Success(response.scheduleLearning))
                } else {
                    Timber.d("update schedule learning error - emit: No courses found")
                    emit(AddScheduleLearningState.Error("No courses found"))
                }
            } catch (e: Exception) {
                Timber.tag("ScheduleRepository").e("Error update schedule: ${e.message}")
                emit(AddScheduleLearningState.Error(e.message.toString()))
            }
        }
    }

    override suspend fun deleteSchedule(id: Int): Flow<AddScheduleLearningState> {
        return flow {
            try {
                val response = scheduleApi.deleteSchedule(id)
                Timber.d("response: $response")
                if (response.scheduleLearning != null) {
                    Timber.d(" delete schedule learning success - emit: ${response.scheduleLearning}")
                    emit(AddScheduleLearningState.Success(response.scheduleLearning))
                } else {
                    Timber.d("delete schedule learning error - emit: No courses found")
                    emit(AddScheduleLearningState.Error("No courses found"))
                }
            } catch (e: Exception) {
                Timber.tag("ScheduleRepository").e("Error delete schedule: ${e.message}")
                emit(AddScheduleLearningState.Error(e.message.toString()))
            }
        }
    }

    override suspend fun updateStateEvent(
        eventId: Int,
        state: String,
        type: String
    ): Flow<Resource<Boolean>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = allApi.updateEventState(eventId, type, state)
                if(response.statusCode != null){
                    emit(Resource.Success(true))
                } else {
                    emit(Resource.Error("Error updating event state"))
                }
            } catch (e : Exception){
                Timber.tag("ScheduleRepository").e("Error updating event state: ${e.message}")
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}