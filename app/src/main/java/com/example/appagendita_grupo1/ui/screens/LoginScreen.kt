package com.example.appagendita_grupo1.ui.screens

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.ui.theme.AppTypography
import com.example.appagendita_grupo1.ui.theme.PoppinsFamily
import com.example.appagendita_grupo1.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    onNavigateToSplash: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.state
    val accent = colorResource(id = R.color.button_purple)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val sharedPrefs = remember(context) {
        context.applicationContext.getSharedPreferences(SESSION_PREFS_NAME, Context.MODE_PRIVATE)
    }
    var localEmailError by remember { mutableStateOf<String?>(null) }
    var localPasswordError by remember { mutableStateOf<String?>(null) }
    var hasRedirected by remember { mutableStateOf(false) }
    val onLoginSuccessState = rememberUpdatedState(onLoginSuccess)

    // Si la sesión ya se encontraba activa, navegamos automáticamente al Home.
    LaunchedEffect(sharedPrefs) {
        if (sharedPrefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            hasRedirected = true
            onLoginSuccessState.value()
        }
    }

    val emailErrorMessage = state.emailError ?: localEmailError
    val passwordErrorMessage = state.passwordError ?: localPasswordError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 48.dp,
                bottom = 16.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onNavigateToSplash,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Ingresar",
                style = AppTypography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bienvenido",
            fontSize = 24.sp,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Por favor, ingrese su dirección de correo electrónico y contraseña para iniciar sesión.",
            fontSize = 14.sp,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = {
                viewModel.onEmailChange(it)
                localEmailError = null
            },
            label = { Text("Ingrese su email") },
            isError = emailErrorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                cursorColor = Color.Black,
                focusedIndicatorColor = accent,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        emailErrorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = {
                viewModel.onPasswordChange(it)
                localPasswordError = null
            },
            label = { Text("Ingrese su contraseña") },
            isError = passwordErrorMessage != null,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray,
                cursorColor = Color.Black,
                focusedIndicatorColor = accent,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        passwordErrorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidó la contraseña?",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle forgot password */ },
            textAlign = TextAlign.End,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium,
            color = accent
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                if (state.isLoading || hasRedirected) return@Button

                val email = state.email.trim()
                val password = state.password

                var hasError = false
                localEmailError = when {
                    email.isBlank() -> {
                        hasError = true
                        "Por favor, ingresa tu email"
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        hasError = true
                        "Por favor, ingresa un email válido"
                    }

                    else -> null
                }

                localPasswordError = when {
                    password.isBlank() -> {
                        hasError = true
                        "Por favor, ingresa tu contraseña"
                    }

                    else -> null
                }

                if (!hasError) {
                    viewModel.onEmailChange(email)
                    viewModel.login {
                        sharedPrefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
                        hasRedirected = true
                        onLoginSuccessState.value()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accent,
                contentColor = Color.White
            ),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Ingresar", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Ingresar con...",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_apple),
                contentDescription = "Apple Login",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable { /* Handle Apple login */ }
            )
            Spacer(modifier = Modifier.size(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_google),
                contentDescription = "Google Login",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { /* Handle Google login */ }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("¿Aún no te registras? ")
            Text(
                text = "Registrarse",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onNavigateToRegistration),
                color = accent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, onNavigateToRegistration = {}, onNavigateToSplash = {})
}

private const val SESSION_PREFS_NAME = "session_prefs"
private const val KEY_IS_LOGGED_IN = "isLoggedIn"
