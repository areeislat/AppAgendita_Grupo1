package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// --- INICIO DE CAMBIOS: IMPORTACIONES ---
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.LoginState // <- Importamos el State de 'model'
import kotlinx.coroutines.launch
// --- FIN DE CAMBIOS: IMPORTACIONES ---

// --- CAMBIO 1: Añadir Repositorio al constructor ---
class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, emailError = null, generalError = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, passwordError = null, generalError = null)
    }

    // --- INICIO DE CAMBIOS: LÓGICA DE LOGIN CON ROOM ---

    /**
     * Se llama cuando el usuario presiona el botón de Iniciar Sesión.
     * Reemplaza tu función 'login(onLoginSuccess: () -> Unit)'.
     */
    fun onLoginClick() {
        // 1. Validar campos
        if (!validate()) {
            return
        }

        // 2. Mostrar 'loading'
        state = state.copy(isLoading = true)

        // 3. Lanzar corrutina para la BD
        viewModelScope.launch {
            try {
                // 4. Intentar hacer login con el repositorio
                val user = repository.login(
                    email = state.email.trim(),
                    password = state.password
                )

                if (user != null) {
                    // 5. ¡Éxito! El usuario existe.
                    state = state.copy(
                        loginSuccess = true,
                        isLoading = false
                    )
                } else {
                    // 6. Fracaso. Email o contraseña incorrectos.
                    state = state.copy(
                        generalError = "Email o contraseña incorrectos",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // 7. Error inesperado de la BD
                e.printStackTrace()
                state = state.copy(
                    generalError = "Ocurrió un error inesperado",
                    isLoading = false
                )
            }
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

    // --- FIN DE CAMBIOS: LÓGICA DE LOGIN CON ROOM ---
}