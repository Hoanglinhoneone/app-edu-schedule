package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.PersonalWork
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkApi
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkBody
import com.hiendao.eduschedule.ui.screen.add.AddPersonalWorkState
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonalWorkRepositoryImp @Inject constructor(
    private val personalWorkApi: PersonalWorkApi
): PersonalWorkRepository {
    override suspend fun createPersonalWork(personalWork: PersonalWorkBody):  Flow<AddPersonalWorkState> {
        return flow {
            try {
                emit(AddPersonalWorkState.Loading)
                val response = personalWorkApi.createPersonalWork(personalWork)
                if(response.personalWork != null){
                    emit(AddPersonalWorkState.Success(response.personalWork))
                } else {
                    emit(AddPersonalWorkState.Error("Create personal work failed"))
                }
            } catch (e : Exception) {
                emit(AddPersonalWorkState.Error(e.message.toString()))
            }
        }
    }
}