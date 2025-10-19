package com.example.appagendita_grupo1.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.appagendita_grupo1.navigation.NavEvent

@Composable
fun HomeScreen(
  windowSize: WindowSizeClass,
  onNavigate: (NavEvent) -> Unit
) {
  when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact  -> HomeCompact(
        onOpenSettings = { onNavigate(NavEvent.ToSettings) },
        onOpenDetail = { onNavigate(NavEvent.ToDetail) },
        onOpenEvents = { },
        onOpenTeams = { },
        onOpenAccount = { onNavigate(NavEvent.ToAccount) },
        onAddTask = { onNavigate(NavEvent.ToAddTask) },
        onAddNote = { onNavigate(NavEvent.ToAddNote) },
        onAddTeam = { onNavigate(NavEvent.ToAddTeam) },
        onAddEvent = { onNavigate(NavEvent.ToAddEvent) }
    )
  }
}
