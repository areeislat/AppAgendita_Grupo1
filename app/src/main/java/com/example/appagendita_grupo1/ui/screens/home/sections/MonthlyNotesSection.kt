package com.example.appagendita_grupo1.ui.screens.home.sections

// --- Importaciones de UI ---
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// --- Importaciones de la App ---
import com.example.appagendita_grupo1.ui.screens.home.components.NoteItemCard
import com.example.appagendita_grupo1.viewmodel.NoteListViewModel
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme

// --- Importaciones para la Preview ---
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.request.EventRequest
import com.example.appagendita_grupo1.data.remote.request.NoteRequest
import com.example.appagendita_grupo1.data.remote.request.TaskRequest
import com.example.appagendita_grupo1.data.remote.request.RegisterRequest
import com.example.appagendita_grupo1.data.remote.response.EventResponse
import com.example.appagendita_grupo1.data.remote.response.NoteResponse
import com.example.appagendita_grupo1.data.remote.response.UserResponse
// NUEVOS IMPORTS
import com.example.appagendita_grupo1.data.remote.request.LoginRequest
import com.example.appagendita_grupo1.data.remote.response.LoginResponse
import com.example.appagendita_grupo1.utils.SessionManager
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// ---------------------------


@Composable
fun MonthlyNotesSection(
    modifier: Modifier = Modifier,
    onAddNote: () -> Unit,
    noteListViewModelFactory: NoteListViewModelFactory,
    onNoteClick: () -> Unit
) {
    // 1. Instanciamos el ViewModel usando la factory
    val viewModel: NoteListViewModel = viewModel(factory = noteListViewModelFactory)

    // 2. Recolectamos el estado
    val state by viewModel.state.collectAsState()
    val notes = state.notes

    // 3. UI
    if (notes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Aún no tienes notas. ¡Crea una!")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                NoteItemCard(
                    note = note,
                    onClick = { onNoteClick() },
                    modifier = Modifier
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MonthlyNotesSectionPreview() {
    val context = LocalContext.current

    // --- CORRECCIÓN PREVIEW ---

    // 1. Fake API
    val fakeApiService = object : ApiService {
        override suspend fun createTask(taskRequest: TaskRequest): Response<Unit> = Response.success(Unit)
        override suspend fun registerUser(registerRequest: RegisterRequest): Response<UserResponse> = Response.success(null)

        // --- AÑADIDO: Mock para loginUser ---
        override suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> = Response.success(null)

        override suspend fun createEvent(eventRequest: EventRequest): Response<EventResponse> = Response.success(null)

        override suspend fun getUserEvents(ownerId: String): Response<List<EventResponse>> = Response.success(emptyList())
        // -----------------------------------

        override suspend fun getUserNotes(userId: String): Response<List<NoteResponse>> = Response.success(emptyList())
        override suspend fun createNote(noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
        override suspend fun deleteNote(noteId: String, userId: String): Response<Map<String, String>> = Response.success(emptyMap())
        override suspend fun updateNote(noteId: String, userId: String, noteRequest: NoteRequest): Response<NoteResponse> = Response.success(null)
    }

    // 2. Fake DAO
    val fakeDao = object : NoteDao {
        override suspend fun insert(note: NoteEntity): Long = 0L
        override suspend fun update(note: NoteEntity) {}
        override suspend fun delete(note: NoteEntity) {}

        // Implementación de métodos de consulta
        override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(emptyList())
        override fun getNotesByUserId(userId: String): Flow<List<NoteEntity>> = flowOf(
            listOf(
                NoteEntity(1, "Nota 1", "Desc 1", null, "user-1"),
                NoteEntity(2, "Nota 2", "Desc 2", null, "user-1")
            )
        )

        override suspend fun getNoteById(noteId: Long): NoteEntity? = null
        override suspend fun count(): Int = 2
    }

    // 3. Fake Repository
    val fakeRepository = NoteRepository(
        noteDao = fakeDao,
        apiService = fakeApiService,
        sessionManager = SessionManager.getInstance(context)
    )

    // 4. Fake Factory
    val fakeFactory = NoteListViewModelFactory(repository = fakeRepository)

    AppAgendita_Grupo1Theme {
        MonthlyNotesSection(
            modifier = Modifier.padding(16.dp),
            onAddNote = {},
            noteListViewModelFactory = fakeFactory,
            onNoteClick = {}
        )
    }
    // --- FIN CORRECCIÓN PREVIEW ---
}