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
fun HomeExpanded(onNavigate: (NavEvent) -> Unit) {
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
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavigationRail(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                NavigationRailItem(
                    selected = true,
                    onClick = { /* Inicio */ },
                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                    label = { Text("Inicio") }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )
                Text("¡Bienvenido a tu Agendita Virtual!", style = MaterialTheme.typography.headlineMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { onNavigate(NavEvent.ToDetail) }) { Text("Ir a Detalle") }
                    OutlinedButton(onClick = { onNavigate(NavEvent.ToSettings) }) { Text("Ir a Ajustes") }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
