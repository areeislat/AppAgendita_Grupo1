package com.example.appagendita_grupo1.di

import android.content.Context
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val USER_API_URL = "https://msvc-user-749990022458.us-central1.run.app/"
    private const val LOCAL_TASKS_URL = "http://10.0.2.2:8071/"
    private const val LOCAL_NOTES_URL = "http://10.0.2.2:8071/" // Assuming same as tasks

    @Provides
    @Singleton
    @UserApi // Custom qualifier
    fun provideUserApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(USER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @TaskApi // Custom qualifier
    fun provideTaskApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(LOCAL_TASKS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @NoteApi // Custom qualifier
    fun provideNoteApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(LOCAL_NOTES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AgendaVirtualDatabase {
        return AgendaVirtualDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AgendaVirtualDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: AgendaVirtualDatabase): NoteDao {
        return database.noteDao()
    }

    // UserRepository and NoteRepository are provided automatically by Hilt
    // via their @Inject constructors, so we don't need to provide them manually here.
}
