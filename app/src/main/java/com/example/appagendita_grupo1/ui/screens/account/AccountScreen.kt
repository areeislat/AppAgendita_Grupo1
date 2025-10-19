package com.example.appagendita_grupo1.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.screens.home.components.HomeBottomBar
import com.example.appagendita_grupo1.ui.theme.AppTypography
import com.example.appagendita_grupo1.ui.theme.Bg
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.PurplePrimary

@Composable
fun AccountScreen(
    name: String = "Alvart Ainstain",
    username: String = "@alvart.ainstain",
    inProgress: Int = 5,
    completed: Int = 25,
    onEditProfile: () -> Unit = {},
    onOpenEvents: () -> Unit = {},
    onOpenTeams: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenTasks: () -> Unit = {},
    onOpenCreate: () -> Unit = {},
    onNavigateHome: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Bg,
        bottomBar = {
            HomeBottomBar(
                isAccountSelected = true,
                onHomeClick = onNavigateHome,
                onEventsClick = onOpenEvents,
                onTeamsClick = onOpenTeams,
                onAccountClick = { /* Ya estás aquí */ },
                onCreateClick = onOpenCreate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileCard(
                name = name,
                username = username,
                onEditProfile = onEditProfile
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AccountStatCard(
                    title = "En Proceso",
                    value = inProgress.toString(),
                    modifier = Modifier.weight(1f)
                )
                AccountStatCard(
                    title = "Total Completo",
                    value = completed.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccountMenuItem(title = "Mis Eventos", onClick = onOpenEvents)
                AccountMenuItem(title = "Mis Equipos", onClick = onOpenTeams)
                AccountMenuItem(title = "Configuraciones", onClick = onOpenSettings)
                AccountMenuItem(title = "Mis tareas", onClick = onOpenTasks)
            }

            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun ProfileCard(
    name: String,
    username: String,
    onEditProfile: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Perfil",
                style = AppTypography.bodyMedium,
                color = Color(0xFF8D8D9C)
            )

            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE9E7FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(54.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = name,
                    style = AppTypography.titleLarge,
                    color = NavyText,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = username,
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF9EA2B1)
                )
            }

            Button(
                onClick = onEditProfile,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp)
            ) {
                Text(text = "Editar", style = AppTypography.labelLarge)
            }
        }
    }
}

@Composable
private fun AccountStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = AppTypography.displaySmall,
                color = NavyText
            )
            Text(
                text = title,
                style = AppTypography.bodyMedium,
                color = Color(0xFF9EA2B1)
            )
        }
    }
}

@Composable
private fun AccountMenuItem(title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 3.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = AppTypography.bodyLarge,
                color = NavyText
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFBDC1CE)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}
