package com.example.appagendita_grupo1.data

import android.content.Context
import androidx.core.content.edit

/**
 * Simple helper around [SharedPreferences] to persist the login session state.
 *
 * The storage is intentionally lightweight so it can be reused from Composables,
 * activities, or any other layer without introducing additional dependencies
 * such as a database. All methods are synchronous and thread-safe because the
 * platform applies the edits on a background thread when using [edit].
 */
object SessionManager {

    private const val PREFS_NAME = "appagendita_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    /**
     * Stores the login state flag.
     */
    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_IS_LOGGED_IN, loggedIn)
        }
    }

    /**
     * Returns `true` when the user has an active session.
     */
    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Clears every value associated with the session. We only store the boolean
     * flag but wiping everything ensures a clean state for future extensions.
     */
    fun clearSession(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            clear()
        }
    }
}
