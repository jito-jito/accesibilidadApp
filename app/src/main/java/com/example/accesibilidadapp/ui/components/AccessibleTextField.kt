package com.example.accesibilidadapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun AccessibleTextField(
    value: String,                    // Recibe el texto desde afuera
    onValueChange: (String) -> Unit,   // Notifica cuando el usuario escribe
    label: String,
    icon: ImageVector,
    description: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier      // Siempre permite pasar un Modifier de afuera
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange, // Delegamos el cambio
        label = { Text(label, style = MaterialTheme.typography.bodyLarge) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = description, // A11y: Mejor usar la descripción aquí
                modifier = Modifier.size(28.dp)
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                // TalkBack leerá el label automáticamente,
                // pero esto añade contexto extra si es necesario.
                contentDescription = "Ingresa tu $label"
            },
        textStyle = MaterialTheme.typography.titleMedium,
        singleLine = true
    )
}