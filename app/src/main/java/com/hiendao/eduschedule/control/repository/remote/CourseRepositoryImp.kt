package com.hiendao.eduschedule.control.repository.remote

import com.hiendao.eduschedule.control.datasource.database.JsonDataSource
import com.hiendao.eduschedule.control.datasource.remote.api.study.CourseApi
import com.hiendao.eduschedule.control.model.body.CourseBody
import com.hiendao.eduschedule.ui.screen.add.AddCourseState
import com.hiendao.eduschedule.ui.screen.study.course.CoursesUiState
import com.hiendao.eduschedule.ui.screen.study.coursedetail.CourseDetailUiState
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.utils.entity.CourseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class CourseRepositoryImp @Inject constructor(
    private val jsonDataSource: JsonDataSource,
    private val courseApi: CourseApi
) : CourseRepository {
    override suspend fun getCourses(): Flow<CoursesUiState> = flow {
        Timber.d("getCourses")
        delay(1000L)
        emit(CoursesUiState.Loading)
        try {
            val response = courseApi.getCourses()
//            val response = jsonDataSource.getCourse()
            Timber.d("response: $response")
            if (response.courses?.isNotEmpty() == true) {
                emit(CoursesUiState.Success(response.courses))
                Timber.d("success - emit: ${response.courses}")
            } else {
                emit(CoursesUiState.Error(Exception("No courses found")))
                Timber.d("null - emit: No courses found")
            }
        } catch (e: Exception) {
            Timber.d("error: ${e.message}")
            emit(CoursesUiState.Error(e))
        }
    }

    override suspend fun getCourseById(courseId: Int): Flow<CourseDetailUiState> = flow {
        Timber.i("Repo get courseById: $courseId")
        emit(CourseDetailUiState.Loading)
        try {
//            val response = jsonDataSource.getCourseById(courseId)
            val response = courseApi.getCourseById(courseId)
            delay(500L)
            Timber.d("response: $response")
            if (response.course != null) {
                emit(CourseDetailUiState.Success(response.course))
                Timber.d("success - emit: ${response.course}")
            } else {
                emit(CourseDetailUiState.Error("No courses found"))
                Timber.d("null - emit: No courses found")
            }
        } catch (e: Exception) {
            Timber.d("error: ${e.message}")
            emit(CourseDetailUiState.Error(e.message.toString()))
        }
    }

    override suspend fun createCourse(courseModel: CourseModel): Flow<AddCourseState> = flow {
        Timber.i("creating course: $courseModel")
        emit(AddCourseState.Loading)
        delay(500L)
        try {
            val response = courseApi.createCourse(
                CourseBody(
                    name = courseModel.name,
                    note = courseModel.note,
                    credits = courseModel.credits,
                    teacher = courseModel.teacher,
                    timeStart = courseModel.timeStart,
                    timeEnd = courseModel.timeEnd,
                    addressLearning = courseModel.addressLearning,
                    repeatType = courseModel.repeatType,
                    startLessonTime = courseModel.startLessonTime,
                    endLessonTime = courseModel.endLessonTime,
                    listDay = courseModel.listDay
                )
            )
//            val response = jsonDataSource.getCourseById(1352)
            Timber.d("response: $response")
            if (response.course != null) {
                Timber.d(" create course success - emit: ${response.course}")
                emit(AddCourseState.Success(response.course))
            } else {
                Timber.d("create course error - emit: No courses found")
                emit(AddCourseState.Error("No courses found"))
            }
        } catch (e: Exception) {
            Timber.d("error: ${e.message}")
            e.message?.let { AddCourseState.Error(it) }?.let { emit(it) }
            emit(AddCourseState.Error(e.message.toString()))
        }
    }

    override suspend fun deleteCourseById(courseId: Int): Flow<DeleteState> = flow {
        Timber.i("delete courseById: $courseId")
        emit(DeleteState.Loading)
        delay(1000L)

        try {
//            val response = jsonDataSource.deleteCourseById(courseId)
            val response = courseApi.deleteCourseById(courseId)
            if (response.statusCode == "200") {
                Timber.d("delete success - emit: $response")
                emit(DeleteState.Success)
            } else {
                Timber.d("delete error - ${response.statusMsg}")
                emit(DeleteState.Error("${response.statusMsg}"))
            }
        } catch (e: Exception) {
            Timber.d("delete error: ${e.message}")
            emit(DeleteState.Error(e.message.toString()))
        }
    }

    override suspend fun updateCourseById(courseId: Int, courseModel: CourseModel): Flow<CourseDetailUiState> = flow {
        Timber.i("Repo update courseById: $courseId")
        emit(CourseDetailUiState.Loading)
        delay(1000)
//        val response = jsonDataSource.getCourseById(courseId)
        try {
            val response = courseApi.updateCourseById(courseId, CourseBody(
                name = courseModel.name,
                note = courseModel.note,
                credits = courseModel.credits,
                teacher = courseModel.teacher,
                timeStart = courseModel.timeStart,
                timeEnd = courseModel.timeEnd,
                addressLearning = courseModel.addressLearning,
                repeatType = courseModel.repeatType,
                listDay = courseModel.listDay,
            ))
            Timber.d("response: $response")
            if (response.course != null) {
                Timber.d("update success - emit: ${response.course}")
                emit(CourseDetailUiState.Success(response.course))
            } else {
                Timber.d("update error - emit: No courses found")
                emit(CourseDetailUiState.Error("No courses found"))
            }
        } catch (e: Exception) {
            Timber.d("update error: ${e.message}")
            emit(CourseDetailUiState.Error(e.toString()))
        }
    }
}

interface CourseRepository {
    suspend fun getCourses(): Flow<CoursesUiState>
    suspend fun getCourseById(courseId: Int): Flow<CourseDetailUiState>
    suspend fun createCourse(courseModel: CourseModel): Flow<AddCourseState>
    suspend fun deleteCourseById(courseId: Int): Flow<DeleteState>
    suspend fun updateCourseById(courseId: Int, courseModel: CourseModel): Flow<CourseDetailUiState>
}