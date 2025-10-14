package com.example.appagendita_grupo1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
  primary = PurplePrimary,
  onPrimary = Color.White,
  secondary = BlueAccent,
  background = Bg,
  surface = Color.White,
  onSurface = NavyText,
  outline = CardStroke
)

@Composable
fun AppAgendita_Grupo1Theme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = LightColors,
    typography = AppTypography,
    shapes = AppShapes,
    content = content
  )
}