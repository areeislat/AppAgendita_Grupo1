package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.AppTypography
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeTopHeader(onLeftClick: () -> Unit, onRightClick: () -> Unit) {
    // Obtenemos la fecha actual formateada
    val currentDate = getCurrentFormattedDate()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RoundIconButton(icon = Icons.Outlined.GridView, onClick = onLeftClick)
        // Usamos la variable con la fecha actual
        Text(currentDate, style = AppTypography.titleMedium)
        RoundIconButton(icon = Icons.Outlined.Notifications, onClick = onRightClick)
    }
}

@Composable
private fun getCurrentFormattedDate(): String {
    // Especificamos el Locale para que el día de la semana esté en español
    val locale = Locale("es", "ES")
    val calendar = Calendar.getInstance()
    // El patrón "EEEE, d" da como resultado el nombre completo del día de la semana y el número del día.
    // Ejemplo: "viernes, 26"
    val sdf = SimpleDateFormat("EEEE, d", locale)
    val formattedDate = sdf.format(calendar.time)

    // Capitalizamos la primera letra del día de la semana
    return formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}


@Composable
fun TitleBlock() {
    Column {
        Text(
            text = "Hagamos\nhábitos juntos 👏🏻",
            style = AppTypography.titleLarge,
            color = NavyText,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun RoundIconButton(icon: ImageVector, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        tonalElevation = 1.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
            Icon(icon, contentDescription = null, tint = NavyText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopHeaderPreview() {
    HomeTopHeader(onLeftClick = {}, onRightClick = {})
}

@Preview(showBackground = true)
@Composable
fun TitleBlockPreview() {
    TitleBlock()
}
