package com.example.appagendita_grupo1.data.repository

import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

/**
 * Repositorio para gestionar las operaciones de Usuario (Login, Registro).
 * Actúa como intermediario entre los ViewModels (Auth) y el UserDao.
 */
class UserRepository(private val userDao: UserDao) {

    companion object {
        // Session duration: 30 days in milliseconds
        private const val SESSION_DURATION_MS = 30L * 24 * 60 * 60 * 1000
    }

    /**
     * Result classes for better error handling
     */
    sealed class AuthResult {
        data class Success(val user: UserEntity) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    sealed class RegistrationResult {
        data class Success(val userId: Long) : RegistrationResult()
        data class Error(val message: String) : RegistrationResult()
    }

    /**
     * Registra un nuevo usuario en la base de datos con contraseña hasheada.
     */
    suspend fun registerUser(name: String, email: String, password: String): RegistrationResult {
        return try {
            // Check if email already exists
            val existingUser = userDao.getUserByEmail(email.trim())
            if (existingUser != null) {
                return RegistrationResult.Error("El email ya está en uso")
            }

            // Hash the password using BCrypt
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

            // Create new user entity
            val newUser = UserEntity(
                name = name.trim(),
                email = email.trim(),
                password = hashedPassword,
                isActive = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            // Insert user and get ID
            val userId = userDao.insert(newUser)
            RegistrationResult.Success(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            RegistrationResult.Error("Error al registrar usuario: ${e.message}")
        }
    }

    /**
     * Busca un usuario por su email.
     * Útil para ver si un email ya está en uso durante el registro.
     */
    suspend fun getUserByEmail(email: String): UserEntity? {
        return try {
            userDao.getUserByEmail(email.trim())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Autentica a un usuario con email y contraseña.
     * Valida la contraseña usando BCrypt y crea una sesión si es exitoso.
     */
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            // Get user by email
            val user = userDao.getUserByEmail(email.trim())
                ?: return AuthResult.Error("Email o contraseña incorrectos")

            // Check if account is active
            if (!user.isActive) {
                return AuthResult.Error("Cuenta desactivada. Contacte al administrador")
            }

            // Verify password using BCrypt
            if (!BCrypt.checkpw(password, user.password)) {
                return AuthResult.Error("Email o contraseña incorrectos")
            }

            // Create session token
            val sessionToken = UUID.randomUUID().toString()
            val currentTime = System.currentTimeMillis()
            val expiresAt = currentTime + SESSION_DURATION_MS

            // Update user session
            userDao.updateSessionToken(
                userId = user.id,
                sessionToken = sessionToken,
                sessionCreatedAt = currentTime,
                sessionExpiresAt = expiresAt
            )

            // Return updated user
            val updatedUser = user.copy(
                sessionToken = sessionToken,
                sessionCreatedAt = currentTime,
                sessionExpiresAt = expiresAt
            )

            AuthResult.Success(updatedUser)
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.Error("Error al iniciar sesión: ${e.message}")
        }
    }

    /**
     * Cierra la sesión de un usuario.
     */
    suspend fun logout(userId: Long): Boolean {
        return try {
            userDao.clearSession(userId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Valida si una sesión es válida.
     */
    suspend fun validateSession(sessionToken: String): UserEntity? {
        return try {
            val user = userDao.getUserBySessionToken(sessionToken) ?: return null
            
            // Check if session has expired
            val currentTime = System.currentTimeMillis()
            if (user.sessionExpiresAt != null && user.sessionExpiresAt < currentTime) {
                // Session expired, clear it
                userDao.clearSession(user.id)
                return null
            }

            user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Actualiza el perfil de un usuario.
     */
    suspend fun updateProfile(userId: Long, name: String): Boolean {
        return try {
            userDao.updateProfile(userId, name.trim())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Cambia la contraseña de un usuario.
     */
    suspend fun changePassword(
        userId: Long,
        currentPassword: String,
        newPassword: String
    ): Boolean {
        return try {
            // Get current user
            val user = userDao.getUserById(userId) ?: return false

            // Verify current password
            if (!BCrypt.checkpw(currentPassword, user.password)) {
                return false
            }

            // Hash new password
            val hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())

            // Update password
            userDao.updatePassword(userId, hashedPassword)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}