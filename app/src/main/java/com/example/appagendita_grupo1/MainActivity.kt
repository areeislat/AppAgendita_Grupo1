package com.example.appagendita_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.ui.screens.HomeScreen
import com.example.appagendita_grupo1.ui.utils.calculateAppWidthSize  // ← NUEVO import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAgendita_Grupo1Theme {
                val appWidthSize = calculateAppWidthSize(this)   // aca usamos utils
                HomeScreen(appWidthSize)                         // aca lo pasamos a la fachada
            }
        }
    }
}
