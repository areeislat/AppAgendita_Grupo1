package com.example.appagendita_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.navigation.Routes

class NavigationViewModel : ViewModel() {

    fun onNavEvent(navController: NavController, event: NavEvent) {
        when (event) {
            NavEvent.ToHome     -> navController.navigate(Routes.Home)
            NavEvent.ToDetail   -> navController.navigate(Routes.Detail)
            NavEvent.ToSettings -> navController.navigate(Routes.Settings)
            NavEvent.Back       -> navController.popBackStack()
        }
    }
}
