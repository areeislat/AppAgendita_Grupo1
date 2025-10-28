package com.example.appagendita_grupo1.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
     * Busca un usuario por su email.
     * Se usará para comprobar si un email ya está registrado.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Busca un usuario por email y contraseña.
     * Se usará para la lógica de Login.
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    // Nota: No necesitamos un Flow<List<UserEntity>>
    // porque nunca vamos a mostrar una lista de todos los usuarios.
}