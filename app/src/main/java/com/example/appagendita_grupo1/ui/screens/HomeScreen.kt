package com.example.appagendita_grupo1.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appagendita_grupo1.ui.utils.AppWidthSize  // ← NUEVO import

@Composable
fun HomeScreen(appWidthSize: AppWidthSize) {
    when (appWidthSize) {
        AppWidthSize.Compact  -> HomeCompact()
        AppWidthSize.Medium   -> HomeMedium()
        AppWidthSize.Expanded -> HomeExpanded()
    }
}

/* Previews “forzados” para revisar cada layout */
@Preview(name = "Solo Compact", showBackground = true, widthDp = 360, heightDp = 800)
@Composable fun PreviewHome_Compact() { HomeCompact() }

@Preview(name = "Solo Medium", showBackground = true, widthDp = 840, heightDp = 900)
@Composable fun PreviewHome_Medium() { HomeMedium() }

@Preview(name = "Solo Expanded", showBackground = true, widthDp = 1200, heightDp = 900)
@Composable fun PreviewHome_Expanded() { HomeExpanded() }
