package com.example.appagendita_grupo1.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.viewmodel.AddTaskViewModel
import com.example.appagendita_grupo1.viewmodel.ReminderOption
import com.example.appagendita_grupo1.viewmodel.SubtaskUiState
import com.example.appagendita_grupo1.viewmodel.TaskColorOption
import com.example.appagendita_grupo1.viewmodel.TaskType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    viewModel: AddTaskViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val locale = remember { context.resources.configuration.locales[0] ?: Locale.getDefault() }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.startDate.toEpochMillis())
    val startTimePickerState = rememberTimePickerState(
        initialHour = uiState.startTime.hour,
        initialMinute = uiState.startTime.minute,
        is24Hour = false
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = uiState.endTime.hour,
        initialMinute = uiState.endTime.minute,
        is24Hour = false
    )

    LaunchedEffect(uiState.startDate) {
        datePickerState.selectedDateMillis = uiState.startDate.toEpochMillis()
    }

    LaunchedEffect(uiState.startTime) {
        startTimePickerState.setTime(uiState.startTime)
    }

    LaunchedEffect(uiState.endTime) {
        endTimePickerState.setTime(uiState.endTime)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Tarea") },
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardSection(title = "Detalles de la tarea") {
                OutlinedTextField(
                    value = uiState.taskName,
                    onValueChange = viewModel::onTaskNameChange,
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.taskNameError != null,
                    supportingText = {
                        uiState.taskNameError?.let { Text(it) }
                    }
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                Text(
                    text = "Miembros",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(3) {
                        MemberChip(name = "Jeny")
                    }
                    item {
                        AssistChip(
                            onClick = { /* TODO seleccionar miembros */ },
                            label = { Text("Agregar") },
                            leadingIcon = {
                                Icon(Icons.Filled.Add, contentDescription = null)
                            }
                        )
                    }
                }
            }

            CardSection(title = "Horario") {
                PickerTextField(
                    label = "Fecha de inicio",
                    value = uiState.startDate.formatDisplay(locale),
                    onClick = { showDatePicker = true },
                    leadingIcon = Icons.Outlined.CalendarMonth
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PickerTextField(
                        label = "Hora de inicio",
                        value = uiState.startTime.formatDisplay(locale),
                        onClick = { showStartTimePicker = true },
                        leadingIcon = Icons.Outlined.AccessTime,
                        modifier = Modifier.weight(1f)
                    )

                    PickerTextField(
                        label = "Hora de término",
                        value = uiState.endTime.formatDisplay(locale),
                        onClick = { showEndTimePicker = true },
                        leadingIcon = Icons.Outlined.AccessTime,
                        modifier = Modifier.weight(1f),
                        isError = uiState.timeError != null,
                        supportingText = uiState.timeError
                    )
                }

                ReminderDropdown(
                    selectedOption = uiState.reminder,
                    onOptionSelected = viewModel::onReminderSelected
                )
            }

            CardSection(title = "Categorización") {
                Text(
                    text = "Tipo",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskType.entries.forEach { type ->
                        FilterChip(
                            selected = uiState.taskType == type,
                            onClick = { viewModel.onTaskTypeSelected(type) },
                            label = { Text(type.displayName) }
                        )
                    }
                }

                Text(
                    text = "Color",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(TaskColorOption.pastelPalette.size) { index ->
                        val option = TaskColorOption.pastelPalette[index]
                        ColorOptionChip(
                            option = option,
                            isSelected = uiState.selectedColor == option,
                            onClick = { viewModel.onColorSelected(option) }
                        )
                    }
                }
            }

            CardSection(title = "Subtareas") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Añadir subtareas", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = uiState.subtasksEnabled,
                        onCheckedChange = viewModel::onSubtasksToggle
                    )
                }

                if (uiState.subtasksEnabled) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.subtasks.forEach { subtask ->
                            SubtaskRow(
                                subtask = subtask,
                                onTitleChange = { viewModel.onSubtaskTitleChange(subtask.id, it) },
                                onCheckedChange = { viewModel.onSubtaskCheckedChange(subtask.id, it) },
                                onRemove = { viewModel.onRemoveSubtask(subtask.id) }
                            )
                        }

                        TextButton(onClick = viewModel::onAddSubtask) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(Modifier.size(4.dp))
                            Text("Agregar subtarea")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = { /* TODO Guardar tarea */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canSave
            ) {
                Text("Guardar")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onDateSelected(it.toLocalDate())
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showStartTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            onConfirm = {
                viewModel.onStartTimeSelected(startTimePickerState.toLocalTime())
                showStartTimePicker = false
            },
            state = startTimePickerState,
            title = "Seleccionar hora de inicio"
        )
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            onConfirm = {
                viewModel.onEndTimeSelected(endTimePickerState.toLocalTime())
                showEndTimePicker = false
            },
            state = endTimePickerState,
            title = "Seleccionar hora de término"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    state: TimePickerState,
    title: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancelar") }
        },
        title = { Text(title) },
        text = { TimePicker(state = state) }
    )
}

@Composable
private fun CardSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun MemberChip(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PickerTextField(
    label: String,
    value: String,
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        readOnly = true,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } }
    )
}

@Composable
private fun ColorOptionChip(
    option: TaskColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(option.label) },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = option.color, shape = CircleShape)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) option.color.copy(alpha = 0.24f) else MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) option.color else MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun SubtaskRow(
    subtask: SubtaskUiState,
    onTitleChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(checked = subtask.isDone, onCheckedChange = onCheckedChange)
        OutlinedTextField(
            value = subtask.title,
            onValueChange = onTitleChange,
            label = { Text("Subtarea") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Outlined.Close, contentDescription = "Eliminar subtarea")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderDropdown(
    selectedOption: ReminderOption,
    onOptionSelected: (ReminderOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedOption.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Recordatorio") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ReminderOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddTaskScreenPreview() {
    AppAgendita_Grupo1Theme {
        AddTaskScreen(onBack = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun TimePickerState.setTime(time: LocalTime) {
    this.hour = time.hour
    this.minute = time.minute
}


@OptIn(ExperimentalMaterial3Api::class)
private fun TimePickerState.toLocalTime(): LocalTime = LocalTime.of(hour, minute)

private fun LocalTime.formatDisplay(locale: Locale): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", locale)
    return format(formatter).lowercase(locale)
}

private fun LocalDate.formatDisplay(locale: Locale): String {
    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", locale)
    return format(formatter)
}

private fun LocalDate.toEpochMillis(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}
