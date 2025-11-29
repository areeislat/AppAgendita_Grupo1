package com.example.appagendita_grupo1.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.viewmodel.RegistrationViewModel
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.utils.SessionManager

// --- IMPORTS PARA PREVIEW ---
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.EventRequest
import com.example.appagendita_grupo1.data.remote.request.LoginRequest
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.data.remote.response.EventResponse
import com.example.appagendita_grupo1.data.remote.response.LoginResponse
import com.example.appagendita_grupo1.data.remote.response.NoteResponse
import com.example.appagendita_grupo1.data.remote.response.UserResponse
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// ----------------------------


@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegistrationViewModel
) {
    val state = viewModel.state
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.registrationSuccess) {
        if (state.registrationSuccess) {
            onRegistrationSuccess()
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
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Campo Nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nameError != null,
            singleLine = true
        )
        state.nameError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null,
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
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.passwordError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility_open // Ojo abierto
                            else R.drawable.ic_visibility_off // Ojo cerrado
                        ),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true
        )
        state.passwordError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.confirmPasswordError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (confirmPasswordVisible) R.drawable.ic_visibility_open // Ojo abierto
                            else R.drawable.ic_visibility_off // Ojo cerrado
                        ),
                        contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true
        )
        state.confirmPasswordError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Registro
        Button(
            onClick = { viewModel.onRegisterClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Registrarse")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "¿Ya tienes una cuenta? ")
            Text(
                text = "Inicia sesión",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    val context = LocalContext.current

    // 1. Fake API
    val fakeApiService = object : ApiService {
        override suspend fun createTask(taskRequest: TaskRequest): Response<Unit> = Response.success(Unit)
        override suspend fun registerUser(registerRequest: RegisterRequest): Response<UserResponse> = Response.success(null)
        override suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> = Response.success(null)
        override suspend fun getUserNotes(userId: String): Response<List<NoteResponse>> = Response.success(emptyList())
        override suspend fun createNote(noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
        override suspend fun deleteNote(noteId: String, userId: String): Response<Map<String, String>> = Response.success(emptyMap())
        override suspend fun updateNote(noteId: String, userId: String, noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
        override suspend fun createEvent(eventRequest: EventRequest): Response<EventResponse> = Response.success(null)
        override suspend fun getUserEvents(ownerId: String): Response<List<EventResponse>> = Response.success(emptyList())
    }

    // 2. Fake DAO
    val fakeDao = object : UserDao {
        override suspend fun insert(user: UserEntity) {}
        override suspend fun getUserByEmail(email: String): UserEntity? = null
        override suspend fun getUserById(id: String): UserEntity? = null
        override suspend fun clearUsers() {}
        override suspend fun updateUserName(userId: String, name: String) {}
    }

    // 3. Fake Repository
    val fakeRepository = UserRepository(fakeDao, fakeApiService)

    // 4. Fake ViewModel
    val fakeViewModel = RegistrationViewModel(fakeRepository)

    RegistrationScreen(
        onRegistrationSuccess = {},
        onNavigateToLogin = {},
        viewModel = fakeViewModel
    )
}