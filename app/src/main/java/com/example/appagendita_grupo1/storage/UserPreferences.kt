package com.example.appagendita_grupo1.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.appagendita_grupo1.data.local.database.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val lastUserState = MutableStateFlow(readLastUser())

    fun observeLastUser(): StateFlow<LastUser?> = lastUserState.asStateFlow()

    fun currentUser(): LastUser? = lastUserState.value

    fun saveLastUser(user: UserEntity) {
        val lastUser = LastUser(user.id, user.username, user.email)
        sharedPreferences.edit()
            .putLong(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.username)
            .putString(KEY_USER_EMAIL, user.email)
            .apply()
        lastUserState.value = lastUser
    }

    fun clearLastUser() {
        sharedPreferences.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_EMAIL)
            .apply()
        lastUserState.value = null
    }

    private fun readLastUser(): LastUser? {
        val id = sharedPreferences.getLong(KEY_USER_ID, DEFAULT_ID)
        if (id == DEFAULT_ID) return null
        val name = sharedPreferences.getString(KEY_USER_NAME, null) ?: return null
        val email = sharedPreferences.getString(KEY_USER_EMAIL, null) ?: return null
        return LastUser(id, name, email)
    }

    data class LastUser(
        val userId: Long,
        val username: String,
        val email: String
    )

    companion object {
        private const val PREFS_NAME = "appagendita_user_prefs"
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USER_NAME = "key_user_name"
        private const val KEY_USER_EMAIL = "key_user_email"
        private const val DEFAULT_ID = -1L
    }
}
