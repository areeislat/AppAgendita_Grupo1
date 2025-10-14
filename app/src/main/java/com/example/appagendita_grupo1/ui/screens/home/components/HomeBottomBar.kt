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

@Composable
fun HomeBottomBar(onSettingsClick: () -> Unit) {
  BottomAppBar(containerColor = Color.White) {
    NavigationBarItem(selected = true,  onClick = { }, icon = { Icon(Icons.Outlined.Home, null) })
    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Outlined.ChatBubbleOutline, null) })
    // El FAB ocupará el espacio central
    NavigationBarItem(selected = false, onClick = { }, icon = { Icon(Icons.Outlined.MoreHoriz, null) })
    NavigationBarItem(selected = false, onClick = onSettingsClick, icon = { Icon(Icons.Outlined.Settings, null) })
  }
}