package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagendita_grupo1.viewmodel.AddTeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamScreen(
    onBack: () -> Unit,
    onTeamSaved: () -> Unit,
    viewModel: AddTeamViewModel = viewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Equipo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            IconButton(
                onClick = { /* TODO: Handle image picking */ },
                modifier = Modifier.size(100.dp).clip(CircleShape)
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = "Añadir logo equipo", modifier = Modifier.size(50.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Añadir logo equipo", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Tu logotipo se publicará siempre", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.teamName,
                onValueChange = { viewModel.onTeamNameChange(it) },
                label = { Text("Nombre equipo") },
                isError = state.teamNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.teamNameError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Start)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Miembros del Team", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(4) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Member", modifier = Modifier.size(40.dp))
                        Text(text = "Jeny", fontSize = 12.sp)
                    }
                }
                item {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Member")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Tipo", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val types = listOf("Privado", "Publico", "Secreto")
                types.forEach { type ->
                    FilterChip(
                        selected = state.teamType == type,
                        onClick = { viewModel.onTeamTypeChange(type) },
                        label = { Text(type) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (viewModel.validate()) {
                        onTeamSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Create Team")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTeamScreenPreview() {
    AddTeamScreen(onBack = {}, onTeamSaved = {})
}
