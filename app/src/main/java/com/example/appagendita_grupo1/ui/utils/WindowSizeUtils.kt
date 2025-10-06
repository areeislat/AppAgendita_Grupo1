package com.example.appagendita_grupo1.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

// Enum simple
enum class AppWidthSize { Compact, Medium, Expanded }


@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun calculateAppWidthSize(activity: Activity): AppWidthSize {
    val wsc = calculateWindowSizeClass(activity) // esta función NO es composable
    return when (wsc.widthSizeClass) {
        WindowWidthSizeClass.Compact  -> AppWidthSize.Compact
        WindowWidthSizeClass.Medium   -> AppWidthSize.Medium
        WindowWidthSizeClass.Expanded -> AppWidthSize.Expanded
        else                          -> AppWidthSize.Compact
    }
}
