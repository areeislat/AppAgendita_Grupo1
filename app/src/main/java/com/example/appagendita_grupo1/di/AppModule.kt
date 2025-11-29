package com.example.appagendita_grupo1.di

import android.content.Context
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.local.event.EventDao
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.task.TaskDao
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

    // APIs are now provided by NetworkModule, removing duplicates

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

    @Provides
    @Singleton
    fun provideTaskDao(database: AgendaVirtualDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(database: AgendaVirtualDatabase): EventDao {
        return database.eventDao()
    }

    // UserRepository, NoteRepository, TaskRepository, and EventRepository are provided 
    // automatically by Hilt via their @Inject constructors
}
