package com.leithcarsreports.di

import com.leithcarsreports.presentation.app.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideFIlePickerManager(
//        @ApplicationContext context: Context,
////        activity: MainActivity
//    ): IFilePickerManager {
//        return FilePickerManager(context)
//    }

    @Singleton
    @Provides
    fun provideMainActivity(
    ): MainActivity {
        return MainActivity()
    }
}
