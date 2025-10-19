package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.PurplePrimary
import com.example.appagendita_grupo1.ui.theme.Bg


@Composable
fun BottomActionsSheet(actions: List<BottomAction>, onClose: () -> Unit) {
  Column(Modifier.navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    actions.forEach { action ->
      Surface(
        onClick = { action.onClick(); onClose() },
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, CardStroke),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = Bg,
            border = BorderStroke(1.dp, CardStroke),
          ) {
            Icon(action.icon, null, modifier = Modifier.size(28.dp).padding(4.dp), tint = NavyText)
          }
          Spacer(Modifier.width(12.dp))
          Text(action.label, style = MaterialTheme.typography.bodyLarge, color = NavyText)
        }
      }
    }
    Spacer(Modifier.height(12.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
      FloatingActionButton(onClick = onClose, containerColor = PurplePrimary) {
        Icon(Icons.Outlined.Close, null, tint = Color.White)
      }
    }
    Spacer(Modifier.height(16.dp))
  }
}

