package com.example.appagendita_grupo1.viewmodel

import android.util.Patterns // Mantenemos tu import de Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// --- INICIO DE CAMBIOS: IMPORTACIONES ---
import com.example.appagendita_grupo1.data.local.user.UserEntity
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.model.RegistrationState // <- Importamos el State de 'model'
import kotlinx.coroutines.launch
// --- FIN DE CAMBIOS: IMPORTACIONES ---

// --- CAMBIO 1: Añadir Repositorio al constructor ---
class RegistrationViewModel(private val repository: UserRepository) : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    // Funciones 'on...Change' (sin cambios)
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

    // --- INICIO DE CAMBIOS: LÓGICA DE REGISTRO CON ROOM ---

    /**
     * Se llama cuando el usuario presiona el botón de registrarse.
     * Valida los campos y luego intenta registrar al usuario en la BD.
     * Reemplaza tu función 'register(onRegistrationSuccess: () -> Unit)'.
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
                // 4. Comprobar si el usuario ya existe (usamos trim para limpiar)
                val existingUser = repository.getUserByEmail(state.email.trim())
                if (existingUser != null) {
                    // El email ya está en uso, mostramos un error
                    state = state.copy(
                        emailError = "El email ya está en uso",
                        isLoading = false
                    )
                } else {
                    // 5. El email está disponible, creamos la entidad
                    val newUser = UserEntity(
                        name = state.name.trim(),
                        email = state.email.trim(),
                        password = state.password // (En una app real, esto debería estar hasheado)
                    )

                    // 6. Insertamos el usuario en la BD
                    repository.registerUser(newUser)

                    // 7. Actualizamos el estado para indicar éxito
                    state = state.copy(
                        registrationSuccess = true,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // Manejar cualquier error inesperado de la BD
                e.printStackTrace()
                state = state.copy(
                    isLoading = false
                    // (Podríamos añadir un error general al 'state' si quisiéramos)
                    // generalError = "Ocurrió un error inesperado"
                )
            }
        }
    }

    /**
     * Función de validación privada.
     * Es la misma lógica que tenías en tu función 'register', pero separada.
     */
    private fun validate(): Boolean {
        // Limpiamos errores previos
        state = state.copy(
            nameError = null,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null
        )

        var isValid = true
        val name = state.name
        val email = state.email
        val password = state.password
        val confirmPassword = state.confirmPassword

        if (name.isBlank()) {
            state = state.copy(nameError = "El nombre no puede estar vacío")
            isValid = false
        }

        if (email.isBlank()) {
            state = state.copy(emailError = "El email no puede estar vacío")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(emailError = "Por favor, ingresa un email válido")
            isValid = false
        }

        if (password.isBlank()) {
            state = state.copy(passwordError = "La contraseña no puede estar vacía")
            isValid = false
        } else if (password.length < 6) {
            state = state.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            isValid = false
        }

        if (confirmPassword.isBlank()) {
            state = state.copy(confirmPasswordError = "Por favor, confirma tu contraseña")
            isValid = false
        } else if (password != confirmPassword) {
            state = state.copy(confirmPasswordError = "Las contraseñas no coinciden")
            isValid = false
        }

        return isValid
    }

    // --- FIN DE CAMBIOS: LÓGICA DE REGISTRO CON ROOM ---
}