package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.PurplePrimary

@Composable
fun SectionHeader(title: String) {
  Row(
    Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(title, style = MaterialTheme.typography.titleMedium, color = NavyText)
    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = NavyText)
  }
}

enum class TaskStatus(val label: String) {
  Todo("Por hacer"),
  InProgress("En progreso"),
  Done("Completadas")
}

data class TaskUi(
  val title: String,
  val subtitle: String,
  val progress: Int,
  val status: TaskStatus
)

val sampleTasks = listOf(
  TaskUi(
    title = "Crear prototipo de reservas",
    subtitle = "App de productividad · Hace 2 min",
    progress = 60,
    status = TaskStatus.InProgress
  ),
  TaskUi(
    title = "Diseñar pantalla de inicio",
    subtitle = "Banca móvil · Hace 5 min",
    progress = 70,
    status = TaskStatus.InProgress
  ),
  TaskUi(
    title = "Preparar briefing con el cliente",
    subtitle = "Marketing digital · Hace 10 min",
    progress = 0,
    status = TaskStatus.Todo
  ),
  TaskUi(
    title = "Redactar documentación técnica",
    subtitle = "Sistema interno · Hace 15 min",
    progress = 10,
    status = TaskStatus.Todo
  ),
  TaskUi(
    title = "Publicar landing page",
    subtitle = "Curso online · Hace 20 min",
    progress = 100,
    status = TaskStatus.Done
  ),
  TaskUi(
    title = "Enviar informe semanal",
    subtitle = "Equipo de ventas · Hace 25 min",
    progress = 100,
    status = TaskStatus.Done
  )
)

@Composable
fun ProgressTaskCard(task: TaskUi, onClick: () -> Unit) {
  Surface(
    onClick = onClick,
    color = Color.White,
    shape = RoundedCornerShape(20.dp),
    border = BorderStroke(1.dp, CardStroke),
    shadowElevation = 0.dp,
    tonalElevation = 0.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(Modifier.weight(1f)) {
        Text(task.title, style = MaterialTheme.typography.bodyLarge, color = NavyText)
        Spacer(Modifier.height(4.dp))
        Text(task.subtitle, style = MaterialTheme.typography.bodyMedium, color = NavyText.copy(alpha = .6f))
      }
      ProgressRing(percentage = task.progress)
    }
  }
}

@Composable
fun TaskStatusTabs(
  selectedStatus: TaskStatus,
  onStatusSelected: (TaskStatus) -> Unit,
  modifier: Modifier = Modifier
) {
  val statuses = remember { TaskStatus.values().toList() }
  val selectedIndex = statuses.indexOf(selectedStatus).coerceAtLeast(0)

  TabRow(
    selectedTabIndex = selectedIndex,
    modifier = modifier.fillMaxWidth(),
    containerColor = Color.Transparent,
    contentColor = NavyText,
    indicator = { tabPositions ->
      if (tabPositions.isNotEmpty()) {
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier
            .tabIndicatorOffset(tabPositions[selectedIndex])
            .height(3.dp),
          color = PurplePrimary
        )
      }
    },
    divider = {}
  ) {
    statuses.forEach { status ->
      val selected = status == selectedStatus
      Tab(
        selected = selected,
        onClick = { onStatusSelected(status) }
      ) {
        Text(
          text = status.label,
          style = MaterialTheme.typography.bodyMedium,
          color = if (selected) NavyText else NavyText.copy(alpha = .6f),
          modifier = Modifier.padding(vertical = 12.dp)
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader(title = "En progreso")
}

@Preview(showBackground = true)
@Composable
fun ProgressTaskCardPreview() {
    ProgressTaskCard(task = sampleTasks.first { it.status == TaskStatus.InProgress }, onClick = {})
}

@Preview(showBackground = true)
@Composable
fun TaskStatusTabsPreview() {
    TaskStatusTabs(selectedStatus = TaskStatus.InProgress, onStatusSelected = {})
}
