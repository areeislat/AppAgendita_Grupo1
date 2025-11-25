package com.example.appagendita_grupo1.di

import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Este módulo vivirá mientras la app viva
object NetworkModule {

    @Provides
    @Singleton // Crea una única instancia para toda la app
    fun provideApiService(): ApiService {
        return RetrofitClient.instance
    }
}