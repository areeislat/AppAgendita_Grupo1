// SplashScreen.kt

package com.example.appagendita_grupo1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.appagendita_grupo1.ui.theme.PollerOneRegular
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appagendita_grupo1.R
import com.example.appagendita_grupo1.ui.theme.AppAgendita_Grupo1Theme
import com.example.appagendita_grupo1.ui.theme.PoppinsFamily

// Colores que se usan
val PurpleBlue = Color(0xFF7B61FF) // Color principal para el botón y el título
val LightGrayText = Color(0xFF6D6D6D) // Color para el texto secundario
val DarkText = Color(0xFF1D1D1D) // Color para el texto principal

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    // Usamos una Columna para apilar la imagen y la tarjeta de contenido verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBlue), // Fondo para toda la pantalla
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Imagen Superior
        Image(
            painter = painterResource(id = R.drawable.onboarding_image), // Imagen de la parte superior
            contentDescription = "Onboarding Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // El 'weight' hace que la imagen ocupe el espacio superior disponible
            contentScale = ContentScale.Crop // Asegura que la imagen cubra el área sin deformarse
        )

        // 2. Tarjeta con el contenido inferior
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-30).dp), // Sube la tarjeta para que se solape con la imagen
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp,
                bottomStart = 32.dp, bottomEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título "Agendita"
                Text(
                    text = "Agendita",
                    color = PurpleBlue,
                    fontSize = 45.sp,
                    fontFamily = PollerOneRegular,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtítulo "Gestiona tus ideas"
                Text(
                    text = "Gestiona\n\ntu día a día",
                    color = DarkText,
                    fontSize = 38.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Texto descriptivo
                Text(
                    text = "Organiza y planea tu vida con este diario personal.",
                    color = LightGrayText,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón "Comenzar"
                Button(
                    onClick = onContinue, // La acción que se ejecuta al pulsar
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleBlue)
                ) {
                    Text(
                        text = "Comenzar",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Preview para ver el resultado en Android Studio
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AppAgendita_Grupo1Theme { // Usa tu tema para la previsualización
        SplashScreen(onContinue = {})
    }
}
