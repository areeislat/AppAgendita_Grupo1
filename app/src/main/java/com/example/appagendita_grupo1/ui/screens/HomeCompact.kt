package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.navigation.NavEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCompact(onNavigate: (NavEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Agendita Virtual") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Ícono estrella",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Text(text = "¡Bienvenido a tu Agendita Virtual!")

            // Botón de ejemplo que ya tenías → lo usamos para ir a Detalle
            Button(
                onClick = { onNavigate(NavEvent.ToDetail) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) { Text("Ir a Detalle") }

            // Segundo botón: ir a Ajustes
            OutlinedButton(onClick = { onNavigate(NavEvent.ToSettings) }) {
                Text("Ir a Ajustes")
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
