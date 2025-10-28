package com.example.appagendita_grupo1.model

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,

    // --- INICIO DE CAMBIOS ---
    /**
     * Se pondrá en 'true' cuando el VM
     * haya verificado al usuario en la BD exitosamente.
     * La Vista (Screen) observará este campo para navegar.
     */
    val loginSuccess: Boolean = false,

    /**
     * Para errores que no son de un campo específico
     * (Ej. "Email o contraseña incorrectos")
     */
    val generalError: String? = null
    // --- FIN DE CAMBIOS ---
)