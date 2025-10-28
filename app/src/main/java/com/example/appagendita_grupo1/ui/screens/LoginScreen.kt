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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
import com.example.appagendita_grupo1.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    onNavigateToSplash: () -> Unit,
    viewModel: LoginViewModel
) {
    val state = viewModel.state
    var passwordVisible by remember { mutableStateOf(false) }

    // Check for existing session on first composition
    LaunchedEffect(Unit) {
        viewModel.checkExistingSession()
    }

    LaunchedEffect(key1 = state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null || state.generalError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        state.emailError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.passwordError != null || state.generalError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    // --- INICIO DE CAMBIOS: ÍCONO DE OJO ---
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility_open // <-- Ojo abierto
                            else R.drawable.ic_visibility_off                      // <-- Ojo cerrado
                        ),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                    // --- FIN DE CAMBIOS: ÍCONO DE OJO ---
                }
            },
            singleLine = true
        )
        state.passwordError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        state.generalError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onLoginClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
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

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "O inicia sesión con", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /* TODO: Sin funcionalidad por ahora */ }) {
                Image(
                    painter = painterResource(id = R.drawable.logo_google),
                    contentDescription = "Google Login",
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /* TODO: Sin funcionalidad por ahora */ }) {
                Image(
                    painter = painterResource(id = R.drawable.logo_apple),
                    contentDescription = "Apple Login",
                    modifier = Modifier.size(48.dp)
                )
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
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val fakeDao = object : UserDao {
        override suspend fun insert(user: UserEntity): Long = 0
        override suspend fun getUserByEmail(email: String): UserEntity? = null
        override suspend fun login(email: String, password: String): UserEntity? = null
    }
    val fakeRepository = UserRepository(fakeDao)
    val fakeSessionManager = com.example.appagendita_grupo1.utils.SessionManager.getInstance(context)
    val fakeViewModel = LoginViewModel(fakeRepository, fakeSessionManager)

    LoginScreen(
        onLoginSuccess = {},
        onNavigateToRegistration = {},
        onNavigateToSplash = {},
        viewModel = fakeViewModel
    )
}