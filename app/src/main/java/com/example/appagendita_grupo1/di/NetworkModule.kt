package com.example.appagendita_grupo1.di

import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class) // Este módulo vivirá mientras la app viva
object NetworkModule {

    @Provides
    @Singleton
    @Named("userApi")
    fun provideUserApiService(): ApiService = RetrofitClient.userApi

    @Provides
    @Singleton
    @Named("taskApi")
    fun provideTaskApiService(): ApiService = RetrofitClient.taskApi

    @Provides
    @Singleton
    @Named("noteApi")
    fun provideNoteApiService(): ApiService = RetrofitClient.noteApi

    @Provides
    @Singleton
    @Named("eventApi")
    fun provideEventApiService(): ApiService = RetrofitClient.eventApi
}