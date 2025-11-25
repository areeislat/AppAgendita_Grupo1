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

// --- IMPORTACIONES DE ROOM Y RETROFIT ---
import com.example.appagendita_grupo1.data.local.database.AgendaVirtualDatabase
import com.example.appagendita_grupo1.data.remote.RetrofitClient
import com.example.appagendita_grupo1.data.repository.NoteRepository
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModel
import com.example.appagendita_grupo1.viewmodel.AddNoteViewModelFactory
import com.example.appagendita_grupo1.viewmodel.AddTaskViewModel
import com.example.appagendita_grupo1.viewmodel.NoteListViewModelFactory

// --- IMPORTACIONES DE USER ---
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
import com.example.appagendita_grupo1.viewmodel.RegistrationViewModel
import com.example.appagendita_grupo1.utils.SessionManager

// --- IMPORTACIONES DE HILT ---
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
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

                // Callback centralizado para navegar
                val go: (NavEvent) -> Unit = remember(navController) {
                    { event -> navVM.onNavEvent(navController, event) }
                }

                // --- DEPENDENCIAS GLOBALES (Aún necesarias para NoteRepository manual) ---
                val database = remember { AgendaVirtualDatabase.getInstance(applicationContext) }
                val sessionManager = remember { SessionManager.getInstance(applicationContext) }
                val apiService = remember { RetrofitClient.instance }
                val noteDao = remember { database.noteDao() }

                // --- NOTA: AddNoteViewModel aún usa Factory manual, lo mantenemos por ahora ---
                val noteRepository = remember {
                    NoteRepository(noteDao, apiService, sessionManager)
                }
                val addNoteViewModelFactory = remember(noteRepository) {
                    AddNoteViewModelFactory(noteRepository)
                }
                val noteListViewModelFactory = remember(noteRepository) {
                    NoteListViewModelFactory(noteRepository)
                }

                // --- ELIMINADO: Factories manuales de User (Login/Register) ---
                // Ya no las necesitamos porque usamos Hilt

                // GRAFICO DE NAVEGACIÓN
                NavHost(
                    navController = navController,
                    startDestination = if (sessionManager.isSessionValid()) Routes.Home else Routes.Splash
                ) {
                    composable(Routes.Splash) {
                        SplashScreen(onContinue = { go(NavEvent.ToLogin) })
                    }

                    // --- RUTA Login (MIGRADA A HILT) ---
                    composable(Routes.Login) {
                        // Usamos hiltViewModel() para inyección automática
                        val loginViewModel: LoginViewModel = hiltViewModel()

                        LoginScreen(
                            onLoginSuccess = { go(NavEvent.ToHome()) },
                            onNavigateToRegistration = { go(NavEvent.ToRegistration) },
                            onNavigateToSplash = { go(NavEvent.BackToSplash) },
                            viewModel = loginViewModel
                        )
                    }

                    // --- RUTA Registration (MIGRADA A HILT) ---
                    composable(Routes.Registration) {
                        // Usamos hiltViewModel() para inyección automática
                        val registrationViewModel: RegistrationViewModel = hiltViewModel()

                        RegistrationScreen(
                            onRegistrationSuccess = { go(NavEvent.ToLoginFromRegistration) },
                            onNavigateToLogin = { go(NavEvent.Back) },
                            viewModel = registrationViewModel
                        )
                    }

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

                    // ... (Resto de rutas: AccountEdit, Detail, Settings... se mantienen igual) ...
                    composable(Routes.AccountEdit) { EditAccountScreen(onBack = { go(NavEvent.Back) }, onSave = { _, _, _, _ -> go(NavEvent.Back) }) }
                    composable(Routes.Detail) { DetailScreen(onBack = { go(NavEvent.Back) }, onNavigate = go) }
                    composable(Routes.Settings) { SettingsScreen(onNavigate = go) }
                    composable(Routes.SettingsSecurity) { SecuritySettingsScreen(onNavigate = go) }
                    composable(Routes.SettingsHelp) { HelpSettingsScreen(onNavigate = go) }
                    composable(Routes.SettingsLanguage) { LanguageSettingsScreen(onNavigate = go) }
                    composable(Routes.SettingsAbout) { AboutSettingsScreen(onNavigate = go) }

                    // --- RUTA AddTask (YA ERA HILT) ---
                    composable(Routes.AddTask) {
                        val addTaskViewModel: AddTaskViewModel = hiltViewModel()
                        AddTaskScreen(
                            viewModel = addTaskViewModel,
                            onNavigateBack = { go(NavEvent.Back) }
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