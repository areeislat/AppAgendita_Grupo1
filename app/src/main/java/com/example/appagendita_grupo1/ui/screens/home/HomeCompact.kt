package com.example.appagendita_grupo1.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.appagendita_grupo1.ui.screens.home.components.BottomAction
import com.example.appagendita_grupo1.ui.screens.home.components.BottomActionsSheet
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.screens.home.components.HomeTopHeader
import com.example.appagendita_grupo1.ui.screens.home.sections.EventsSection
import com.example.appagendita_grupo1.ui.screens.home.sections.MonthlyNotesSection
import com.example.appagendita_grupo1.ui.screens.home.sections.OverviewSection
import com.example.appagendita_grupo1.ui.screens.home.sections.TodayTasksSection
import com.example.appagendita_grupo1.ui.theme.Bg
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory

// --- IMPORTS PARA PREVIEW ---
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
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// ---------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompact(
    onOpenSettings: () -> Unit = {},
    onOpenDetail: () -> Unit = {},
    onOpenAccount: () -> Unit = {},
    onAddTask: () -> Unit = {},
    onAddNote: () -> Unit = {},
    onAddTeam: () -> Unit = {},
    onAddEvent: () -> Unit = {},
    section: HomeSection? = null,
    noteListViewModelFactory: NoteListViewModelFactory
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf(section ?: HomeSection.Overview) }

    LaunchedEffect(section) {
        section?.let {
            selectedSection = it
        }
    }

    Scaffold(
        modifier = Modifier.padding(top = 18.dp),
        containerColor = Bg,
        topBar = {
            HomeTopHeader(
                selectedSection = selectedSection,
                onSectionSelected = { selectedSection = it },
                onRightClick = onOpenSettings
            )
        },
        bottomBar = {
            HomeBottomBar(
                isHomeSelected = selectedSection == HomeSection.Overview,
                isEventsSelected = selectedSection == HomeSection.MonthlyNotes,
                isTeamsSelected = selectedSection == HomeSection.Events,
                onHomeClick = { selectedSection = HomeSection.Overview },
                onEventsClick = { selectedSection = HomeSection.MonthlyNotes },
                onTeamsClick = { selectedSection = HomeSection.Events },
                onAccountClick = onOpenAccount,
                onCreateClick = { showSheet = true }
            )
        }
    ) { padding ->
        val contentModifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)

        when (selectedSection) {
            HomeSection.Overview -> OverviewSection(
                modifier = contentModifier,
                onAddTask = onAddTask,
                onAddNote = onAddNote,
                onAddEvent = onAddEvent,
                onOpenDetail = onOpenDetail
            )

            HomeSection.TodayTasks -> TodayTasksSection(
                modifier = contentModifier,
                onAddTask = onAddTask
            )

            HomeSection.MonthlyNotes -> MonthlyNotesSection(
                modifier = contentModifier,
                onAddNote = onAddNote,
                noteListViewModelFactory = noteListViewModelFactory,
                onNoteClick = { onOpenDetail() }
            )

            HomeSection.Events -> EventsSection(
                modifier = contentModifier,
                onAddEvent = onAddEvent,
                onOpenDetail = onOpenDetail
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            BottomActionsSheet(
                actions = listOf(
                    BottomAction(Icons.Outlined.Edit, "Crear Nota", onAddNote),
                    BottomAction(Icons.Outlined.AddCircle, "Crear Tarea", onAddTask),
                    BottomAction(Icons.Outlined.Group, "Crear Equipo", onAddTeam),
                    BottomAction(Icons.Outlined.Schedule, "Crear Evento", onAddEvent),
                ),
                onClose = { showSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeCompactPreview() {
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
        override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(emptyList())
        override fun getNotesByUserId(userId: String): Flow<List<NoteEntity>> = flowOf(emptyList())
        override suspend fun getNoteById(noteId: Long): NoteEntity? = null
        override suspend fun count(): Int = 0
    }

    // 3. Fake Repository
    val fakeRepository = NoteRepository(
        noteDao = fakeDao,
        apiService = fakeApiService,
        sessionManager = com.example.appagendita_grupo1.utils.SessionManager.getInstance(context)
    )

    // 4. Fake Factory
    val fakeFactory = NoteListViewModelFactory(repository = fakeRepository)

    HomeCompact(
        noteListViewModelFactory = fakeFactory
    )
    // --- FIN CORRECCIÓN PREVIEW ---
}