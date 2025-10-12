package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appagendita_grupo1.R
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToSplash) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Ingresar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bienvenido",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Por favor, ingrese su dirección de correo electrónico y contraseña para iniciar sesión.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Ingrese su email") },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = accent,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        state.emailError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Ingrese su contraseña") },
            isError = state.passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = accent,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        state.passwordError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidó la contraseña?",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle forgot password */ },
            textAlign = TextAlign.End,
            color = accent
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (viewModel.validate()) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accent,
                contentColor = Color.White
            )
        ) {
            Text("Ingresar", fontSize = 16.sp)
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Apple Login",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { /* Handle Apple login */ }
            )
            Spacer(modifier = Modifier.size(32.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
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
                modifier = Modifier.clickable(onClick = onNavigateToRegistration),
                color = accent
            )
        }
    }
}
