package com.example.appagendita_grupo1.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appagendita_grupo1.data.repository.UserRepository
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.utils.SessionManager
import com.example.appagendita_grupo1.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_debeMostrarCamposYBoton() {
        // 1. Mocks: Simulamos el Repositorio y SessionManager directamente
        val mockRepository = mockk<UserRepository>(relaxed = true)
        val mockSessionManager = mockk<SessionManager>(relaxed = true)

        // Configurar comportamiento del mock (para que no devuelva null ni falle el when)
        // Le decimos: "Si alguien llama a login, devuelve Error (o Success, da igual para este test visual)"
        coEvery { mockRepository.login(any(), any()) } returns UserRepository.AuthResult.Error("Simulated Error")

        // 2. ViewModel real con mocks inyectados
        val viewModel = LoginViewModel(mockRepository, mockSessionManager)

        // 3. Cargar UI
        composeTestRule.setContent {
            AppAgendita_Grupo1Theme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegistration = {},
                    onNavigateToSplash = {},
                    viewModel = viewModel
                )
            }
        }

        // 4. Verificaciones Visuales
        composeTestRule.onNodeWithText("Bienvenido de vuelta").assertIsDisplayed()

        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()
            .performTextInput("test@correo.com")

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()
            .performTextInput("123456")

        composeTestRule.onNodeWithText("Iniciar Sesión")
            .assertIsDisplayed()
            .performClick()
    }
}