package com.example.appagendita_grupo1.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Home : Routes("home")
    data object Detail : Routes("detail")
    data object Settings : Routes("settings")
}
