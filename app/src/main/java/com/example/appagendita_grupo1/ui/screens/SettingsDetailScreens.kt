package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDetailScaffold(
    title: String,
    onNavigate: (NavEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = AppTypography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(NavEvent.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            HomeBottomBar(
                onHomeClick = { onNavigate(NavEvent.ToHome) },
                onEventsClick = { },
                onTeamsClick = { },
                onAccountClick = { onNavigate(NavEvent.ToAccount) },
                onCreateClick = { }
            )
        },
        containerColor = Color(0xFFF7F7F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(contentPadding)
                .fillMaxSize()
                .background(Color.Transparent)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SecuritySettingsScreen(onNavigate: (NavEvent) -> Unit) {
    SettingsDetailScaffold(title = "Seguridad", onNavigate = onNavigate) {
        Text(
            text = "Configura opciones como la autenticación en dos pasos y el bloqueo con biometría para mantener tu cuenta protegida.",
            style = AppTypography.bodyMedium,
            color = Color.DarkGray
        )
        Text(
            text = "• Autenticación en dos pasos\n• Bloqueo con huella o rostro\n• Administrar dispositivos vinculados",
            style = AppTypography.bodySmall,
            color = Color.DarkGray
        )
    }
}

@Composable
fun HelpSettingsScreen(onNavigate: (NavEvent) -> Unit) {
    SettingsDetailScaffold(title = "Ayuda", onNavigate = onNavigate) {
        Text(
            text = "Encuentra respuestas a preguntas frecuentes y ponte en contacto con nuestro equipo de soporte si necesitas asistencia.",
            style = AppTypography.bodyMedium,
            color = Color.DarkGray
        )
        Text(
            text = "• Preguntas frecuentes\n• Centro de ayuda\n• Contactar soporte",
            style = AppTypography.bodySmall,
            color = Color.DarkGray
        )
    }
}

@Composable
fun LanguageSettingsScreen(onNavigate: (NavEvent) -> Unit) {
    SettingsDetailScaffold(title = "Lenguaje", onNavigate = onNavigate) {
        Text(
            text = "Personaliza el idioma de la aplicación y configura formatos regionales para fechas y horas.",
            style = AppTypography.bodyMedium,
            color = Color.DarkGray
        )
        Text(
            text = "• Idioma de la interfaz\n• Formato de fecha\n• Formato de hora",
            style = AppTypography.bodySmall,
            color = Color.DarkGray
        )
    }
}

@Composable
fun AboutSettingsScreen(onNavigate: (NavEvent) -> Unit) {
    SettingsDetailScaffold(title = "Sobre nosotros", onNavigate = onNavigate) {
        Text(
            text = "Conoce más sobre el equipo detrás de AppAgendita y revisa la información legal relevante.",
            style = AppTypography.bodyMedium,
            color = Color.DarkGray
        )
        Text(
            text = "• Nuestro equipo\n• Versión de la aplicación\n• Términos y privacidad",
            style = AppTypography.bodySmall,
            color = Color.DarkGray
        )
    }
}
