package com.example.accesibilidadapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.accesibilidadapp.ui.components.A11yTopBar
import com.example.accesibilidadapp.ui.components.AccessibleTextField


@Composable
fun RegisterScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            A11yTopBar(
                title = "Crear Cuenta",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // A11y: Marcamos esto como encabezado semántico para navegación rápida
            Text(
                text = "Ingresa tus datos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .semantics { heading() } // TalkBack anunciará esto como "Encabezado"
            )

            AccessibleTextField(label = "Nombre Completo", icon = Icons.Default.Person, description = "Icono de usuario")
            Spacer(modifier = Modifier.height(16.dp))
            AccessibleTextField(label = "Correo Electrónico", icon = Icons.Default.Email, description = "Icono de correo")
            Spacer(modifier = Modifier.height(16.dp))
            AccessibleTextField(label = "Crear Contraseña", icon = Icons.Default.Lock, description = "Icono de candado", isPassword = true)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Lógica de registro */ },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
