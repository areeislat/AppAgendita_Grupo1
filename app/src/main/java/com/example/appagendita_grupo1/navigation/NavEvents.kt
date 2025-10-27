package com.example.appagendita_grupo1.navigation

import com.example.appagendita_grupo1.ui.screens.home.HomeSection

sealed interface NavEvent {
    data object Back : NavEvent

    // Splash
    data object ToLogin : NavEvent

    // Login
    data class ToHome(val section: HomeSection? = null) : NavEvent
    data object ToRegistration : NavEvent
    data object BackToSplash : NavEvent

    // Registration
    data object ToLoginFromRegistration : NavEvent

    // Home
    data object ToDetail : NavEvent
    data object ToSettings : NavEvent
    data object ToSettingsSecurity : NavEvent
    data object ToSettingsHelp : NavEvent
    data object ToSettingsLanguage : NavEvent
    data object ToSettingsAbout : NavEvent
    data object ToAccount : NavEvent
    data object ToAccountEdit : NavEvent
    data object ToAddTask : NavEvent
    data object ToAddNote : NavEvent
    data object ToAddTeam : NavEvent
    data object ToAddEvent : NavEvent
    data object ToEvents : NavEvent
    data object ToTeams : NavEvent
    data object Logout : NavEvent
}
