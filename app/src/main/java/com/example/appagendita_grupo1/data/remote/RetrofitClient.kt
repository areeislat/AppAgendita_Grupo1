package com.example.appagendita_grupo1.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // URLs de cada microservicio
    private const val USER_BASE_URL = "https://msvc-user-749990022458.us-central1.run.app/"
    private const val TASK_BASE_URL = "https://msvc-task-749990022458.us-central1.run.app/"
    private const val NOTE_BASE_URL = "https://msvc-note-749990022458.us-central1.run.app/"
    private const val EVENT_BASE_URL = "https://msvc-event-749990022458.us-central1.run.app/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val userApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val taskApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TASK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val noteApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NOTE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val eventApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EVENT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
