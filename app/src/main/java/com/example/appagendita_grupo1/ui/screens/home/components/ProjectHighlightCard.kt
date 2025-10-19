package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// P
@Composable
fun ProjectHighlightCard(onClick: () -> Unit) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.primary,
    shadowElevation = 0.dp,
    tonalElevation = 0.dp,
    modifier = Modifier
      .fillMaxWidth()
      .height(170.dp)
  ) {
    Column(Modifier.padding(20.dp)) {
      Text("Diseño de APP", color = Color.White, style = MaterialTheme.typography.titleLarge)
      Text("UI Design Kit", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
      Spacer(Modifier.height(16.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        // AvatarsStack() // 3 caritas superpuestas. Not implemented.
        Column(horizontalAlignment = Alignment.End) {
          Text("Progreso", color = Color.White.copy(alpha = .9f), style = MaterialTheme.typography.labelLarge)
          LinearProgressIndicator(
            progress = { 0.62f },
            trackColor = Color.White.copy(alpha = 0.25f),
            color = Color.White,
            modifier = Modifier
              .width(160.dp)
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp))
          )
          Text("50/80", color = Color.White, style = MaterialTheme.typography.bodyMedium)
        }
      }
    }
  }
}

@Preview
@Composable
fun ProjectHighlightCardPreview() {
    ProjectHighlightCard(onClick = {})
}
