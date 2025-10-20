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
  section: HomeSection? = null
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

      HomeSection.MonthlyNotes -> MonthlyNotesSection(
        modifier = contentModifier,
        onAddNote = onAddNote
      )

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
    HomeCompact()
}
