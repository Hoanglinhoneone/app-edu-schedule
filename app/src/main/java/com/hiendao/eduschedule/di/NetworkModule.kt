package com.hiendao.eduschedule.di

import com.hiendao.eduschedule.control.datasource.remote.api.AllApi
import com.hiendao.eduschedule.control.datasource.remote.api.StatisticalApi
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.AlarmApi
import com.hiendao.eduschedule.control.datasource.remote.api.auth.AuthApi
import com.hiendao.eduschedule.control.datasource.remote.api.notification.NotificationApi
import com.hiendao.eduschedule.control.datasource.remote.api.personalWork.PersonalWorkApi
import com.hiendao.eduschedule.control.datasource.remote.api.schedule.ScheduleApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.AssignmentApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.CourseApi
import com.hiendao.eduschedule.control.datasource.remote.api.study.LessonApi
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.control.repository.remote.MyInterceptor
import com.hiendao.eduschedule.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideMyInterceptor(localStorage: LocalStorage): MyInterceptor {
        return MyInterceptor(localStorage)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(myInterceptor: MyInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(myInterceptor)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl( "http://${Constants.LOCALHOST}:8080/" )
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCourseApi(retrofit: Retrofit): CourseApi = retrofit.create(CourseApi::class.java)

    @Provides
    @Singleton
    fun provideAssignmentApi(retrofit: Retrofit): AssignmentApi =
        retrofit.create(AssignmentApi::class.java)

    @Provides
    @Singleton
    fun provideLessonApi(retrofit: Retrofit): LessonApi = retrofit.create(LessonApi::class.java)

    @Provides
    @Singleton
    fun provideStatisticalApi(retrofit: Retrofit): StatisticalApi =
        retrofit.create(StatisticalApi::class.java)
    @Provides
    @Singleton
    fun provideAlarmApi(retrofit: Retrofit): AlarmApi = retrofit.create(AlarmApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi = retrofit.create(NotificationApi::class.java)

    @Provides
    @Singleton
    fun provideAllInfoApi(retrofit: Retrofit): AllApi = retrofit.create(AllApi::class.java)

    @Provides
    @Singleton
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi = retrofit.create(ScheduleApi::class.java)

    @Provides
    @Singleton
    fun providePersonalWorkApi(retrofit: Retrofit): PersonalWorkApi =
        retrofit.create(PersonalWorkApi::class.java)
}