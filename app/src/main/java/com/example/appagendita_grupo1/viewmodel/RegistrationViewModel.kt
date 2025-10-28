package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: UserRepository) : ViewModel() {

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
     * Valida los campos y luego intenta registrar al usuario en la BD con BCrypt.
     */
    fun onRegisterClick() {
        // 1. Validar los campos de la UI primero
        if (!validate()) {
            return // Si la validación falla, no continuamos
        }

        // 2. Mostrar el 'loading'
        state = state.copy(isLoading = true)

        // 3. Lanzar una corrutina para interactuar con la BD
        viewModelScope.launch {
            try {
                // 4. Registrar usuario con el repositorio (BCrypt hashing)
                when (val result = repository.registerUser(
                    name = state.name.trim(),
                    email = state.email.trim(),
                    password = state.password
                )) {
                    is UserRepository.RegistrationResult.Success -> {
                        // 5. Actualizamos el estado para indicar éxito
                        state = state.copy(
                            registrationSuccess = true,
                            isLoading = false,
                            generalError = null
                        )
                    }
                    is UserRepository.RegistrationResult.Error -> {
                        // 6. Mostrar error específico
                        if (result.message.contains("email", ignoreCase = true)) {
                            state = state.copy(
                                emailError = result.message,
                                isLoading = false
                            )
                        } else {
                            state = state.copy(
                                generalError = result.message,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar cualquier error inesperado
                e.printStackTrace()
                state = state.copy(
                    generalError = "Ocurrió un error inesperado. Por favor, inténtelo de nuevo",
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