package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.PersonalWork
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkBody
import com.hiendao.eduschedule.ui.screen.add.AddPersonalWorkState
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PersonalWorkRepository {
    suspend fun createPersonalWork(personalWork: PersonalWorkBody):  Flow<AddPersonalWorkState>
}