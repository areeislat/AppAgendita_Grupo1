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
    Font(R.font.poller_one_regular, FontWeight.Normal)
)

// 2. Define tu nueva familia de fuentes para Poppins
val PoppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal), // Poppins Regular
    Font(R.font.poppins_medium, FontWeight.Medium), // Poppins Medium
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold) // Poppins SemiBold
)

val AppTypography = Typography(
    // Para los titulares grandes:
    headlineLarge = TextStyle(
        fontFamily = PollerOneRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp
    ),

    // Se configuran otros estilos
    // Estilo para el cuerpo del texto principal
    bodyLarge = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Estilo para títulos de sección (ej. usando Medium)
    titleLarge = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Estilo para texto que necesita más énfasis (ej. usando SemiBold)
    labelLarge = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)