package com.example.accesibilidadapp.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.accesibilidadapp.ui.viewmodels.VoiceViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoiceTranscriptionScreen(
    viewModel: VoiceViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onFinish: (String) -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val state = viewModel.uiState

    // Iniciar automáticamente al obtener el permiso
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            viewModel.startTranscription()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Transcripción de Voz"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (state.isListening) "Escuchando..." else "Micrófono pausado",
                style = MaterialTheme.typography.headlineMedium,
                color = if (state.isListening) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.semantics {
                    this.contentDescription = if (state.isListening) {
                        "Estado: Escuchando activamente"
                    } else {
                        "Estado: Pausado"
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Caja de transcripción en tiempo real
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(Modifier.padding(16.dp)) {
                    Text(
                        text = if (state.textResult.isEmpty()) "El texto aparecerá aquí..." else state.textResult,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (!permissionState.status.isGranted) {
                Button(
                    onClick = { permissionState.launchPermissionRequest() },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text("Permitir Micrófono")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de la Izquierda (Finalizar)
                Button(
                    onClick = { onFinish(state.textResult) },
                    enabled = state.textResult.isNotEmpty(),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Finalizar")
                }

                // Botón de la Derecha (Micrófono)
                FloatingActionButton(
                    onClick = { viewModel.toggleListening() },
                    containerColor = if (state.isListening) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = if (state.isListening) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = if (state.isListening) "Pausar micrófono" else "Reanudar micrófono",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}