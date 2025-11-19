package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.RegistrationState
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    // Funciones 'on...Change' (sin cambios)
    fun onNameChange(name: String) {
        state = state.copy(name = name, nameError = null, generalError = null)
    }

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null, generalError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null, generalError = null)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            generalError = null
        )
    }

    /**
     * Se llama cuando el usuario presiona el botón de registrarse.
     * Registra al usuario en la API y luego guarda la sesión localmente.
     */
    fun onRegisterClick() {
        // 1. Validar los campos de la UI primero
        if (!validate()) {
            return // Si la validación falla, no continuamos
        }

        // 2. Mostrar el 'loading'
        state = state.copy(isLoading = true)

        // 3. Lanzar una corrutina para registrar en la API
        viewModelScope.launch {
            try {
                // Separar firstName y lastName del nombre completo
                val nameParts = state.name.trim().split(" ", limit = 2)
                val firstName = nameParts.firstOrNull() ?: ""
                val lastName = nameParts.getOrNull(1) ?: ""

                // 4. Crear request para la API
                val registerRequest = RegisterRequest(
                    username = state.email.trim(), // Usar email como username
                    email = state.email.trim(),
                    password = state.password,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = null
                )

                // 5. Llamar a la API
                val response = apiService.registerUser(registerRequest)

                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    
                    // 6. Guardar también localmente en Room (opcional, para modo offline)
                    val localResult = repository.registerUser(
                        name = state.name.trim(),
                        email = state.email.trim(),
                        password = state.password
                    )

                    // 7. Guardar sesión con ambos IDs (local Long y server UUID)
                    if (localResult is UserRepository.RegistrationResult.Success) {
                        sessionManager.saveSession(
                            userId = localResult.userId,
                            userEmail = userResponse.email,
                            userName = "${userResponse.firstName} ${userResponse.lastName}".trim(),
                            serverUserId = userResponse.id
                        )

                        // 8. Éxito!
                        state = state.copy(
                            registrationSuccess = true,
                            isLoading = false,
                            generalError = null
                        )
                    } else {
                        state = state.copy(
                            generalError = "Error al guardar localmente",
                            isLoading = false
                        )
                    }
                } else {
                    // Error de la API
                    val errorMessage = when (response.code()) {
                        400 -> "Datos inválidos. Verifica los campos."
                        409 -> "Este email ya está registrado"
                        500 -> "Error del servidor. Intenta más tarde."
                        else -> "Error al registrar: código ${response.code()}"
                    }
                    state = state.copy(
                        generalError = errorMessage,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // Manejar errores de conexión
                e.printStackTrace()
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "No se puede conectar al servidor. Verifica tu conexión."
                    e.message?.contains("timeout") == true -> 
                        "Tiempo de espera agotado. Intenta de nuevo."
                    else -> "Error de red: ${e.message}"
                }
                state = state.copy(
                    generalError = errorMessage,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Función de validación privada mejorada.
     */
    private fun validate(): Boolean {
        // Limpiamos errores previos
        state = state.copy(
            nameError = null,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            generalError = null
        )

        var isValid = true
        val name = state.name.trim()
        val email = state.email.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        // Validar nombre
        if (name.isBlank()) {
            state = state.copy(nameError = "El nombre no puede estar vacío")
            isValid = false
        } else if (name.length < 2) {
            state = state.copy(nameError = "El nombre debe tener al menos 2 caracteres")
            isValid = false
        }

        // Validar email
        if (email.isBlank()) {
            state = state.copy(emailError = "El email no puede estar vacío")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(emailError = "Por favor, ingresa un email válido")
            isValid = false
        }

        // Validar contraseña
        if (password.isBlank()) {
            state = state.copy(passwordError = "La contraseña no puede estar vacía")
            isValid = false
        } else if (password.length < 6) {
            state = state.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            isValid = false
        } else if (!isPasswordStrong(password)) {
            state = state.copy(
                passwordError = "La contraseña debe contener al menos una letra y un número"
            )
            isValid = false
        }

        // Validar confirmación de contraseña
        if (confirmPassword.isBlank()) {
            state = state.copy(confirmPasswordError = "Por favor, confirma tu contraseña")
            isValid = false
        } else if (password != confirmPassword) {
            state = state.copy(confirmPasswordError = "Las contraseñas no coinciden")
            isValid = false
        }

        return isValid
    }

    /**
     * Verifica que la contraseña sea fuerte.
     */
    private fun isPasswordStrong(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    /**
     * Resetea el estado del registro (útil para navegación)
     */
    fun resetState() {
        state = RegistrationState()
    }
}