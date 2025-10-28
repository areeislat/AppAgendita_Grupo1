package com.example.appagendita_grupo1.data.repository

import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity

/**
 * Repositorio para gestionar las operaciones de Usuario (Login, Registro).
 * Actúa como intermediario entre los ViewModels (Auth) y el UserDao.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Registra un nuevo usuario en la base de datos.
     * Llama a la función 'suspend' del DAO.
     */
    suspend fun registerUser(user: UserEntity) {
        userDao.insert(user)
    }

    /**
     * Busca un usuario por su email.
     * Útil para ver si un email ya está en uso durante el registro.
     */
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Intenta loguear a un usuario con email y contraseña.
     * Devuelve el UserEntity si la combinación es correcta, o null si no lo es.
     */
    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
}