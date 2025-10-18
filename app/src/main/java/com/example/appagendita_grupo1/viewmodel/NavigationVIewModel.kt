package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.navigation.Routes

class NavigationViewModel : ViewModel() {

    fun onNavEvent(navController: NavController, event: NavEvent) {
        when (event) {
            NavEvent.Back -> navController.popBackStack()

            // Splash
            NavEvent.ToLogin -> navController.navigate(Routes.Login) {
                popUpTo(Routes.Splash) { inclusive = true }
            }

            // Login
            NavEvent.ToHome -> navController.navigate(Routes.Home) {
                popUpTo(Routes.Login) { inclusive = true }
            }
            NavEvent.ToRegistration -> navController.navigate(Routes.Registration)
            NavEvent.BackToSplash -> navController.navigate(Routes.Splash) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }

            // Registration
            NavEvent.ToLoginFromRegistration -> navController.navigate(Routes.Login) {
                popUpTo(Routes.Registration) { inclusive = true }
            }

            // Home
            NavEvent.ToDetail -> navController.navigate(Routes.Detail)
            NavEvent.ToSettings -> navController.navigate(Routes.Settings)
            NavEvent.ToAddTask -> navController.navigate(Routes.AddTask)
            NavEvent.ToAddNote -> navController.navigate(Routes.AddNote)
            NavEvent.ToAddTeam -> navController.navigate(Routes.AddTeam)
            NavEvent.ToAddEvent -> navController.navigate(Routes.AddEvent)
        }
    }
}
