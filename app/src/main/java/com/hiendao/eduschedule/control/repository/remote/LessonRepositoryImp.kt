package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.database.JsonDataSource
import com.hiendao.eduschedule.control.datasource.remote.api.study.LessonApi
import com.hiendao.eduschedule.control.model.body.UpdateLessonBody
import com.hiendao.eduschedule.ui.screen.study.lesson.LessonState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.LessonDetailState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.UpdateState
import com.hiendao.eduschedule.utils.entity.LessonModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LessonRepositoryImp @Inject constructor(
//    private val jsonDataSource: JsonDataSource,
    private val lessonApi: LessonApi
) : LessonRepository {
    override suspend fun getAllLessons(): Flow<LessonDetailState> = flow {

    }

    override suspend fun getLessons(courseId: Int): Flow<LessonDetailState> = flow {
    }

    override suspend fun getLesson(lessonId: Int): Flow<LessonDetailState> = flow {
        Timber.i("get Lesson by id:$lessonId")
        emit(LessonDetailState.Loading)
        delay(1000)
        try {
            val response = lessonApi.getLessonById(lessonId)
//            val response = jsonDataSource.getLessonById(lessonId)
            if (response.lesson != null) {
                Timber.d("get Lesson success: ${response.lesson}")
                emit(LessonDetailState.Success(response.lesson))
            } else {
                Timber.d("get Lesson error: ${response.errorMessage}")
                emit(LessonDetailState.Error("Not found Lesson"))
            }
        } catch (e:Exception) {
            Timber.d("get Lesson error: ${e.message}")
            emit(LessonDetailState.Error(e.message.toString()))
        }
    }

    override suspend fun deleteLesson(lessonId: Int): Flow<DeleteState> = flow {
        Timber.i("delete Lesson by id:$lessonId")
        emit(DeleteState.Loading)
        delay(1000L)
        try {
            val response = lessonApi.deleteLesson(lessonId)
            if(response.statusCode == "200") {
                Timber.d("delete Lesson success: ${response.statusMsg}")
                emit(DeleteState.Success)
            } else {
                Timber.d("delete Lesson error: ${response.statusMsg}")
                emit(DeleteState.Error("Not found Lesson"))
            }
        } catch (e:Exception) {
            Timber.d("delete Lesson error: ${e.message}")
            emit(DeleteState.Error(e.message.toString()))
        }
    }

    override suspend fun updateLesson(lesson: LessonModel): Flow<UpdateState> = flow {
        Timber.i("update Lesson: $lesson")
        emit(UpdateState.Loading)
        delay(1000L)
        try {
            val response = lessonApi.updateLesson(lesson.id, UpdateLessonBody(
                name = lesson.name,
                note = lesson.note,
                timeStart = lesson.startTime,
                timeEnd = lesson.endTime,
                teacher = lesson.teacher,
                learningAddresses = lesson.address,
                state = lesson.state,
            ))
            if(response.lesson != null) {
                Timber.d("update Lesson success: ${response.lesson}")
                emit(UpdateState.Success(response.lesson))
            } else {
                Timber.d("update Lesson error: ${response.errorMessage}")
                emit(UpdateState.Error("Not found Lesson"))
            }
        } catch (e: Exception) {
            Timber.d("update Lesson error: ${e.message}")
            emit(UpdateState.Error(e.message.toString()))
        }
    }
}

interface LessonRepository {
    suspend fun getAllLessons(): Flow<LessonDetailState>
    suspend fun getLessons(courseId: Int): Flow<LessonDetailState>
    suspend fun getLesson(lessonId: Int): Flow<LessonDetailState>
    suspend fun deleteLesson(lessonId: Int): Flow<DeleteState>
    suspend fun updateLesson(lesson: LessonModel): Flow<UpdateState>

}