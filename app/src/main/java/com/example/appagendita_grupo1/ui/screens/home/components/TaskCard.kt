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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText

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

data class TaskUi(val title: String, val subtitle: String, val progress: Int)
val sampleTasks = listOf(
  TaskUi("Create Detail Booking", "Productivity Mobile App · Hace 2 min", 60),
  TaskUi("Revision Home Page", "Banking Mobile App · Hace 5 min", 70),
  TaskUi("Working On Landing Page", "Online Course · Hace 7 min", 80)
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

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader(title = "En progreso")
}

@Preview(showBackground = true)
@Composable
fun ProgressTaskCardPreview() {
    ProgressTaskCard(task = sampleTasks.first(), onClick = {})
}
