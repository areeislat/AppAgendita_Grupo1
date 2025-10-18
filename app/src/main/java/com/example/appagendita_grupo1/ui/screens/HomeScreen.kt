package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appagendita_grupo1.navigation.NavEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    windowSize: WindowSizeClass,
    onNavigate: (NavEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("AppAgendita") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigate(NavEvent.ToAddTask) }) {
                Icon(Icons.Filled.Add, contentDescription = "Crear nota")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // TODO: Add your home screen content here
        }
    }
}
