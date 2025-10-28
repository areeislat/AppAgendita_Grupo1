package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// --- INICIO DE CAMBIOS: IMPORTACIONES ---
import androidx.compose.runtime.LaunchedEffect // <-- 1. Importar LaunchedEffect
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
// Importaciones para la Preview (puedes minimizarlas)
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
import com.example.appagendita_grupo1.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// --- FIN DE CAMBIOS: IMPORTACIONES ---

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    onNavigateToSplash: () -> Unit,
    // --- CAMBIO 1: Aceptar el ViewModel como parámetro ---
    viewModel: LoginViewModel
) {
    // --- CAMBIO 2: Obtener el estado del ViewModel ---
    val state = viewModel.state
    var passwordVisible by remember { mutableStateOf(false) }

    // --- CAMBIO 3: Efecto para navegar cuando el login sea exitoso ---
    LaunchedEffect(key1 = state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess() // Llama a la navegación
        }
    }
    // --- FIN CAMBIO 3 ---

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido de vuelta",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Inicia sesión en tu cuenta",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Campo Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) }, // <- Conectado al VM
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null || state.generalError != null, // <- Error si hay error de campo O general
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        state.emailError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) }, // <- Conectado al VM
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.passwordError != null || state.generalError != null, // <- Error si hay error de campo O general
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(R.drawable.logo_apple) else painterResource(R.drawable.logo_google), // (Deberías cambiar estos íconos)
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            singleLine = true
        )
        state.passwordError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // --- CAMBIO 4: Mostrar error general ---
        state.generalError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        // --- FIN CAMBIO 4 ---

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Login
        Button(
            // --- CAMBIO 5: Llamar a onLoginClick ---
            onClick = { viewModel.onLoginClick() },
            // --- FIN CAMBIO 5 ---
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading // Deshabilitar si está cargando
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Iniciar Sesión")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "¿No tienes una cuenta? ")
            Text(
                text = "Regístrate",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToRegistration() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Volver al Inicio",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onNavigateToSplash() }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // --- CAMBIO 6: Arreglar la Preview ---
    // Creamos un DAO falso
    val fakeDao = object : UserDao {
        override suspend fun insert(user: UserEntity): Long = 0
        override suspend fun getUserByEmail(email: String): UserEntity? = null
        override suspend fun login(email: String, password: String): UserEntity? = null
    }
    // Creamos un Repositorio falso
    val fakeRepository = UserRepository(fakeDao)
    // Creamos un ViewModel falso
    val fakeViewModel = LoginViewModel(fakeRepository)

    LoginScreen(
        onLoginSuccess = {},
        onNavigateToRegistration = {},
        onNavigateToSplash = {},
        viewModel = fakeViewModel // Le pasamos el VM falso
    )
    // --- FIN CAMBIO 6 ---
}