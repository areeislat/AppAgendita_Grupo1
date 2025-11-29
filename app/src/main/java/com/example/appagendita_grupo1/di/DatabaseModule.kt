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

    // Eliminado provideDatabase para evitar duplicidad con AppModule

    // Eliminado provideNoteDao para evitar duplicidad con AppModule

    // Eliminado provideUserDao para evitar duplicidad con AppModule

    // Eliminado provideSessionManager para evitar duplicidad con AppModule
}