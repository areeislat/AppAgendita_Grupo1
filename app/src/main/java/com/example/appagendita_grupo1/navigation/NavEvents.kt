package com.example.appagendita_grupo1.navigation

sealed interface NavEvent {
    data object ToHome : NavEvent
    data object ToDetail : NavEvent
    data object ToSettings : NavEvent
    data object Back : NavEvent
}
