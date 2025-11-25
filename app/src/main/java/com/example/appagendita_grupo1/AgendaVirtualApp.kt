package com.example.appagendita_grupo1

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgendaVirtualApp : Application() {
    // Ya no necesitamos instanciar nada aquí manualmente.
    // Hilt se encarga de todo.
}