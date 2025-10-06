package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.navigation.NavEvent

@Composable
fun SettingsScreen(onNavigate: (NavEvent) -> Unit = {}) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* ya estás en Ajustes */ },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Ajustes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(NavEvent.ToHome) },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)              // evita solaparse con la bottom bar
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pantalla de Ajustes", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { onNavigate(NavEvent.ToDetail) }) {
                Text("Ir a Detalle")
            }
        }
    }
}
