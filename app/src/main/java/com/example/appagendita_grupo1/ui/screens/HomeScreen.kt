package com.example.appagendita_grupo1.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appagendita_grupo1.ui.utils.AppWidthSize
import com.example.appagendita_grupo1.navigation.NavEvent

@Composable
fun HomeScreen(
    appWidthSize: AppWidthSize,
    onNavigate: (NavEvent) -> Unit
) {
    when (appWidthSize) {
        AppWidthSize.Compact  -> HomeCompact(onNavigate)
        AppWidthSize.Medium   -> HomeMedium(onNavigate)
        AppWidthSize.Expanded -> HomeExpanded(onNavigate)
    }
}

/* Previews sin navegación real (lambda vacío) */
@Preview(name = "Solo Compact", showBackground = true, widthDp = 360, heightDp = 800)
@Composable fun PreviewHome_Compact() { HomeCompact(onNavigate = {}) }

@Preview(name = "Solo Medium", showBackground = true, widthDp = 840, heightDp = 900)
@Composable fun PreviewHome_Medium() { HomeMedium(onNavigate = {}) }

@Preview(name = "Solo Expanded", showBackground = true, widthDp = 1200, heightDp = 900)
@Composable fun PreviewHome_Expanded() { HomeExpanded(onNavigate = {}) }
