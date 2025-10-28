package com.example.appagendita_grupo1.ui.screens

import android.Manifest // 1. Importar Manifest
import android.content.Context // 2. Importar Context
import android.net.Uri // 3. Importar Uri
import androidx.activity.compose.rememberLauncherForActivityResult // 4. Importar Launcher
import androidx.activity.result.contract.ActivityResultContracts // 5. Importar Contrato
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // 6. Importar ScrollState
import androidx.compose.foundation.verticalScroll // 7. Importar verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // 8. Importar getValue
import androidx.compose.runtime.mutableStateOf // 9. Importar mutableStateOf
import androidx.compose.runtime.remember // 10. Importar remember
import androidx.compose.runtime.setValue // 11. Importar setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale // 12. Importar ContentScale
import androidx.compose.ui.platform.LocalContext // 13. Importar LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider // 14. Importar FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage // 15. Importar Coil
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi // 16. Importar Accompanist
import com.google.accompanist.permissions.rememberPermissionState // 17. Importar Accompanist
import com.google.accompanist.permissions.PermissionStatus // <--- ¡¡CAMBIO 1: AÑADIR ESTA IMPORTACIÓN!!
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlinx.coroutines.flow.Flow // Necesaria para el DAO falso
import kotlinx.coroutines.flow.flowOf // Necesaria para el DAO falso
import com.example.appagendita_grupo1.data.local.note.NoteDao // Necesaria para el DAO falso
import com.example.appagendita_grupo1.data.local.note.NoteEntity // Necesaria para el DAO falso
import com.example.appagendita_grupo1.data.repository.NoteRepository // Necesaria para el repo falso

// --- INICIO CÓDIGO AÑADIDO ---

/**
 * Función auxiliar para crear una Uri segura usando el FileProvider.
 * La Vista es responsable de crear esto, ya que necesita el Context.
 */
fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    // Usamos el directorio de caché interno de la app
    val storageDir = context.cacheDir
    val imageFile = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    )

    // Obtén la autoridad del provider (definida en AndroidManifest.xml)
    val authority = "${context.packageName}.provider"

    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        authority,
        imageFile
    )
}

// --- FIN CÓDIGO AÑADIDO ---


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class) // 18. Añadir OptIn de Accompanist
@Composable
fun AddNoteScreen(
    onBack: () -> Unit,
    onNoteSaved: () -> Unit,
    viewModel: AddNoteViewModel = viewModel()
) {
    val state = viewModel.state

    // --- INICIO CÓDIGO AÑADIDO ---

    val context = LocalContext.current

    // 1. Variable temporal para guardar la Uri antes de que la cámara la use
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // 2. Crear el lanzador para la cámara (ActivityResultLauncher)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // Si la foto se tomó con éxito, actualiza el ViewModel
                viewModel.onPhotoTaken(tempUri)
            }
        }
    )

    // 3. Crear el manejador de permisos
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    // --- FIN CÓDIGO AÑADIDO ---

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
                // 19. Hacemos la columna "scrolleable" por si la imagen es grande
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
                // 20. Ajustamos la altura del campo de descripción
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            state.descriptionError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp)) // 21. Espacio añadido

            // --- INICIO CÓDIGO AÑADIDO ---

            // 4. Mostrar la imagen si existe en el estado
            if (state.imageUri != null) {
                AsyncImage(
                    model = state.imageUri,
                    contentDescription = "Imagen de la nota",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Botón para tomar la foto
            Button(
                onClick = {
                    // Lógica para tomar la foto

                    // --- ¡¡CAMBIO 2: ASÍ ES LA NUEVA API!! ---
                    if (cameraPermissionState.status == PermissionStatus.Granted) {
                        // Si ya tenemos permiso, crea Uri y lanza la cámara
                        val newUri = createImageUri(context) // Creamos la Uri no nula
                        tempUri = newUri // La guardamos en el estado temporal
                        cameraLauncher.launch(newUri) // Lanzamos la cámara con la Uri no nula
                    } else {
                        // Si no tenemos permiso, solicítalo
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.imageUri == null) "Añadir Foto" else "Tomar Foto Nueva")
            }

            // --- FIN CÓDIGO AÑADIDO ---

            Spacer(modifier = Modifier.weight(1f)) // Esto empuja el botón de guardar al fondo

            Button(
                onClick = {
                    if (viewModel.validate()) {
                        // 22. Llamamos al ViewModel para que guarde el estado
                        viewModel.onSaveNote()
                        // Navegamos hacia atrás
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
    // --- INICIO CORRECCIÓN PREVIEW ---
    // Simulación MUY BÁSICA de un repositorio para la preview
    // No hará nada, pero satisface el constructor del ViewModel
    val fakeRepository = NoteRepository(noteDao = object : NoteDao {
        override suspend fun insert(note: NoteEntity): Long = 0L
        override suspend fun update(note: NoteEntity) {}
        override suspend fun delete(note: NoteEntity) {}
        override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(emptyList()) // Devuelve flujo vacío
        override suspend fun getNoteById(noteId: Long): NoteEntity? = null
        override suspend fun count(): Int = 0
    })

    // Creamos el ViewModel pasándole el repositorio falso
    val previewViewModel = AddNoteViewModel(fakeRepository)

    AddNoteScreen(
        onBack = {},
        onNoteSaved = {},
        viewModel = previewViewModel // Pasamos el ViewModel simulado
    )
    // --- FIN CORRECCIÓN PREVIEW ---
}