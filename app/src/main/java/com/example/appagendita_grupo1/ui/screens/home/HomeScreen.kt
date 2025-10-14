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
        onOpenDetail = { onNavigate(NavEvent.ToDetail) }
    )
    //WindowWidthSizeClass.Medium   -> HomeMedium()
    //else                          -> HomeExpanded()
  }
}