package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.PurplePrimary

@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    isHomeSelected: Boolean = false,
    isCalendarSelected: Boolean = false,
    isEventsSelected: Boolean = false,
    isAccountSelected: Boolean = false,
    onHomeClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    onEventsClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
  Box(
    modifier = modifier
      .fillMaxWidth(),
    contentAlignment = Alignment.TopCenter
  ) {
    Surface(
      color = Color.White,
      shadowElevation = 12.dp,
      shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 28.dp, vertical = 18.dp)
      ) {
        Column(Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            BottomBarIcon(
              imageVector = Icons.Outlined.Home,
              contentDescription = "Inicio",
              selected = isHomeSelected,
              onClick = onHomeClick
            )
            BottomBarIcon(
              imageVector = Icons.Outlined.DateRange,
              contentDescription = "Notas del mes",
              selected = isCalendarSelected,
              onClick = onCalendarClick
            )
            Spacer(modifier = Modifier.width(64.dp))
            BottomBarIcon(
              imageVector = Icons.Outlined.Event,
              contentDescription = "Eventos",
              selected = isEventsSelected,
              onClick = onEventsClick
            )
            BottomBarIcon(
              imageVector = Icons.Outlined.PersonOutline,
              contentDescription = "Cuenta",
              selected = isAccountSelected,
              onClick = onAccountClick
            )
          }
        }
      }
    }

    FloatingActionButton(
      onClick = onCreateClick,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .offset(y = (18).dp),
      shape = CircleShape,
      containerColor = PurplePrimary,
      elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 0.dp)
    ) {
      Icon(Icons.Filled.Add, contentDescription = "Crear", tint = Color.White)
    }
  }
}

@Composable
private fun BottomBarIcon(
  imageVector: androidx.compose.ui.graphics.vector.ImageVector,
  contentDescription: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  val tint = if (selected) PurplePrimary else Color(0xFF9EA2B1)

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    IconButton(onClick = onClick) {
      Icon(imageVector = imageVector, contentDescription = contentDescription, tint = tint)
    }
    Spacer(modifier = Modifier.height(4.dp))
    Box(
      modifier = Modifier
        .size(6.dp)
        .background(
          color = if (selected) PurplePrimary else Color.Transparent,
          shape = CircleShape
        )
    )
  }
}

@Preview
@Composable
fun HomeBottomBarPreview() {
    HomeBottomBar(isHomeSelected = true)
}
