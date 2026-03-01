package com.example.accesibilidadapp.ui.screens

import android.R.attr.contentDescription
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.accesibilidadapp.ui.viewmodels.VoiceViewModel

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VoiceTranscriptionScreen(
    viewModel: VoiceViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onFinish: (String) -> Unit
) {
    val permissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
    val state = viewModel.uiState

    // Iniciar automáticamente al obtener el permiso
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            viewModel.startTranscription()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (state.isListening) "Escuchando..." else "Micrófono pausado",
            style = MaterialTheme.typography.headlineMedium, // Ajustado para Material 3
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
        ) {
            Box(Modifier.padding(16.dp)) {
                Text(
                    text = if (state.textResult.isEmpty()) "El texto aparecerá aquí..." else state.textResult,
                )
            }
        }

        if (!permissionState.status.isGranted) {
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Permitir Micrófono")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de la Izquierda (Finalizar)
            Button(
                onClick = { onFinish(state.textResult) },
                enabled = state.textResult.isNotEmpty()
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
                    contentDescription = if (state.isListening) "Pausar" else "Reanudar",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}