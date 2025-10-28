package com.example.appagendita_grupo1.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Esta función es 'suspend' para ser llamada desde una corrutina.
     * Devuelve el ID de la fila recién insertada.
     */
    @Insert
    suspend fun insert(user: UserEntity): Long

    /**
     * Actualiza un usuario existente en la base de datos.
     */
    @Update
    suspend fun update(user: UserEntity)

    /**
     * Busca un usuario por su email.
     * Se usará para comprobar si un email ya está registrado.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Busca un usuario por su ID.
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    /**
     * Busca un usuario por su token de sesión.
     * Útil para validar sesiones activas.
     */
    @Query("SELECT * FROM users WHERE session_token = :sessionToken AND is_active = 1 LIMIT 1")
    suspend fun getUserBySessionToken(sessionToken: String): UserEntity?

    /**
     * Actualiza el token de sesión de un usuario.
     */
    @Query("""
        UPDATE users 
        SET session_token = :sessionToken,
            session_created_at = :sessionCreatedAt,
            session_expires_at = :sessionExpiresAt,
            updated_at = :updatedAt
        WHERE id = :userId
    """)
    suspend fun updateSessionToken(
        userId: Long,
        sessionToken: String?,
        sessionCreatedAt: Long?,
        sessionExpiresAt: Long?,
        updatedAt: Long = System.currentTimeMillis()
    )

    /**
     * Invalida la sesión de un usuario (logout).
     */
    @Query("""
        UPDATE users 
        SET session_token = NULL,
            session_created_at = NULL,
            session_expires_at = NULL,
            updated_at = :updatedAt
        WHERE id = :userId
    """)
    suspend fun clearSession(userId: Long, updatedAt: Long = System.currentTimeMillis())

    /**
     * Actualiza el perfil de un usuario.
     */
    @Query("""
        UPDATE users 
        SET name = :name,
            updated_at = :updatedAt
        WHERE id = :userId
    """)
    suspend fun updateProfile(
        userId: Long,
        name: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    /**
     * Actualiza la contraseña de un usuario.
     */
    @Query("""
        UPDATE users 
        SET password = :hashedPassword,
            updated_at = :updatedAt
        WHERE id = :userId
    """)
    suspend fun updatePassword(
        userId: Long,
        hashedPassword: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    // Nota: Eliminamos el método login(email, password) inseguro
    // La validación de contraseña se hará en el repositorio con BCrypt
}