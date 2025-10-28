package com.example.appagendita_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appagendita_grupo1.navigation.NavEvent
import com.example.appagendita_grupo1.navigation.Routes
import com.example.appagendita_grupo1.ui.screens.AboutSettingsScreen
import com.example.appagendita_grupo1.ui.screens.HelpSettingsScreen
import com.example.appagendita_grupo1.ui.screens.LanguageSettingsScreen
import com.example.appagendita_grupo1.ui.screens.SecuritySettingsScreen
import com.example.appagendita_grupo1.ui.screens.SettingsScreen
import com.example.appagendita_grupo1.ui.screens.SplashScreen
import com.example.appagendita_grupo1.ui.screens.AddEventScreen
import com.example.appagendita_grupo1.ui.screens.AddNoteScreen
import com.example.appagendita_grupo1.ui.screens.AddTaskScreen
import com.example.appagendita_grupo1.ui.screens.AddTeamScreen
import com.example.appagendita_grupo1.ui.screens.DetailScreen
import com.example.appagendita_grupo1.ui.screens.LoginScreen
import com.example.appagendita_grupo1.ui.screens.RegistrationScreen
import com.example.appagendita_grupo1.ui.screens.account.AccountScreen
import com.example.appagendita_grupo1.ui.screens.account.EditAccountScreen
import com.example.appagendita_grupo1.ui.screens.home.HomeScreen
import com.example.appagendita_grupo1.ui.screens.home.HomeSection
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.viewmodel.NavigationViewModel

// --- IMPORTACIONES DE ROOM (NOTAS) ---
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModel
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModelFactory
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory

// --- INICIO DE CAMBIOS: IMPORTACIONES DE USER ---
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
import com.example.appagendita_grupo1.viewmodel.LoginViewModelFactory
import com.example.appagendita_grupo1.viewmodel.RegistrationViewModel
import com.example.appagendita_grupo1.viewmodel.RegistrationViewModelFactory
import com.example.appagendita_grupo1.utils.SessionManager
// --- FIN DE CAMBIOS: IMPORTACIONES DE USER ---


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppAgendita_Grupo1Theme {
                val windowSize = calculateWindowSizeClass(this@MainActivity)

                // Nav + VM
                val navController = rememberNavController()
                val navVM: NavigationViewModel = viewModel()

                // Callback centralizado para navegar con eventos tipados
                val go: (NavEvent) -> Unit = remember(navController) {
                    { event -> navVM.onNavEvent(navController, event) }
                }

                // --- CREACIÓN DE DEPENDENCIAS (ROOM) ---

                // Instancia de la Base de Datos
                val database = remember { AgendaVirtualDatabase.getInstance(applicationContext) }

                // Instancia de SessionManager
                val sessionManager = remember { SessionManager.getInstance(applicationContext) }

                // --- Dependencias de Notas ---
                val noteDao = remember { database.noteDao() }
                val noteRepository = remember { NoteRepository(noteDao, sessionManager) }
                val addNoteViewModelFactory = remember(noteRepository) {
                    AddNoteViewModelFactory(noteRepository)
                }
                val noteListViewModelFactory = remember(noteRepository) {
                    NoteListViewModelFactory(noteRepository)
                }

                // --- INICIO DE CAMBIOS: DEPENDENCIAS DE USER ---
                val userDao = remember { database.userDao() }
                val userRepository = remember { UserRepository(userDao) }

                val loginViewModelFactory = remember(userRepository, sessionManager) {
                    LoginViewModelFactory(userRepository, sessionManager)
                }
                val registrationViewModelFactory = remember(userRepository) {
                    RegistrationViewModelFactory(userRepository)
                }
                // --- FIN DE CAMBIOS: DEPENDENCIAS DE USER ---


                // grafico de navegación
                NavHost(
                    navController = navController,
                    startDestination = if (sessionManager.isSessionValid()) Routes.Home else Routes.Splash
                ) {
                    composable(Routes.Splash) {
                        SplashScreen(onContinue = { go(NavEvent.ToLogin) })
                    }

                    // --- INICIO DE CAMBIOS: RUTA Login ---
                    composable(Routes.Login) {
                        val loginViewModel: LoginViewModel = viewModel(
                            factory = loginViewModelFactory
                        )
                        LoginScreen(
                            onLoginSuccess = { go(NavEvent.ToHome()) },
                            onNavigateToRegistration = { go(NavEvent.ToRegistration) },
                            onNavigateToSplash = { go(NavEvent.BackToSplash) },
                            viewModel = loginViewModel // <-- Pasamos el ViewModel
                        )
                    }
                    // --- FIN DE CAMBIOS: RUTA Login ---

                    // --- INICIO DE CAMBIOS: RUTA Registration ---
                    composable(Routes.Registration) {
                        val registrationViewModel: RegistrationViewModel = viewModel(
                            factory = registrationViewModelFactory
                        )
                        RegistrationScreen(
                            onRegistrationSuccess = { go(NavEvent.ToLoginFromRegistration) },
                            onNavigateToLogin = { go(NavEvent.Back) },
                            viewModel = registrationViewModel // <-- Pasamos el ViewModel
                        )
                    }
                    // --- FIN DE CAMBIOS: RUTA Registration ---

                    composable(
                        route = "${Routes.Home}?section={section}",
                        arguments = listOf(navArgument("section") {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { backStackEntry ->
                        val sectionName = backStackEntry.arguments?.getString("section")
                        val section = sectionName?.let { HomeSection.valueOf(it) }
                        HomeScreen(
                            windowSize = windowSize,
                            onNavigate = go,
                            section = section,
                            noteListViewModelFactory = noteListViewModelFactory
                        )
                    }
                    composable(Routes.Account) {
                        AccountScreen(
                            onEditProfile = { go(NavEvent.ToAccountEdit) },
                            onOpenEvents = { go(NavEvent.ToHome(HomeSection.MonthlyNotes)) },
                            onOpenTeams = { go(NavEvent.ToHome(HomeSection.Events)) },
                            onOpenSettings = { go(NavEvent.ToSettings) },
                            onOpenTasks = { go(NavEvent.ToHome(HomeSection.TodayTasks)) },
                            onNavigateHome = { go(NavEvent.ToHome()) },
                            onAddTask = { go(NavEvent.ToAddTask) },
                            onAddNote = { go(NavEvent.ToAddNote) },
                            onAddTeam = { go(NavEvent.ToAddTeam) },
                            onAddEvent = { go(NavEvent.ToAddEvent) },
                            onLogout = {
                                sessionManager.clearSession()
                                go(NavEvent.BackToSplash)
                                go(NavEvent.ToLogin)
                            }
                        )
                    }
                    composable(Routes.AccountEdit) {
                        EditAccountScreen(
                            onBack = { go(NavEvent.Back) },
                            onSave = { _, _, _, _ -> go(NavEvent.Back) }
                        )
                    }
                    composable(Routes.Detail) {
                        DetailScreen(
                            onBack = { go(NavEvent.Back) },
                            onNavigate = go
                        )
                    }
                    composable(Routes.Settings) {
                        SettingsScreen(
                            onNavigate = go
                        )
                    }
                    composable(Routes.SettingsSecurity) {
                        SecuritySettingsScreen(onNavigate = go)
                    }
                    composable(Routes.SettingsHelp) {
                        HelpSettingsScreen(onNavigate = go)
                    }
                    composable(Routes.SettingsLanguage) {
                        LanguageSettingsScreen(onNavigate = go)
                    }
                    composable(Routes.SettingsAbout) {
                        AboutSettingsScreen(onNavigate = go)
                    }
                    composable(Routes.AddTask) {
                        AddTaskScreen(
                            onBack = { go(NavEvent.Back) }
                        )
                    }
                    composable(Routes.AddNote) {
                        val addNoteViewModel: AddNoteViewModel = viewModel(
                            factory = addNoteViewModelFactory
                        )
                        AddNoteScreen(
                            onBack = { go(NavEvent.Back) },
                            onNoteSaved = { go(NavEvent.Back) },
                            viewModel = addNoteViewModel
                        )
                    }
                    composable(Routes.AddTeam) {
                        AddTeamScreen(
                            onBack = { go(NavEvent.Back) },
                            onTeamSaved = { go(NavEvent.Back) }
                        )
                    }
                    composable(Routes.AddEvent) {
                        AddEventScreen(
                            onBack = { go(NavEvent.Back) },
                            onEventSaved = { go(NavEvent.Back) }
                        )
                    }
                }
            }
        }
    }
}