package com.example.appagendita_grupo1.data.repository

import android.util.Log
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.LoginRequest
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.response.LoginResponse
import com.example.appagendita_grupo1.data.remote.response.UserResponse
import com.example.appagendita_grupo1.di.UserApi
import javax.inject.Inject

/**
 * Repositorio encargado de la autenticación y gestión de usuarios vía API.
 * Mantiene una caché local básica mediante UserDao.
 */
import javax.inject.Named

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    @Named("userApi") private val apiService: ApiService
) {

    companion object {
        private const val TAG = "UserRepository"
    }

    // Clases selladas para manejar los resultados de la UI de forma limpia
    sealed class AuthResult {
        data class Success(val response: LoginResponse) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    sealed class RegistrationResult {
        data class Success(val user: UserResponse) : RegistrationResult()
        data class Error(val message: String) : RegistrationResult()
    }

    /**
     * Inicia sesión en el backend.
     */
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            Log.d(TAG, "Intentando login para: $email")
            val request = LoginRequest(usernameOrEmail = email, password = password)
            val response = apiService.loginUser(request)

            Log.d(TAG, "Respuesta recibida - Código: ${response.code()}, Exitosa: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                Log.d(TAG, "Login exitoso - Usuario ID: ${loginResponse.user.id}")

                // Guardar usuario en caché local (SQLite) para uso offline básico
                saveUserToCache(loginResponse.user)

                AuthResult.Success(loginResponse)
            } else {
                // Intentar leer el mensaje de error del cuerpo, si existe
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                val httpCode = response.code()
                Log.e(TAG, "Error HTTP $httpCode: $errorMsg")
                AuthResult.Error("Error al iniciar sesión (código $httpCode): $errorMsg")
            }
        } catch (e: com.google.gson.JsonSyntaxException) {
            // Error específico de parseo JSON
            Log.e(TAG, "Error de parseo JSON", e)
            AuthResult.Error("Error de formato en la respuesta del servidor: ${e.message}")
        } catch (e: java.net.UnknownHostException) {
            // Error de conexión - servidor no encontrado
            Log.e(TAG, "Servidor no encontrado", e)
            AuthResult.Error("No se pudo conectar al servidor. Verifica tu conexión.")
        } catch (e: java.net.SocketTimeoutException) {
            // Timeout
            Log.e(TAG, "Timeout en la conexión", e)
            AuthResult.Error("Tiempo de espera agotado. Intenta nuevamente.")
        } catch (e: Exception) {
            // Cualquier otro error
            Log.e(TAG, "Error inesperado en login", e)
            AuthResult.Error("Error inesperado: ${e.javaClass.simpleName} - ${e.message}")
        }
    }

    /**
     * Registra un usuario en el backend.
     * Nota: Asumimos que 'name' se usa para First Name y Username por simplicidad.
     */
    suspend fun registerUser(name: String, email: String, password: String): RegistrationResult {
        return try {
            val request = RegisterRequest(
                username = email.split("@")[0], // Generar username basado en email temporalmente
                email = email,
                password = password,
                firstName = name,
                lastName = "" // Opcional
            )

            val response = apiService.registerUser(request)

            if (response.isSuccessful && response.body() != null) {
                RegistrationResult.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                RegistrationResult.Error("Error al registrar: $errorMsg")
            }
        } catch (e: Exception) {
            RegistrationResult.Error("Error de conexión: ${e.message}")
        }
    }

    // Helper para guardar en Room
    private suspend fun saveUserToCache(userDto: UserResponse) {
        val userEntity = UserEntity(
            id = userDto.id, // UUID String
            name = "${userDto.firstName} ${userDto.lastName}".trim(),
            email = userDto.email
        )
        userDao.insert(userEntity)
    }
}