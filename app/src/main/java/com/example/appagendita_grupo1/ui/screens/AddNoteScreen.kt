package com.example.appagendita_grupo1.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
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
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// --------------------------------------

/**
 * Función auxiliar para crear una Uri segura usando el FileProvider.
 */
fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.cacheDir
    val imageFile = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    )

    val authority = "${context.packageName}.provider"

    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        authority,
        imageFile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onBack: () -> Unit,
    onNoteSaved: () -> Unit,
    viewModel: AddNoteViewModel = viewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    // 1. Variable temporal para guardar la Uri antes de que la cámara la use
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // 2. Crear el lanzador para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                viewModel.onPhotoTaken(tempUri)
            }
        }
    )

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 3. Crear el lanzador para los permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Título") },
                isError = state.titleError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.titleError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                isError = state.descriptionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            state.descriptionError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Mostrar la imagen si existe
            if (state.imageUri != null) {
                AsyncImage(
                    model = state.imageUri,
                    contentDescription = "Imagen de la nota",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Botón para tomar la foto
            Button(
                onClick = {
                    if (hasCameraPermission) {
                        val newUri = createImageUri(context)
                        tempUri = newUri
                        cameraLauncher.launch(newUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.imageUri == null) "Añadir Foto" else "Tomar Foto Nueva")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (viewModel.validate()) {
                        viewModel.onSaveNote()
                        onNoteSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // --- CORRECCIÓN PREVIEW ---

    // 1. Creamos un ApiService falso (vacío)
    val fakeApiService = object : ApiService {
        override suspend fun createTask(taskRequest: TaskRequest): Response<Unit> = Response.success(Unit)
        override suspend fun registerUser(registerRequest: RegisterRequest): Response<UserResponse> = Response.success(null)

        // --- AÑADIDO: Mock para loginUser ---
        override suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> = Response.success(null)

        override suspend fun createEvent(eventRequest: EventRequest): Response<EventResponse> = Response.success(null)

        override suspend fun getUserEvents(ownerId: String): Response<List<EventResponse>> = Response.success(emptyList())
        // -----------------------------------

        // Métodos de Nota
        override suspend fun getUserNotes(userId: String): Response<List<NoteResponse>> = Response.success(emptyList())
        override suspend fun createNote(noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
        override suspend fun deleteNote(noteId: String, userId: String): Response<Map<String, String>> = Response.success(emptyMap())
        override suspend fun updateNote(noteId: String, userId: String, noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
    }

    // 2. Creamos el Repositorio falso con el DAO actualizado
    val fakeRepository = NoteRepository(
        noteDao = object : NoteDao {
            override suspend fun insert(note: NoteEntity): Long = 0L
            override suspend fun update(note: NoteEntity) {}
            override suspend fun delete(note: NoteEntity) {}

            // Método legacy (si existe)
            override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(emptyList())

            // CORRECCIÓN: userId ahora es String
            override fun getNotesByUserId(userId: String): Flow<List<NoteEntity>> = flowOf(emptyList())

            override suspend fun getNoteById(noteId: Long): NoteEntity? = null

            // --- AÑADIDO: Implementación de count() ---
            override suspend fun count(): Int = 0
        },
        // Pasamos el ApiService falso
        apiService = fakeApiService,
        sessionManager = com.example.appagendita_grupo1.utils.SessionManager.getInstance(context)
    )

    // Creamos el ViewModel pasándole el repositorio falso
    val previewViewModel = AddNoteViewModel(fakeRepository)

    AddNoteScreen(
        onBack = {},
        onNoteSaved = {},
        viewModel = previewViewModel
    )
}
