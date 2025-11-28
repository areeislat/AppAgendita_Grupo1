package com.example.appagendita_grupo1.ui.screens.home.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme

/**
 * Un Composable que muestra una tarjeta individual para una Nota.
 * Incluye la imagen (si existe), el título y una breve descripción.
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario para que la Card tenga onClick
@Composable
fun NoteItemCard(
    note: NoteEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Espacio entre las tarjetas
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // 1. Mostrar la imagen SÓLO si la nota tiene una URI
            if (note.imageUri != null) {
                AsyncImage(
                    model = Uri.parse(note.imageUri), // Convertimos el String de la BD a Uri
                    contentDescription = note.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Altura fija para la imagen
                    contentScale = ContentScale.Crop // Recorta la imagen para que llene el espacio
                )
            }

            // 2. Columna para el texto (título y descripción)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Padding interno para el texto
            ) {
                // Título (siempre se muestra)
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1, // Solo una línea para el título
                    overflow = TextOverflow.Ellipsis // Pone "..." si es muy largo
                )

                // Descripción (SÓLO si no está en blanco)
                if (note.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3, // Máximo 3 líneas para la descripción
                        overflow = TextOverflow.Ellipsis // Pone "..." si es muy larga
                    )
                }
            }
        }
    }
}


// --- PREVIEWS PARA VERIFICAR EL DISEÑO ---

@Preview(showBackground = true)
@Composable
fun NoteItemCardPreview_NoImage() {
    val testNote = NoteEntity(
        id = 1,
        title = "Reunión de equipo mañana",
        description = "Revisar los hitos del sprint 3. Traer ideas para el próximo feature. La reunión será en la sala principal a las 10:00 AM.",
        imageUri = null, // Prueba sin imagen
        userId = "user-123" // <-- CORRECCIÓN: Añadido userId de prueba
    )
    AppAgendita_Grupo1Theme {
        Box(modifier = Modifier.padding(16.dp)) {
            NoteItemCard(note = testNote, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemCardPreview_WithImage() {
    val testNote = NoteEntity(
        id = 2,
        title = "Idea para la foto",
        description = "Tomé esta foto para el proyecto.",
        // (La imagen de preview no cargará, pero el espacio se reservará)
        imageUri = "file:///android_asset/logo.png",
        userId = "user-123" // <-- CORRECCIÓN: Añadido userId de prueba
    )
    AppAgendita_Grupo1Theme {
        Box(modifier = Modifier.padding(16.dp)) {
            NoteItemCard(note = testNote, onClick = {})
        }
    }
}