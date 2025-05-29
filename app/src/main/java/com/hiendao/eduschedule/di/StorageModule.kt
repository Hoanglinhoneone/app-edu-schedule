package com.hiendao.eduschedule.di

import android.content.Context
import com.hiendao.eduschedule.control.datasource.database.JsonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideJsonDataSource(@ApplicationContext context: Context): JsonDataSource {
        return JsonDataSource(context)
    }
}