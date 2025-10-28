package com.example.appagendita_grupo1

import android.app.Application
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.utils.SessionManager

/**
 * Custom Application class for centralized dependency management.
 * This provides singleton instances of database, repositories, and session manager
 * that can be accessed throughout the application lifecycle.
 */
class AgendaVirtualApp : Application() {

    // Lazy initialization of database - created only when first accessed
    val database: AgendaVirtualDatabase by lazy {
        AgendaVirtualDatabase.getInstance(this)
    }

    // Lazy initialization of SessionManager - created only when first accessed
    val sessionManager: SessionManager by lazy {
        SessionManager.getInstance(this)
    }

    // Lazy initialization of repositories
    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao())
    }

    val noteRepository: NoteRepository by lazy {
        NoteRepository(database.noteDao(), sessionManager)
    }

    override fun onCreate() {
        super.onCreate()
        // Any application-level initialization can go here
        // For example, initializing logging, analytics, etc.
    }

    companion object {
        private const val TAG = "AgendaVirtualApp"
    }
}
