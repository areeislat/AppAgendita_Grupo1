package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.ui.screens.home.HomeSection
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigate: (NavEvent) -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Configuraciones",
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
                onHomeClick = { onNavigate(NavEvent.ToHome()) },
                onEventsClick = { onNavigate(NavEvent.ToHome(HomeSection.MonthlyNotes)) },
                onTeamsClick = { onNavigate(NavEvent.ToHome(HomeSection.Events)) },
                onAccountClick = { onNavigate(NavEvent.ToAccount) },
                onCreateClick = { }
            )
        },
        containerColor = Color(0xFFF7F7F9) // Un fondo gris claro como en la imagen
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Estado para los interruptores
            var permissionsEnabled by remember { mutableStateOf(true) }
            var notificationsEnabled by remember { mutableStateOf(false) }
            var darkModeEnabled by remember { mutableStateOf(false) }

            SettingsToggleItem(
                title = "Permisos",
                checked = permissionsEnabled,
                onCheckedChange = { permissionsEnabled = it }
            )
            SettingsToggleItem(
                title = "Notificaciones",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            SettingsToggleItem(
                title = "Modo Oscuro",
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre secciones

            SettingsNavigationItem(title = "Seguridad") { onNavigate(NavEvent.ToSettingsSecurity) }
            SettingsNavigationItem(title = "Ayuda") { onNavigate(NavEvent.ToSettingsHelp) }
            SettingsNavigationItem(title = "Lenguaje") { onNavigate(NavEvent.ToSettingsLanguage) }
            SettingsNavigationItem(title = "Sobre nosotros") { onNavigate(NavEvent.ToSettingsAbout) }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF7B61FF), // Púrpura de otras pantallas
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            )
        )
    }
}

@Composable
private fun SettingsNavigationItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
