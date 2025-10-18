package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagendita_grupo1.viewmodel.AddEventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    onBack: () -> Unit,
    onEventSaved: () -> Unit,
    viewModel: AddEventViewModel = viewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Evento") },
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
        ) {
            OutlinedTextField(
                value = state.eventName,
                onValueChange = { viewModel.onEventNameChange(it) },
                label = { Text("Nombre del evento") },
                isError = state.eventNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.eventNameError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.eventDate,
                onValueChange = { viewModel.onEventDateChange(it) },
                label = { Text("Fecha") },
                isError = state.eventDateError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.eventDateError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.startTime,
                    onValueChange = { viewModel.onStartTimeChange(it) },
                    label = { Text("Hora de inicio") },
                    isError = state.startTimeError != null,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = state.endTime,
                    onValueChange = { viewModel.onEndTimeChange(it) },
                    label = { Text("Hora de término") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (viewModel.validate()) {
                        onEventSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
