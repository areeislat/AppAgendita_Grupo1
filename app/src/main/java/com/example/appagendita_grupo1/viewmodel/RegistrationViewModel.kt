package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.model.RegistrationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    fun onNameChange(name: String) {
        state = state.copy(name = name, nameError = null)
    }

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword, confirmPasswordError = null)
    }

    fun register(onRegistrationSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            // Simulamos una demora de 2 segundos para la validación
            delay(2000)

            var hasError = false
            val name = state.name
            val email = state.email
            val password = state.password
            val confirmPassword = state.confirmPassword

            if (name.isBlank()) {
                state = state.copy(nameError = "El nombre no puede estar vacío")
                hasError = true
            }

            if (email.isBlank()) {
                state = state.copy(emailError = "El email no puede estar vacío")
                hasError = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                state = state.copy(emailError = "Por favor, ingresa un email válido")
                hasError = true
            }

            if (password.isBlank()) {
                state = state.copy(passwordError = "La contraseña no puede estar vacía")
                hasError = true
            } else if (password.length < 6) {
                state = state.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
                hasError = true
            }

            if (confirmPassword.isBlank()) {
                state = state.copy(confirmPasswordError = "Por favor, confirma tu contraseña")
                hasError = true
            } else if (password != confirmPassword) {
                state = state.copy(confirmPasswordError = "Las contraseñas no coinciden")
                hasError = true
            }

            if (!hasError) {
                onRegistrationSuccess()
            }

            state = state.copy(isLoading = false)
        }
    }
}
