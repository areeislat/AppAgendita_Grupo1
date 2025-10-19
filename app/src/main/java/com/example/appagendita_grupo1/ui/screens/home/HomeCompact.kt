package com.example.appagendita_grupo1.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.appagendita_grupo1.ui.screens.home.components.BottomSheetHandle
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.screens.home.components.HomeTopHeader
import com.example.appagendita_grupo1.ui.screens.home.components.ProgressTaskCard
import com.example.appagendita_grupo1.ui.screens.home.components.ProjectHighlightCard
import com.example.appagendita_grupo1.ui.screens.home.components.SectionHeader
import com.example.appagendita_grupo1.ui.screens.home.components.TitleBlock
import com.example.appagendita_grupo1.ui.screens.home.components.sampleTasks
import com.example.appagendita_grupo1.ui.theme.Bg
import com.example.appagendita_grupo1.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompact(
  onOpenSettings: () -> Unit = {},
  onOpenDetail: () -> Unit = {},
  onAddTask: () -> Unit = {},
  onAddNote: () -> Unit = {},
  onAddTeam: () -> Unit = {},
  onAddEvent: () -> Unit = {}
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var showSheet by remember { mutableStateOf(false) }

  Scaffold(
    containerColor = Bg,
    topBar = { HomeTopHeader(onLeftClick = {}, onRightClick = onOpenSettings) },
    floatingActionButton = {
        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = PurplePrimary
        ) { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White) }
    },
    floatingActionButtonPosition = FabPosition.Center,
    bottomBar = {
      HomeBottomBar(
          isHomeSelected = true,
          onSettingsClick = onOpenSettings,
          onHomeClick = { /* Ya estás aquí */ }
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item { TitleBlock() }
      item { ProjectHighlightCard(onClick = onOpenDetail) }
      item { SectionHeader(title = "En progreso") }
      items(sampleTasks) { task ->
        ProgressTaskCard(task = task, onClick = onOpenDetail)
      }
      item { Spacer(Modifier.height(88.dp)) } // para despegar del bottom bar
    }
  }

  if (showSheet) {
    ModalBottomSheet(
      onDismissRequest = { showSheet = false },
      sheetState = sheetState,
      containerColor = Color.White,
      shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
      BottomSheetHandle()
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
    HomeCompact()
}
