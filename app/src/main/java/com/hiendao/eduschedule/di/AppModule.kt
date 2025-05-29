package com.hiendao.eduschedule.di

import com.google.gson.Gson
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmApi
import com.hiendao.eduschedule.control.datasource.database.JsonDataSource
import com.hiendao.eduschedule.control.datasource.remote.api.AllApi
import com.hiendao.eduschedule.control.datasource.remote.api.StatisticalApi
import com.hiendao.eduschedule.control.repository.local.PreferenceInfo
import com.higherstudio.calculatorlauncher.calculatorvault.vault.hidemedia.hideapp.local.LocalData
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.local.LoginRepository
import com.hiendao.eduschedule.control.repository.local.RegisterRepository
import com.hiendao.eduschedule.control.datasource.remote.api.auth.AuthApi
import com.hiendao.eduschedule.control.datasource.remote.api.notification.NotificationApi
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkApi
import com.hiendao.eduschedule.control.datasource.remote.api.schedule.ScheduleApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.AssignmentApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.CourseApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.LessonApi
import com.hiendao.eduschedule.control.repository.local.VerifyRepository
import com.hiendao.eduschedule.control.repository.remote.AlarmRepository
import com.hiendao.eduschedule.control.repository.remote.AlarmRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.AssignmentRepository
import com.hiendao.eduschedule.control.repository.remote.AssignmentRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.CourseRepository
import com.hiendao.eduschedule.control.repository.remote.CourseRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.LessonRepository
import com.hiendao.eduschedule.control.repository.remote.LessonRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.LoginRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.NotificationRepository
import com.hiendao.eduschedule.control.repository.remote.NotificationRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.PersonalWorkRepository
import com.hiendao.eduschedule.control.repository.remote.PersonalWorkRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.RegisterRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepository
import com.hiendao.eduschedule.control.repository.remote.ScheduleRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.StatisticalRepository
import com.hiendao.eduschedule.control.repository.remote.StatisticalRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.UserRepository
import com.hiendao.eduschedule.control.repository.remote.UserRepositoryImp
import com.hiendao.eduschedule.control.repository.remote.VerifyEmailRepositoryImp
import com.hiendao.eduschedule.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    @PreferenceInfo
    fun preferencesName(): String {
        return Constants.PREFERENCE_NAME
    }

    @Provides
    @Singleton
    fun provideLocalRepository(localStorage: LocalData): LocalStorage = localStorage

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun bindLoginRepository(authApi: AuthApi, localStorage: LocalData): LoginRepository{
        return LoginRepositoryImp(localStorage, authApi)
    }

    @Provides
    @Singleton
    fun bindRegisterRepository(authApi: AuthApi, localStorage: LocalData): RegisterRepository {
        return RegisterRepositoryImp(localStorage, authApi)
    }

    @Provides
    @Singleton
    fun bindVerifyRepository(authApi: AuthApi, localStorage: LocalData): VerifyRepository {
        return VerifyEmailRepositoryImp(localStorage, authApi)
    }

//    @Provides
//    @Singleton
//    fun provideCourseRepository(jsonDataSource: JsonDataSource) : CourseRepository {
//        return CourseRepositoryImp(jsonDataSource)
//    }

    @Provides
    @Singleton
    fun bindCourseRepository(jsonDataSource: JsonDataSource, courseApi: CourseApi): CourseRepository {
        return CourseRepositoryImp(jsonDataSource, courseApi)
    }

    @Provides
    @Singleton
    fun provideLessonRepository(lessonApi: LessonApi): LessonRepository {
        return LessonRepositoryImp(lessonApi)
    }
//    @Provides
//    @Singleton
//    fun provideLessonRepository(jsonDataSource: JsonDataSource): LessonRepository {
//        return LessonRepositoryImp(jsonDataSource)
//    }

    @Provides
    @Singleton
    fun provideAssignmentRepository(assignmentApi: AssignmentApi): AssignmentRepository {
        return AssignmentRepositoryImp(assignmentApi)
    }

    @Provides
    @Singleton
    fun provideStatisticalRepository(statisticalApi : StatisticalApi): StatisticalRepository {
        return StatisticalRepositoryImp(statisticalApi)
    }

    @Provides
    @Singleton
    fun bindScheduleRepository(
        allApi: AllApi,
        scheduleApi: ScheduleApi
    ): ScheduleRepository {
        return ScheduleRepositoryImp(allApi, scheduleApi)
    }

    @Provides
    @Singleton
    fun bindAlarmRepository(
        alarmApi: AlarmApi
    ): AlarmRepository {
        return AlarmRepositoryImp(alarmApi)
    }

    @Provides
    @Singleton
    fun bindNotificationRepository(
        notificationApi: NotificationApi
    ): NotificationRepository {
        return NotificationRepositoryImp(notificationApi)
    }

    @Provides
    @Singleton
    fun bindPersonalWorkRepository(
        personalWorkApi: PersonalWorkApi
    ): PersonalWorkRepository {
        return PersonalWorkRepositoryImp(personalWorkApi)
    }

    @Provides
    @Singleton
    fun bindUserRepository(allApi: AllApi, ): UserRepository {
        return UserRepositoryImp(allApi)
    }
}