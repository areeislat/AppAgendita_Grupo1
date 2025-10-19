package com.example.appagendita_grupo1.ui.screens.home.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.screens.home.components.ProgressTaskCard
import com.example.appagendita_grupo1.ui.screens.home.components.ProjectHighlightCard
import com.example.appagendita_grupo1.ui.screens.home.components.SectionHeader
import com.example.appagendita_grupo1.ui.screens.home.components.TitleBlock
import com.example.appagendita_grupo1.ui.screens.home.components.sampleTasks
import com.example.appagendita_grupo1.ui.theme.AppTypography
import com.example.appagendita_grupo1.ui.theme.BlueAccent
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.PurplePrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    onOpenDetail: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp, bottom = 96.dp)
    ) {
        item { TitleBlock() }
        item { ProjectHighlightCard(onClick = onOpenDetail) }
        item { SectionHeader(title = "En progreso") }
        items(sampleTasks) { task ->
            ProgressTaskCard(task = task, onClick = onOpenDetail)
        }
    }
}

@Composable
fun TodayTasksSection(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit
) {
    val schedules = remember { generateDailySchedules() }
    var selectedDayIndex by remember {
        mutableStateOf(schedules.indexOfFirst { it.isToday }.takeIf { it >= 0 } ?: 0)
    }
    val selectedSchedule = schedules[selectedDayIndex]
    val locale = remember { Locale("es", "ES") }
    val monthFormat = remember { SimpleDateFormat("MMMM", locale) }
    val formattedHeader = remember(selectedSchedule) {
        val month = monthFormat.format(selectedSchedule.date.time)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        "$month, ${selectedSchedule.dayNumber} ✍️"
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 8.dp, bottom = 96.dp)
    ) {
        item {
            SectionHeadline(
                title = "Tareas de hoy",
                info = formattedHeader,
                subtitle = "${selectedSchedule.tasks.size} tareas programadas",
                actionIcon = Icons.Outlined.AddCircle,
                onActionClick = onAddTask
            )
        }
        item {
            DaySelectorRow(
                schedules = schedules,
                selectedIndex = selectedDayIndex,
                onSelected = { selectedDayIndex = it }
            )
        }
        items(selectedSchedule.tasks) { task ->
            ScheduleCard(task = task)
        }
        if (selectedSchedule.tasks.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "Sin tareas",
                    message = "No tienes tareas planificadas para este día."
                )
            }
        }
    }
}

@Composable
fun MonthlyNotesSection(
    modifier: Modifier = Modifier,
    onAddNote: () -> Unit
) {
    val locale = remember { Locale("es", "ES") }
    val monthFormat = remember { SimpleDateFormat("MMMM", locale) }
    val todayCalendar = remember { Calendar.getInstance() }
    val notes = remember { sampleMonthlyNotes() }
    val notesByDay = remember { notes.groupBy { it.dayOfMonth } }
    var selectedDay by remember { mutableStateOf(todayCalendar.get(Calendar.DAY_OF_MONTH)) }

    val monthName = remember {
        monthFormat.format(todayCalendar.time)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }

    val selectedNotes = notesByDay[selectedDay] ?: emptyList()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 8.dp, bottom = 96.dp)
    ) {
        item {
            SectionHeadline(
                title = "Notas del mes",
                info = "$monthName, $selectedDay ✍️",
                subtitle = "${notes.size} notas creadas",
                actionIcon = Icons.Outlined.Edit,
                onActionClick = onAddNote
            )
        }
        item {
            MonthCalendar(
                monthCalendar = todayCalendar,
                selectedDay = selectedDay,
                highlightedDays = notesByDay.keys,
                onSelectDay = { selectedDay = it }
            )
        }
        item {
            Text(
                text = if (selectedNotes.isNotEmpty()) "Notas del día" else "Sin notas para este día",
                style = AppTypography.bodyLarge,
                color = NavyText
            )
        }
        if (selectedNotes.isNotEmpty()) {
            items(selectedNotes) { note ->
                NoteDetailCard(note = note)
            }
        }
        item {
            Text(
                text = "Previsualización del mes",
                style = AppTypography.bodyLarge,
                color = NavyText
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(notes) { note ->
                    NotePreviewCard(note = note, isSelected = note.dayOfMonth == selectedDay)
                }
            }
        }
    }
}

@Composable
fun EventsSection(
    modifier: Modifier = Modifier,
    onAddEvent: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val events = remember { sampleEvents() }
    var selectedFilter by remember { mutableStateOf(EventFilter.Favorites) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 8.dp, bottom = 96.dp)
    ) {
        item {
            SectionHeadline(
                title = "Eventos",
                info = "${events.count { it.filter == selectedFilter || selectedFilter == EventFilter.All }} eventos activos",
                subtitle = "Gestiona y revisa el progreso",
                actionIcon = Icons.Outlined.Schedule,
                onActionClick = onAddEvent
            )
        }
        item {
            EventFilterRow(selected = selectedFilter, onFilterSelected = { selectedFilter = it })
        }
        items(events.filter { selectedFilter == EventFilter.All || it.filter == selectedFilter }) { event ->
            EventCard(event = event, onClick = onOpenDetail)
        }
    }
}

@Composable
private fun SectionHeadline(
    title: String,
    info: String,
    subtitle: String,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onActionClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = AppTypography.titleLarge, color = NavyText)
            IconButton(onClick = onActionClick) {
                Icon(imageVector = actionIcon, contentDescription = null, tint = PurplePrimary)
            }
        }
        Text(text = info, style = AppTypography.bodyLarge, color = NavyText)
        Text(text = subtitle, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.6f))
    }
}

@Composable
private fun DaySelectorRow(
    schedules: List<DailySchedule>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(schedules) { index, schedule ->
            DayChip(
                label = schedule.dayOfWeekShort,
                dayNumber = schedule.dayNumber,
                selected = index == selectedIndex,
                onClick = { onSelected(index) }
            )
        }
    }
}

@Composable
private fun DayChip(
    label: String,
    dayNumber: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) PurplePrimary else Color.White
    val textColor = if (selected) Color.White else NavyText
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = background,
        border = if (selected) null else BorderStroke(1.dp, CardStroke)
    ) {
        Column(
            modifier = Modifier
                .width(64.dp)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = AppTypography.bodyMedium, color = textColor)
            Spacer(Modifier.height(6.dp))
            Text(text = dayNumber.toString(), style = AppTypography.bodyLarge, color = textColor)
        }
    }
}

@Composable
private fun ScheduleCard(task: ScheduledTask) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(60.dp)) {
            Text(text = task.startTime, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.7f))
            Text(text = task.endTime, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.4f))
        }
        Surface(
            modifier = Modifier.weight(1f),
            color = task.color.copy(alpha = 0.12f),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = task.title, style = AppTypography.bodyLarge, color = NavyText)
                Spacer(Modifier.height(4.dp))
                Text(text = task.description, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.7f))
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AvatarGroup(initials = task.participants)
                    Text(text = task.timeRange, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.6f))
                }
            }
        }
    }
}

@Composable
private fun AvatarGroup(initials: List<String>, size: Dp = 36.dp) {
    if (initials.isEmpty()) return
    Box(modifier = Modifier.width(size + (initials.size - 1).coerceAtLeast(0) * (size / 2))) {
        initials.take(3).forEachIndexed { index, name ->
            Surface(
                modifier = Modifier
                    .size(size)
                    .offset(x = (index * (size / 2))),
                color = PurplePrimary.copy(alpha = 0.2f),
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = name, style = AppTypography.bodyMedium, color = PurplePrimary, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(title: String, message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = AppTypography.bodyLarge, color = NavyText)
            Text(text = message, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun MonthCalendar(
    monthCalendar: Calendar,
    selectedDay: Int,
    highlightedDays: Set<Int>,
    onSelectDay: (Int) -> Unit
) {
    val dayNames = remember { listOf("L", "M", "X", "J", "V", "S", "D") }
    val firstDayCalendar = remember(monthCalendar.get(Calendar.YEAR), monthCalendar.get(Calendar.MONTH)) {
        (monthCalendar.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val daysInMonth = remember(firstDayCalendar.get(Calendar.YEAR), firstDayCalendar.get(Calendar.MONTH)) {
        firstDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    val firstDayOfWeek = remember(firstDayCalendar.get(Calendar.YEAR), firstDayCalendar.get(Calendar.MONTH)) {
        val day = firstDayCalendar.get(Calendar.DAY_OF_WEEK)
        (day + 5) % 7
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dayNames.forEach { day ->
                Text(
                    text = day,
                    style = AppTypography.bodyMedium,
                    color = NavyText.copy(alpha = 0.6f),
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        val totalCells = firstDayOfWeek + daysInMonth
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(240.dp)
        ) {
            items(totalCells) { index ->
                if (index < firstDayOfWeek) {
                    Box(modifier = Modifier.size(40.dp))
                } else {
                    val dayNumber = index - firstDayOfWeek + 1
                    val isSelected = dayNumber == selectedDay
                    val hasNote = highlightedDays.contains(dayNumber)
                    CalendarDay(
                        day = dayNumber,
                        selected = isSelected,
                        hasNote = hasNote,
                        onSelect = { onSelectDay(dayNumber) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(day: Int, selected: Boolean, hasNote: Boolean, onSelect: () -> Unit) {
    val background = if (selected) PurplePrimary else Color.White
    val textColor = if (selected) Color.White else NavyText
    Surface(
        onClick = onSelect,
        shape = CircleShape,
        color = background,
        border = if (selected) null else BorderStroke(1.dp, CardStroke),
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = day.toString(), style = AppTypography.bodyMedium, color = textColor)
            if (hasNote) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(BlueAccent)
                )
            }
        }
    }
}

@Composable
private fun NoteDetailCard(note: MonthlyNote) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = note.title, style = AppTypography.bodyLarge, color = NavyText)
            Text(text = note.content, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.75f))
        }
    }
}

@Composable
private fun NotePreviewCard(note: MonthlyNote, isSelected: Boolean) {
    val background = if (isSelected) PurplePrimary else Color.White
    val textColor = if (isSelected) Color.White else NavyText
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = background,
        border = if (isSelected) null else BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "${note.dayOfMonth} ${note.monthShort}", style = AppTypography.bodyMedium, color = textColor)
            Text(text = note.preview, style = AppTypography.bodyMedium, color = textColor.copy(alpha = if (isSelected) 0.85f else 0.65f))
        }
    }
}

@Composable
private fun EventFilterRow(selected: EventFilter, onFilterSelected: (EventFilter) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        EventFilter.values().forEach { filter ->
            AssistChip(
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(text = filter.label, style = AppTypography.bodyMedium)
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected == filter) PurplePrimary.copy(alpha = 0.12f) else Color.White,
                    labelColor = if (selected == filter) PurplePrimary else NavyText
                ),
                border = BorderStroke(1.dp, if (selected == filter) PurplePrimary.copy(alpha = 0.4f) else CardStroke)
            )
        }
    }
}

@Composable
private fun EventCard(event: EventSummary, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = event.title, style = AppTypography.bodyLarge, color = NavyText)
                    Spacer(Modifier.height(4.dp))
                    Text(text = event.category, style = AppTypography.bodyMedium, color = NavyText.copy(alpha = 0.65f))
                }
                Text(text = "${event.completed}/${event.total}", style = AppTypography.bodyMedium, color = BlueAccent, fontWeight = FontWeight.SemiBold)
            }
            LinearProgressIndicator(
                progress = { event.progress },
                trackColor = BlueAccent.copy(alpha = 0.2f),
                color = BlueAccent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

private data class ScheduledTask(
    val title: String,
    val description: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val participants: List<String>,
    val color: Color
) {
    val startTime: String get() = formatTime(startHour, startMinute)
    val endTime: String get() = formatTime(endHour, endMinute)
    val timeRange: String get() = "$startTime - $endTime"
}

private data class DailySchedule(
    val date: Calendar,
    val tasks: List<ScheduledTask>
) {
    val dayNumber: Int get() = date.get(Calendar.DAY_OF_MONTH)
    val dayOfWeekShort: String
        get() {
            val locale = Locale("es", "ES")
            val formatter = SimpleDateFormat("EEE", locale)
            return formatter.format(date.time)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        }
    val isToday: Boolean
        get() {
            val today = Calendar.getInstance()
            return date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                date.get(Calendar.YEAR) == today.get(Calendar.YEAR)
        }
}

private data class MonthlyNote(
    val dayOfMonth: Int,
    val monthShort: String,
    val title: String,
    val preview: String,
    val content: String
)

private enum class EventFilter(val label: String) {
    Favorites("Favoritos"),
    Recent("Recientes"),
    All("Todos")
}

private data class EventSummary(
    val title: String,
    val category: String,
    val completed: Int,
    val total: Int,
    val filter: EventFilter
) {
    val progress: Float get() = if (total == 0) 0f else completed.toFloat() / total.toFloat()
}

private fun formatTime(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

private fun generateDailySchedules(): List<DailySchedule> {
    val base = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return (-1..3).map { offset ->
        val dayCalendar = (base.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, offset) }
        val tasks = when (offset) {
            -1 -> listOf(
                ScheduledTask(
                    title = "Revisión semanal",
                    description = "Preparar pendientes para la semana",
                    startHour = 9,
                    startMinute = 0,
                    endHour = 10,
                    endMinute = 0,
                    participants = listOf("AL", "JM"),
                    color = PurplePrimary
                )
            )
            0 -> listOf(
                ScheduledTask(
                    title = "Wireframe elements 😊",
                    description = "10am - 11am",
                    startHour = 10,
                    startMinute = 0,
                    endHour = 11,
                    endMinute = 0,
                    participants = listOf("AR", "CM", "JD"),
                    color = PurplePrimary
                ),
                ScheduledTask(
                    title = "Mobile app design 😍",
                    description = "Revisión de pantallas principales",
                    startHour = 11,
                    startMinute = 40,
                    endHour = 12,
                    endMinute = 40,
                    participants = listOf("AR", "PG"),
                    color = BlueAccent
                ),
                ScheduledTask(
                    title = "Design team call 😎",
                    description = "Sincronización semanal",
                    startHour = 13,
                    startMinute = 20,
                    endHour = 14,
                    endMinute = 20,
                    participants = listOf("JD", "CM"),
                    color = Color(0xFFFFC85B)
                )
            )
            1 -> listOf(
                ScheduledTask(
                    title = "Investigación de usuario",
                    description = "Entrevistas iniciales",
                    startHour = 9,
                    startMinute = 30,
                    endHour = 11,
                    endMinute = 0,
                    participants = listOf("AC", "PG"),
                    color = BlueAccent
                )
            )
            2 -> listOf(
                ScheduledTask(
                    title = "Prototipo interactivo",
                    description = "Preparar presentación",
                    startHour = 14,
                    startMinute = 0,
                    endHour = 16,
                    endMinute = 0,
                    participants = listOf("JD", "AL", "CM"),
                    color = PurplePrimary
                )
            )
            else -> emptyList()
        }
        DailySchedule(date = dayCalendar, tasks = tasks)
    }
}

private fun sampleMonthlyNotes(): List<MonthlyNote> {
    val locale = Locale("es", "ES")
    val monthShortFormatter = SimpleDateFormat("MMM", locale)
    val calendar = Calendar.getInstance()
    return listOf(3, 8, 12, 18, 25).map { day ->
        val dayCalendar = (calendar.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day) }
        MonthlyNote(
            dayOfMonth = day,
            monthShort = monthShortFormatter.format(dayCalendar.time)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() },
            title = when (day) {
                3 -> "Ideas de campaña"
                8 -> "Retro semanal"
                12 -> "Apuntes de UX"
                18 -> "Entrevistas"
                25 -> "Resumen mensual"
                else -> "Nota"
            },
            preview = when (day) {
                3 -> "Conceptos principales"
                8 -> "Feedback del equipo"
                12 -> "Insights clave"
                18 -> "Hallazgos clientes"
                25 -> "Metas alcanzadas"
                else -> "Nota"
            },
            content = when (day) {
                3 -> "Se definieron tres líneas de comunicación y próximos pasos para la campaña."
                8 -> "El equipo destacó la necesidad de mejorar la comunicación diaria y revisar el backlog."
                12 -> "Anotar patrones de navegación detectados durante las pruebas iniciales."
                18 -> "Resumen de las entrevistas realizadas con usuarios potenciales y puntos de dolor."
                25 -> "Síntesis de entregables finalizados y tareas pendientes para el siguiente mes."
                else -> ""
            }
        )
    }
}

private fun sampleEvents(): List<EventSummary> {
    return listOf(
        EventSummary("Unity Dashboard 😌", "Diseño", 10, 20, EventFilter.Favorites),
        EventSummary("Instagram Shots ✏️", "Marketing", 10, 20, EventFilter.Recent),
        EventSummary("Cubbles", "Investigación", 6, 15, EventFilter.All),
        EventSummary("Ui8 Platform", "Diseño", 4, 10, EventFilter.All)
    )
}
