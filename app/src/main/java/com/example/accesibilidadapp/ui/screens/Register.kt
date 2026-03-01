package com.example.accesibilidadapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // Importante: usar hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.accesibilidadapp.ui.components.A11yTopBar
import com.example.accesibilidadapp.ui.components.AccessibleTextField
import com.example.accesibilidadapp.ui.viewmodels.RegisterViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    // CAMBIO: Usamos hiltViewModel() para que Hilt inyecte el repositorio automáticamente
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
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
                    .semantics { heading() }
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

            // Mejoramos el feedback visual en el botón
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.registrarUsuario()
                },
                // Se deshabilita si el formulario es inválido O si está cargando
                enabled = viewModel.isFormValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    // Muestra un indicador de carga pequeño dentro del botón
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Registrarse")
                }
            }

            // Mostramos el error de forma más visible si existe
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}