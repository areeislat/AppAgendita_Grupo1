package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeBottomBar(
    isHomeSelected: Boolean = false,
    isSettingsSelected: Boolean = false,
    onHomeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
  BottomAppBar(containerColor = Color.White) {
    NavigationBarItem(selected = isHomeSelected,  onClick = onHomeClick, icon = { Icon(Icons.Outlined.Home, null) })
    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Outlined.ChatBubbleOutline, null) })
    // El FAB ocupará el espacio central
    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Outlined.MoreHoriz, null) })
    NavigationBarItem(selected = isSettingsSelected, onClick = onSettingsClick, icon = { Icon(Icons.Outlined.Settings, null) })
  }
}

@Preview
@Composable
fun HomeBottomBarPreview() {
    HomeBottomBar(isHomeSelected = true)
}
