package com.example.appagendita_grupo1.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // Insertar o actualizar usuario (para caché).
    // Al usar UUID como PK, no devuelve Long (autonumérico), así que lo dejamos sin retorno o Unit.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    // Buscar por email (útil para validaciones rápidas o UI)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // Buscar por ID (UUID String)
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    // Borrar todos los usuarios (útil al hacer logout para limpiar caché privada)
    @Query("DELETE FROM users")
    suspend fun clearUsers()

    // (Opcional) Actualizar nombre si se edita perfil
    @Query("UPDATE users SET name = :name WHERE id = :userId")
    suspend fun updateUserName(userId: String, name: String)
}