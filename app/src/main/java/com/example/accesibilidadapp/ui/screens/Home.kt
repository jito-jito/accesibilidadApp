package com.example.accesibilidadapp.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.accesibilidadapp.ui.components.HeroActionButton
import com.example.accesibilidadapp.ui.components.SecondaryActionButton

// Definición de colores de Alto Contraste para esta pantalla
val HighContrastYellow = Color(0xFFFFEB3B) // Fondo para leer
val HighContrastBlack = Color(0xFF121212) // Texto sobre amarillo
val HighContrastBlue = Color(0xFF2196F3) // Fondo para hablar
val HighContrastWhite = Color(0xFFFFFFFF) // Texto sobre azul
val AlertRed = Color(0xFFD32F2F) // Emergencia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToOCR: () -> Unit = {},
    onNavigateToTTS: () -> Unit = {},
    onNavigateToObjectRec: () -> Unit = {},
    onNavigateToLiveTranscribe: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Accesibilidad App", style = MaterialTheme.typography.headlineLarge)
                },
                actions = {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Abrir Configuración",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Scroll esencial para pantallas pequeñas o fuentes grandes
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // A11y: Encabezado semántico invisible para TalkBack para dar contexto
            Spacer(Modifier.semantics { heading(); contentDescription = "Acciones Principales" })

            // ============================================
            // ZONA HERO (PRIORIDAD 1) - Botones Gigantes
            // ============================================

            // Botón 1: Escáner OCR (importante para problemas visuales)
            HeroActionButton(
                text = "LEER TEXTO\nAHORA",
                icon = Icons.Default.Face,
                backgroundColor = HighContrastYellow,
                contentColor = HighContrastBlack,
                clickLabel = "Abrir cámara para leer texto",
                onClick = onNavigateToOCR
            )

            // Botón 2: Hablar por mí (importante para el habla)
            HeroActionButton(
                text = "HABLAR\nPOR MÍ",
                icon = Icons.Rounded.Face,
                backgroundColor = HighContrastBlue,
                contentColor = HighContrastWhite,
                clickLabel = "Escribir texto para que el celular lo diga",
                onClick = onNavigateToTTS
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.semantics { heading(); contentDescription = "Herramientas de Asistencia" })

            // ============================================
            // ZONA SECUNDARIA (PRIORIDAD 2)
            // ============================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SecondaryActionButton(
                    text = "¿Qué hay frente a mí?",
                    icon = Icons.Rounded.Search,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToObjectRec
                )
                SecondaryActionButton(
                    text = "Transcribir Voz",
                    icon = Icons.Rounded.Phone,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToLiveTranscribe
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================
            // ZONA DE UTILIDAD / EMERGENCIA
            // ============================================

            // Botón de contexto rápido
            OutlinedButton(
                onClick = { /* Lógica para decir ubicación */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("¿Dónde estoy?", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Emergencia (Rojo, muy visible)
            Button(
                onClick = onEmergencyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .semantics { role = Role.Button; contentDescription = "Alerta de Emergencia. Enviar ubicación a contacto." },
                colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(16.dp))
                Text(
                    "PEDIR AYUDA URGENTE",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


// ==========================================
// PREVIEW
// ==========================================
// A11y: Previsualización con fuente escalada para probar cómo se ve si el usuario aumenta el texto
@Preview(showBackground = true, widthDp = 412, heightDp = 915, fontScale = 1.2f)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}