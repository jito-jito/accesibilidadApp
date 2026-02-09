package com.example.accesibilidadapp.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesibilidadapp.ui.components.A11yTopBar
import com.example.accesibilidadapp.ui.components.AccessibleTextField
import com.example.accesibilidadapp.ui.viewmodels.RegisterViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel() // Accedemos al "cerebro"
) {
    // Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    // Escuchamos cuando isSuccess cambie a true
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess() // Ejecutamos la navegación
        }
    }

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

            Text(
                text = "Ingresa tus datos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .semantics { heading() } // TalkBack anunciará esto como "Encabezado"
            )


            AccessibleTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChanged(it) },
                label = "Nombre Completo",
                icon = Icons.Default.Person,
                description = "Icono de usuario"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Correo Electrónico",
                icon = Icons.Default.Email,
                description = "Icono de correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccessibleTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Crear Contraseña",
                icon = Icons.Default.Lock,
                description = "Icono de candado",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    focusManager.clearFocus() // Limpia el teclado/foco antes de la acción
                    android.util.Log.d("ACCESIBILIDAD", "¡Clic detectado en la UI!")
                    viewModel.registrarUsuario()
                },
                enabled = viewModel.isFormValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 12.dp)
            ) {
                Text("Registrarse")
            }

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

        }
    }
}
