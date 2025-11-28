// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // --- INICIO DE CAMBIOS ---
    // Añade el plugin KSP (Kotlin Symbol Processing)
    alias(libs.plugins.ksp) apply false
    // --- FIN DE CAMBIOS ---
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}