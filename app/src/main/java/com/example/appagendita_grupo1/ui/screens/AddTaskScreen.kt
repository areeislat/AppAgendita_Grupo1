package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.viewmodel.AddTaskViewModel
import com.example.appagendita_grupo1.viewmodel.TaskPriority
import com.example.appagendita_grupo1.viewmodel.TaskCategory
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme

// --- IMPORTS PARA LA PREVIEW ---
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.EventRequest
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.response.EventResponse
import com.example.appagendita_grupo1.data.remote.response.UserResponse
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.response.NoteResponse
import com.example.appagendita_grupo1.data.remote.request.LoginRequest
import com.example.appagendita_grupo1.data.remote.response.LoginResponse
import com.example.appagendita_grupo1.utils.SessionManager
import retrofit2.Response
// ------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    // Navegar de vuelta cuando la tarea se guarda exitosamente
    LaunchedEffect(uiState.isTaskSaved) {
        if (uiState.isTaskSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Tarea") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Sección 1: Información Básica
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Información Básica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Campo de Título (OBLIGATORIO)
                    OutlinedTextField(
                        value = uiState.taskName,
                        onValueChange = viewModel::onTaskNameChange,
                        label = { Text("Título *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.taskNameError != null,
                        supportingText = {
                            uiState.taskNameError?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true
                    )

                    // Campo de Descripción (OPCIONAL)
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = viewModel::onDescriptionChange,
                        label = { Text("Descripción (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            // Sección 2: Prioridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Prioridad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Chips de Prioridad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskPriority.entries.forEach { priority ->
                            FilterChip(
                                selected = uiState.priority == priority,
                                onClick = { viewModel.onPrioritySelected(priority) },
                                label = { Text(priority.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    // --- CORRECCIÓN: Añadida la rama URGENT ---
                                    selectedContainerColor = when (priority) {
                                        TaskPriority.LOW -> MaterialTheme.colorScheme.tertiary
                                        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.secondary
                                        TaskPriority.HIGH -> MaterialTheme.colorScheme.error
                                        TaskPriority.URGENT -> MaterialTheme.colorScheme.errorContainer // Color distinto para urgente
                                    },
                                    selectedLabelColor = when (priority) {
                                        TaskPriority.LOW -> MaterialTheme.colorScheme.onTertiary
                                        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.onSecondary
                                        TaskPriority.HIGH -> MaterialTheme.colorScheme.onError
                                        TaskPriority.URGENT -> MaterialTheme.colorScheme.onErrorContainer
                                    }
                                    // ------------------------------------------
                                )
                            )
                        }
                    }
                }
            }

            // Sección 3: Categoría
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Categoría",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Grid de categorías (2 columnas)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskCategory.entries.chunked(2).forEach { rowCategories ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowCategories.forEach { category ->
                                    FilterChip(
                                        selected = uiState.category == category,
                                        onClick = { viewModel.onCategorySelected(category) },
                                        label = { Text(category.displayName) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                // Rellenar espacio si es impar
                                if (rowCategories.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Mostrar error si existe
            uiState.saveError?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Botón Guardar
            Button(
                onClick = { viewModel.saveTask() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState.canSave
            ) {
                Text(
                    text = "Guardar Tarea",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    val context = LocalContext.current

    // --- CORRECCIÓN DE PREVIEW ---
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

    val fakeSessionManager = SessionManager.getInstance(context)
    val previewViewModel = AddTaskViewModel(fakeApiService, fakeSessionManager)

    AppAgendita_Grupo1Theme {
        AddTaskScreen(
            viewModel = previewViewModel,
            onNavigateBack = {}
        )
    }
}