package com.example.appagendita_grupo1.ui.screens.home.sections

// --- Importaciones de UI ---
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState // <-- Importante

// --- Importaciones de la App ---
import com.example.appagendita_grupo1.ui.screens.home.components.NoteItemCard // <-- El Composable del Paso 3
import com.example.appagendita_grupo1.viewmodel.NoteListViewModel
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory

// --- Importaciones para la Preview ---
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@Composable
fun MonthlyNotesSection(
    modifier: Modifier = Modifier,
    onAddNote: () -> Unit,
    // --- INICIO DE CAMBIOS: ACEPTAR PARÁMETROS ---
    noteListViewModelFactory: NoteListViewModelFactory,
    onNoteClick: () -> Unit // (Más adelante podemos hacer que pase el ID de la nota)
    // --- FIN DE CAMBIOS: ACEPTAR PARÁMETROS ---
) {
    // --- INICIO DE CAMBIOS: LÓGICA DEL VIEWMODEL ---

    // 1. Instanciamos el ViewModel usando la factory que nos pasaron
    val viewModel: NoteListViewModel = viewModel(factory = noteListViewModelFactory)

    // 2. Recolectamos el estado (que contiene la lista de notas) del StateFlow
    val state by viewModel.state.collectAsState()
    val notes = state.notes

    // 3. Mostramos la lista o un mensaje de placeholder si está vacía
    if (notes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Aún no tienes notas. ¡Crea una!")
            // TODO: Podríamos añadir un botón aquí que llame a onAddNote
        }
    } else {
        // 4. Usamos LazyColumn para mostrar la lista de forma eficiente
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            // Añadimos padding para que el BottomBar no tape el último item
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre tarjetas
        ) {
            // 5. Creamos un item por cada nota en la lista
            items(notes, key = { it.id }) { note ->
                NoteItemCard(
                    note = note,
                    onClick = {
                        // TODO: Eventualmente, navegar al detalle de la nota
                        // onNoteClick(note.id)
                        onNoteClick() // De momento solo navega a la pantalla genérica de Detalle
                    },
                    modifier = Modifier // El NoteItemCard ya tiene su propio padding
                )
            }
        }
    }
    // --- FIN DE CAMBIOS: LÓGICA DEL VIEWMODEL ---
}


@Preview(showBackground = true)
@Composable
fun MonthlyNotesSectionPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // --- INICIO DE CAMBIOS: ARREGLAR LA PREVIEW ---
    // Creamos un DAO falso
    val fakeDao = object : NoteDao {
        override suspend fun insert(note: NoteEntity): Long = 0L
        override suspend fun update(note: NoteEntity) {}
        override suspend fun delete(note: NoteEntity) {}
        override fun getAllNotes(): Flow<List<NoteEntity>> = flowOf(
            listOf( // Simulamos una lista con notas
                NoteEntity(1, "Nota de prueba 1", "Esta es la descripción de la primera nota. Es un poco larga para ver cómo se corta.", null, 0L),
                NoteEntity(2, "Nota con imagen 2", "Descripción corta.", "file://...", 0L)
            )
        )
        override fun getNotesByUserId(userId: Long): Flow<List<NoteEntity>> = flowOf(
            listOf(
                NoteEntity(1, "Nota de prueba 1", "Esta es la descripción de la primera nota. Es un poco larga para ver cómo se corta.", null, userId),
                NoteEntity(2, "Nota con imagen 2", "Descripción corta.", "file://...", userId)
            )
        )
        override suspend fun getNoteById(noteId: Long): NoteEntity? = null
        override suspend fun count(): Int = 2
    }
    // Creamos un Repositorio falso con SessionManager
    val fakeRepository = NoteRepository(
        fakeDao,
        com.example.appagendita_grupo1.utils.SessionManager.getInstance(context)
    )
    // Creamos una Factory falsa
    val fakeFactory = NoteListViewModelFactory(repository = fakeRepository)

    AppAgendita_Grupo1Theme {
        MonthlyNotesSection(
            modifier = Modifier.padding(16.dp),
            onAddNote = {},
            noteListViewModelFactory = fakeFactory,
            onNoteClick = {}
        )
    }
    // --- FIN DE CAMBIOS: ARREGLAR LA PREVIEW ---
}