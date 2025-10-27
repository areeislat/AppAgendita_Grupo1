package com.example.appagendita_grupo1.repository

import com.example.appagendita_grupo1.data.local.database.UserDao
import com.example.appagendita_grupo1.data.local.database.UserEntity
import com.example.appagendita_grupo1.storage.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class UserRepository(
    private val userDao: UserDao,
    private val preferences: UserPreferences
) {

    fun observeUsers(): Flow<List<UserEntity>> = userDao.observeUsers()

    fun observeLastSession(): StateFlow<UserPreferences.LastUser?> = preferences.observeLastUser()

    suspend fun registerUser(username: String, email: String, passwordHash: String): Result<UserEntity> {
        val trimmedName = username.trim()
        val normalizedEmail = email.trim().lowercase()
        if (trimmedName.isEmpty() || normalizedEmail.isEmpty() || passwordHash.isBlank()) {
            return Result.failure(IllegalArgumentException("Los datos del usuario son inválidos"))
        }

        val entity = UserEntity(
            username = trimmedName,
            email = normalizedEmail,
            passwordHash = passwordHash
        )

        if (!entity.hasValidCredentials()) {
            return Result.failure(IllegalArgumentException("Los datos del usuario son inválidos"))
        }

        val existing = userDao.findByEmail(normalizedEmail)
        if (existing != null) {
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }

        val id = userDao.insert(entity)
        val stored = userDao.findById(id)
            ?: return Result.failure(IllegalStateException("No se pudo recuperar el usuario creado"))

        preferences.saveLastUser(stored)
        return Result.success(stored)
    }

    suspend fun authenticate(email: String, passwordHash: String): Result<UserEntity> {
        val normalizedEmail = email.trim().lowercase()
        val user = userDao.findByEmail(normalizedEmail)
            ?: return Result.failure(IllegalArgumentException("Usuario no encontrado"))

        return if (user.matchesPassword(passwordHash)) {
            preferences.saveLastUser(user)
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Contraseña incorrecta"))
        }
    }

    suspend fun updateUser(entity: UserEntity): Result<UserEntity> {
        if (!entity.hasValidCredentials()) {
            return Result.failure(IllegalArgumentException("Los datos del usuario son inválidos"))
        }
        userDao.update(entity.copy(email = entity.normalizedEmail()))
        val refreshed = userDao.findById(entity.id)
            ?: return Result.failure(IllegalStateException("No se pudo actualizar el usuario"))
        return Result.success(refreshed)
    }

    suspend fun deleteUser(entity: UserEntity) {
        userDao.delete(entity)
        val last = preferences.currentUser()
        if (last?.userId == entity.id) {
            preferences.clearLastUser()
        }
    }
}
