package com.example.accesibilidadapp.ui.screens

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.accesibilidadapp.R
import com.example.accesibilidadapp.ui.components.A11yTopBar
import com.example.accesibilidadapp.ui.components.AccessibleTextField


@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterNavigate: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = { A11yTopBar(title = "Iniciar Sesión") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(200.dp),
                        tint = Color.Unspecified,

                        )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Accesibilidad App",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleTextField(
                label = "Correo Electrónico",
                icon = Icons.Default.Email,
                description = "Icono de sobre de carta"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleTextField(
                label = "Contraseña",
                icon = Icons.Default.Lock,
                description = "Icono de candado",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Ingresar",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onRegisterNavigate,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}