package com.example.appagendita_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.navigation.Routes
import com.example.appagendita_grupo1.ui.screens.DetailScreen
import com.example.appagendita_grupo1.ui.screens.HomeScreen
import com.example.appagendita_grupo1.ui.screens.SettingsScreen
import com.example.appagendita_grupo1.ui.screens.SplashScreen
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.ui.utils.calculateAppWidthSize
import com.example.appagendita_grupo1.viewmodel.NavigationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppAgendita_Grupo1Theme {
                //helper composable para tamaño de pantalla (recibe Activity)
                val appWidthSize = calculateAppWidthSize(this@MainActivity)

                // Nav + VM
                val navController = rememberNavController()
                val navVM: NavigationViewModel = viewModel()

                // Callback centralizado para navegar con eventos tipados
                val go: (NavEvent) -> Unit = remember(navController) {
                    { event -> navVM.onNavEvent(navController, event) }
                }

                // grafico de navegación
                NavHost(
                    navController = navController,
                    startDestination = Routes.Splash.route
                ) {
                    composable(Routes.Splash.route) {
                        SplashScreen(onContinue = {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Splash.route) {
                                    inclusive = true
                                }
                            }
                        })
                    }
                    composable(Routes.Home.route) {
                        HomeScreen(
                            appWidthSize = appWidthSize,
                            onNavigate = go
                        )
                    }
                    composable(Routes.Detail.route) {
                        DetailScreen(
                            onBack = { go(NavEvent.Back) },
                            onNavigate = go
                        )
                    }
                    composable(Routes.Settings.route) {
                        SettingsScreen(
                            onNavigate = go
                        )
                    }
                }
            }
        }
    }
}
