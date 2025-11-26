package com.example.appagendita_grupo1.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory

@Composable
fun HomeScreen(
    windowSize: WindowSizeClass,
    onNavigate: (NavEvent) -> Unit,
    section: HomeSection? = null,
    noteListViewModelFactory: NoteListViewModelFactory // <-- Aceptar la factory
) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeCompact(
            onOpenSettings = { onNavigate(NavEvent.ToSettings) },
            onOpenDetail = { onNavigate(NavEvent.ToDetail) },
            onOpenAccount = { onNavigate(NavEvent.ToAccount) },
            onAddTask = { onNavigate(NavEvent.ToAddTask) },
            onAddNote = { onNavigate(NavEvent.ToAddNote) },
            onAddTeam = { onNavigate(NavEvent.ToAddTeam) }, // <- CORREGIDO: Era ToToAddTeam
            onAddEvent = { onNavigate(NavEvent.ToAddEvent) }, // <- CORREGIDO: Era ToAddTask
            section = section,
            noteListViewModelFactory = noteListViewModelFactory // <-- Pasársela a HomeCompact
        )

        WindowWidthSizeClass.Medium -> HomeCompact(
            onOpenSettings = { onNavigate(NavEvent.ToSettings) },
            onOpenDetail = { onNavigate(NavEvent.ToDetail) },
            onOpenAccount = { onNavigate(NavEvent.ToAccount) },
            onAddTask = { onNavigate(NavEvent.ToAddTask) },
            onAddNote = { onNavigate(NavEvent.ToAddNote) },
            onAddTeam = { onNavigate(NavEvent.ToAddTeam) }, // <- CORREGIDO
            onAddEvent = { onNavigate(NavEvent.ToAddEvent) }, // <- CORREGIDO
            section = section,
            noteListViewModelFactory = noteListViewModelFactory // <-- Pasársela
        )

        WindowWidthSizeClass.Expanded -> HomeCompact(
            onOpenSettings = { onNavigate(NavEvent.ToSettings) },
            onOpenDetail = { onNavigate(NavEvent.ToDetail) },
            onOpenAccount = { onNavigate(NavEvent.ToAccount) },
            onAddTask = { onNavigate(NavEvent.ToAddTask) },
            onAddNote = { onNavigate(NavEvent.ToAddNote) },
            onAddTeam = { onNavigate(NavEvent.ToAddTeam) }, // <- CORREGIDO
            onAddEvent = { onNavigate(NavEvent.ToAddEvent) }, // <- CORREGIDO
            section = section,
            noteListViewModelFactory = noteListViewModelFactory // <-- Pasársela
        )
    }
}