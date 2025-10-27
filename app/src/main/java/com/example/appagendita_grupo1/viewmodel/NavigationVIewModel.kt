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
            is NavEvent.ToHome -> {
                val route = if (event.section != null) {
                    "${Routes.Home}?section=${event.section.name}"
                } else {
                    Routes.Home
                }
                navController.navigate(route) {
                    popUpTo(Routes.Login) { inclusive = true }
                }
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
            NavEvent.ToSettingsSecurity -> navController.navigate(Routes.SettingsSecurity)
            NavEvent.ToSettingsHelp -> navController.navigate(Routes.SettingsHelp)
            NavEvent.ToSettingsLanguage -> navController.navigate(Routes.SettingsLanguage)
            NavEvent.ToSettingsAbout -> navController.navigate(Routes.SettingsAbout)
            NavEvent.ToAccount -> navController.navigate(Routes.Account)
            NavEvent.ToAccountEdit -> navController.navigate(Routes.AccountEdit)
            NavEvent.ToAddTask -> navController.navigate(Routes.AddTask)
            NavEvent.ToAddNote -> navController.navigate(Routes.AddNote)
            NavEvent.ToAddTeam -> navController.navigate(Routes.AddTeam)
            NavEvent.ToAddEvent -> navController.navigate(Routes.AddEvent)
            NavEvent.ToEvents -> navController.navigate(Routes.Events)
            NavEvent.ToTeams -> navController.navigate(Routes.Teams)
            NavEvent.Logout -> navController.navigate(Routes.Login) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }
}
