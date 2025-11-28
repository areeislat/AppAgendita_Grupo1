package com.example.appagendita_grupo1.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * SessionManager handles user session persistence using EncryptedSharedPreferences
 * for secure storage of session data.
 *
 * UPDATED: Now uses String (UUID) for userId to match backend.
 */
class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        // Create MasterKey for encryption
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // Initialize EncryptedSharedPreferences for secure storage
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Save user session after successful login/registration
     *
     * CAMBIO: userId ahora es String (UUID) y es obligatorio.
     * Eliminamos serverUserId porque userId YA ES el del servidor.
     *
     * @param userId Backend UUID (String)
     * @param authToken JWT Token
     */
    fun saveSession(userId: String, userEmail: String, userName: String, authToken: String? = null) {
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, userId) // Guardamos UUID como String
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_NAME, userName)
            putBoolean(KEY_IS_LOGGED_IN, true)
            authToken?.let { putString(KEY_AUTH_TOKEN, it) }
            apply()
        }
    }

    /**
     * Get authentication token
     */
    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Get current logged-in user ID (UUID from Backend)
     *
     * CAMBIO: Ahora devuelve String? en lugar de Long?
     */
    fun getCurrentUserId(): String? {
        if (!isLoggedIn()) return null
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    /**
     * Get current logged-in user email
     */
    fun getCurrentUserEmail(): String? {
        if (!isLoggedIn()) return null
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    /**
     * Get current logged-in user name
     */
    fun getCurrentUserName(): String? {
        if (!isLoggedIn()) return null
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    /**
     * Check if user is currently logged in
     */
    fun isLoggedIn(): Boolean {
        // Verificamos el flag y que exista un ID válido
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false) &&
                sharedPreferences.contains(KEY_USER_ID)
    }

    /**
     * Clear session data on logout
     */
    fun clearSession() {
        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            // remove(KEY_SERVER_USER_ID) // Ya no existe
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_NAME)
            remove(KEY_AUTH_TOKEN)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    /**
     * Validate if the session token is still valid
     */
    fun isSessionValid(): Boolean {
        return isLoggedIn() && getCurrentUserId() != null
    }

    companion object {
        private const val PREFS_NAME = "agenda_virtual_session"

        // Claves de Preferencias
        private const val KEY_USER_ID = "user_id" // Ahora guardará String
        // private const val KEY_SERVER_USER_ID = "server_user_id" // ELIMINADO
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_AUTH_TOKEN = "auth_token"

        @Volatile
        private var INSTANCE: SessionManager? = null

        /**
         * Get singleton instance of SessionManager
         */
        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}