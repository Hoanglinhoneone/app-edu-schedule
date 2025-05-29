package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.remote.api.StatisticalApi
import com.hiendao.eduschedule.control.model.CourseStatsResponse
import com.hiendao.eduschedule.control.model.body.CourseStatsBody
import com.hiendao.eduschedule.ui.screen.stats.CourseStatsState
import com.hiendao.eduschedule.utils.entity.CourseStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class StatisticalRepositoryImp @Inject constructor(
    private val statisticalApi: StatisticalApi,
)  : StatisticalRepository {

    override suspend fun getCoursesStats(timeStart : String, timeEnd: String): Flow<CourseStatsState> = flow {
        Timber.i("repo get courses stats with: ")
        emit(CourseStatsState.Loading)
        try {
            val response = statisticalApi.getCourseStats(
                CourseStatsBody(timeStart = timeStart, timeEnd =  timeEnd)
            )
            Timber.i("repo get courses stats success : $response")
            emit(CourseStatsState.Success(CourseStats(
                timeStart = response.timeStart ?: "",
                timeEnd = response.timeEnd ?: "",
                courseStatsInformation = response.courseResponseDtoList,
                present = response.present ?: 0,
                absent = response.absent ?: 0
            )))
        } catch (e: Exception) {
            Timber.i("repo get courses stats error: ${e.message}")
            emit(CourseStatsState.Error(e.message ?: "Unknown error"))
            e.printStackTrace()
        }
    }
}

interface StatisticalRepository {
    suspend fun getCoursesStats(timeStart : String, timeEnd: String) : Flow<CourseStatsState>
}