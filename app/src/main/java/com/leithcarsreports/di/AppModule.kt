package com.leithcarsreports.di

import android.content.Context
import com.leithcarsreports.presentation.app.MainActivity
import com.leithcarsreports.services.FilePickerManager
import com.leithcarsreports.services.IFilePickerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFIlePickerManager(
        @ApplicationContext context: Context,
        @ActivityContext activity: MainActivity
    ): IFilePickerManager {
        return FilePickerManager(context,activity)
    }
}