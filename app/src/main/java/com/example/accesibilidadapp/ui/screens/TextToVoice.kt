package com.example.accesibilidadapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.accesibilidadapp.ui.viewmodels.TtsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToVoiceScreen(
    onBack: () -> Unit,
    viewModel: TtsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Hablar por mí")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver atrás"
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
                .padding(paddingValues) // Importante: respeta el espacio de la TopBar
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = viewModel.textState,
                onValueChange = { viewModel.onTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                label = { Text("Escribe tu mensaje") },
                placeholder = { Text("¿Qué quieres que diga el teléfono?") },
                shape = RoundedCornerShape(12.dp)
            )

            // Controles de configuración de voz
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TtsSlider(
                        label = "Tono (Pitch)",
                        value = viewModel.pitch,
                        onValueChange = { viewModel.onPitchChange(it) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TtsSlider(
                        label = "Velocidad",
                        value = viewModel.speed,
                        onValueChange = { viewModel.onSpeedChange(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón principal de acción
            Button(
                onClick = { viewModel.speak() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null, // Ya incluido en el texto del botón
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Reproducir voz",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun TtsSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text(
            text = "$label: ${"%.1f".format(value)}",
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.5f..2.0f
        )
    }
}