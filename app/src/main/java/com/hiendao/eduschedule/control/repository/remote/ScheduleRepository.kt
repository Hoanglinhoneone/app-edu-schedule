package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.ui.screen.add.AddCourseState
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.ui.screen.home_schedule.Event
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleItem
import com.hiendao.eduschedule.utils.Resource
import com.hiendao.eduschedule.utils.entity.CourseModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun getAllSchedules(): Flow<Resource<List<Event>>>
    suspend fun getScheduleById(id: Int): ScheduleItem?
    suspend fun addSchedule(scheduleItem: ScheduleItem): Flow<AddScheduleLearningState>
    suspend fun updateSchedule(scheduleId: Int, scheduleItem: ScheduleItem): Flow<AddScheduleLearningState>
    suspend fun deleteSchedule(id: Int): Flow<AddScheduleLearningState>
    suspend fun updateStateEvent(eventId: Int, state: String, type: String): Flow<Resource<Boolean>>
}