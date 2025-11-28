package com.example.appagendita_grupo1.di

import android.content.Context
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AgendaVirtualDatabase {
        return AgendaVirtualDatabase.getInstance(context)
    }

    @Provides
    fun provideNoteDao(database: AgendaVirtualDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideUserDao(database: AgendaVirtualDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager.getInstance(context)
    }
}