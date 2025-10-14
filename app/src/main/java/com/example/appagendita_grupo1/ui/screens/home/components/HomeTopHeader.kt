package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText

@Composable
fun HomeTopHeader(onLeftClick: () -> Unit, onRightClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    RoundIconButton(icon = Icons.Outlined.GridView, onClick = onLeftClick)
    Text("Viernes, 26", style = MaterialTheme.typography.titleMedium)
    RoundIconButton(icon = Icons.Outlined.Notifications, onClick = onRightClick)
  }
}

@Composable
fun TitleBlock() {
  Text(
    text = "Hagamos\nhábitos juntos",
    style = MaterialTheme.typography.displaySmall,
    color = NavyText,
    modifier = Modifier.padding(top = 4.dp)
  )
  Spacer(Modifier.height(4.dp))
  Text("👏🏻", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun RoundIconButton(icon: ImageVector, onClick: () -> Unit) {
  Surface(
    shape = CircleShape,
    color = Color.White,
    tonalElevation = 1.dp,
    shadowElevation = 0.dp,
    border = BorderStroke(1.dp, CardStroke)
  ) {
    IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
      Icon(icon, contentDescription = null, tint = NavyText)
    }
  }
}
