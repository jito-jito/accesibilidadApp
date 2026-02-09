package com.example.accesibilidadapp.ui.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.accesibilidadapp.domain.model.User
import com.example.accesibilidadapp.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

// Definimos qué datos necesita la pantalla
data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val registerUseCase = RegisterUseCase()
    // Estado interno (privado)
    private val _uiState = MutableStateFlow(RegisterUiState())
    // Estado expuesto a la UI (público)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    val isFormValid: Boolean
        get() = uiState.value.nombre.isNotBlank() &&
                uiState.value.email.contains("@") &&
                uiState.value.password.length >= 6

    // Funciones para actualizar el estado cuando el usuario escribe
    fun onNombreChanged(nuevoNombre: String) {
        // Concepto 2: Usamos filter para crear una nueva cadena basada en una condición
        val nombreProcesado = nuevoNombre.filter { char ->

            // Concepto 5: Lambda con etiqueta (@filter)
            // Si el carácter es un número, usamos el retorno con etiqueta para descartarlo
            if (char.isDigit()) {
                return@filter false // Indica a 'filter' que ignore este caracter
            }

            // Si es un símbolo especial (ej. @), también lo saltamos
            if (char == '@' || char == '#') return@filter false

            true // Si pasa los filtros, se incluye en el resultado
        }

        _uiState.update { it.copy(nombre = nombreProcesado) }

    }

    fun onEmailChanged(nuevoEmail: String) {
        _uiState.update { it.copy(email = nuevoEmail) }
    }

    fun onPasswordChanged(nuevaPass: String) {
        _uiState.update { it.copy(password = nuevaPass) }
    }

    // Lógica Mock de Registro
    fun registrarUsuario() {
        _uiState.update { it.copy(isLoading = true) }


        val nuevoId = UUID.randomUUID().toString()

        // Creamos la entidad de dominio con el ID incluido
        val nuevoUsuario = User(
            id = nuevoId, // Se agrega el ID al constructor
            nombre = uiState.value.nombre,
            email = uiState.value.email,
            password = uiState.value.password
        )

        // Ejecutamos el caso de uso
        val resultado = registerUseCase(nuevoUsuario)

        resultado.onSuccess {
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }.onFailure { error ->
            _uiState.update { it.copy(isLoading = false, error = error.message) }
        }
    }
}