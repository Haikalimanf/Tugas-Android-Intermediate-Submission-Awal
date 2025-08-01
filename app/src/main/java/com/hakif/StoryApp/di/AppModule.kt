package com.hakif.StoryApp.di

import android.content.Context
import com.hakif.StoryApp.data.datastore.DataStoreRepository
import com.hakif.StoryApp.data.network.retrofit.ApiConfig
import com.hakif.StoryApp.data.network.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepository(context)
    }
}