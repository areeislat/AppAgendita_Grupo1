package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.LoginState
import com.example.appagendita_grupo1.utils.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null, generalError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null, generalError = null)
    }

    /**
     * Se llama cuando el usuario presiona el botón de Iniciar Sesión.
     * Implementa autenticación segura con BCrypt y gestión de sesiones.
     */
    fun onLoginClick() {
        // 1. Validar campos
        if (!validate()) {
            return
        }

        // 2. Mostrar 'loading'
        state = state.copy(isLoading = true)

        // 3. Lanzar corrutina para la autenticación
        viewModelScope.launch {
            try {
                // 4. Intentar hacer login con el repositorio (BCrypt validation)
                when (val result = repository.login(
                    email = state.email.trim(),
                    password = state.password
                )) {
                    is UserRepository.AuthResult.Success -> {
                        // 5. ¡Éxito! El usuario está autenticado - guardar sesión
                        sessionManager.saveSession(
                            userId = result.user.id,
                            userEmail = result.user.email,
                            userName = result.user.name
                        )
                        
                        state = state.copy(
                            loginSuccess = true,
                            isLoading = false,
                            generalError = null
                        )
                    }
                    is UserRepository.AuthResult.Error -> {
                        // 6. Fracaso. Mostrar error específico.
                        state = state.copy(
                            generalError = result.message,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                // 7. Error inesperado
                e.printStackTrace()
                state = state.copy(
                    generalError = "Ocurrió un error inesperado: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    /**
     * Check if there's an existing session and auto-login
     */
    fun checkExistingSession() {
        if (sessionManager.isSessionValid()) {
            state = state.copy(loginSuccess = true)
        }
    }

    /**
     * Validación simple de los campos de login.
     */
    private fun validate(): Boolean {
        // Limpiamos errores previos
        state = state.copy(
            emailError = null,
            passwordError = null,
            generalError = null
        )

        var isValid = true

        if (state.email.isBlank()) {
            state = state.copy(emailError = "El email no puede estar vacío")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            state = state.copy(emailError = "Email no válido")
            isValid = false
        }

        if (state.password.isBlank()) {
            state = state.copy(passwordError = "La contraseña no puede estar vacía")
            isValid = false
        }

        return isValid
    }
}
