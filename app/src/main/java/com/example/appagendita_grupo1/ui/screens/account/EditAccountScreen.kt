package com.example.appagendita_grupo1.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.AppTypography
import com.example.appagendita_grupo1.ui.theme.Bg
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    initialName: String = "Alvart Ainstain",
    initialEmail: String = "alvart.ainstain@gmail.com",
    initialUsername: String = "@alvart.ainstain",
    initialPhone: String = "+56 9 1234 6789",
    onBack: () -> Unit = {},
    onSave: (name: String, email: String, username: String, phone: String) -> Unit = { _, _, _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var username by remember { mutableStateOf(initialUsername) }
    var phone by remember { mutableStateOf(initialPhone) }

    Scaffold(
        containerColor = Bg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Perfil",
                        style = AppTypography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onSave(name, email, username, phone) }) {
                        Text(text = "Guardar", style = AppTypography.labelLarge, color = PurplePrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = NavyText,
                    navigationIconContentColor = NavyText,
                    actionIconContentColor = PurplePrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(140.dp),
                    color = Color(0xFFE9E7FF),
                    shadowElevation = 4.dp,
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = PurplePrimary,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .offset(x = (-8).dp, y = (-8).dp),
                    color = PurplePrimary,
                    shadowElevation = 6.dp,
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Cambiar foto",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            EditableField(label = "Nombre", value = name, onValueChange = { name = it })
            EditableField(label = "Email", value = email, onValueChange = { email = it })
            EditableField(label = "Username", value = username, onValueChange = { username = it })
            EditableField(label = "Número", value = phone, onValueChange = { phone = it })

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, style = AppTypography.bodyMedium, color = NavyText)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(22.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = PurplePrimary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditAccountScreenPreview() {
    EditAccountScreen()
}
