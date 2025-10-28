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
import com.example.appagendita_grupo1.ui.screens.home.components.BottomAction
import com.example.appagendita_grupo1.ui.screens.home.components.BottomActionsSheet
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.screens.home.components.HomeTopHeader
import com.example.appagendita_grupo1.ui.screens.home.sections.EventsSection
import com.example.appagendita_grupo1.ui.screens.home.sections.MonthlyNotesSection
import com.example.appagendita_grupo1.ui.screens.home.sections.OverviewSection
import com.example.appagendita_grupo1.ui.screens.home.sections.TodayTasksSection
import com.example.appagendita_grupo1.ui.theme.Bg

// --- INICIO DE CAMBIOS: IMPORTACIONES NECESARIAS ---
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory
// Importaciones para la Preview (puedes minimizar esta sección en el IDE)
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// --- FIN DE CAMBIOS: IMPORTACIONES NECESARIAS ---


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
  // --- ACEPTAR PARÁMETRO DE LA FACTORY ---
  noteListViewModelFactory: NoteListViewModelFactory
  // --- FIN ACEPTAR PARÁMETRO ---
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

      // --- INICIO DE CAMBIOS: SECCIÓN DE NOTAS ---
      HomeSection.MonthlyNotes -> MonthlyNotesSection(
        modifier = contentModifier,
        onAddNote = onAddNote,
        noteListViewModelFactory = noteListViewModelFactory, // <- 1. Pasar la factory
        onNoteClick = { /* TODO: Pasar ID de la nota */ onOpenDetail() } // <- 2. Pasar el callback
      )
      // --- FIN DE CAMBIOS: SECCIÓN DE NOTAS ---

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
  val context = androidx.compose.ui.platform.LocalContext.current
  
  // --- INICIO DE CAMBIOS: ARREGLAR LA PREVIEW ---
  // Creamos una implementación anónima CORRECTA del NoteDao para el fakeRepository
  val fakeDao = object : NoteDao {
    override suspend fun insert(note: NoteEntity): Long = 0L
    override suspend fun update(note: NoteEntity) {}
    override suspend fun delete(note: NoteEntity) {}
    override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(emptyList())
    override fun getNotesByUserId(userId: Long): Flow<List<NoteEntity>> = flowOf(emptyList())
    override suspend fun getNoteById(noteId: Long): NoteEntity? = null
    override suspend fun count(): Int = 0
  }
  // Creamos el repositorio falso con el DAO falso y SessionManager
  val fakeRepository = NoteRepository(
    fakeDao,
    com.example.appagendita_grupo1.utils.SessionManager.getInstance(context)
  )
  // Creamos la factory falsa con el repositorio falso
  val fakeFactory = NoteListViewModelFactory(repository = fakeRepository)

  HomeCompact(
    noteListViewModelFactory = fakeFactory // <- Pasar la factory falsa
  )
  // --- FIN DE CAMBIOS: ARREGLAR LA PREVIEW ---
}