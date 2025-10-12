package com.example.appagendita_grupo1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.appagendita_grupo1.R

// 1. Define tu familia de fuentes personalizadas
val PollerOneRegular = FontFamily(
    Font(R.font.poller_one_regular, FontWeight.Normal),
    Font(R.font.poller_one_regular, FontWeight.Bold)
)

val AppTypography = Typography(
    // Para los titulares grandes:
    headlineLarge = TextStyle(
        fontFamily = PollerOneRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp
    ),

    // Se configuran otros estilos
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // O tu otra fuente para el cuerpo del texto
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)