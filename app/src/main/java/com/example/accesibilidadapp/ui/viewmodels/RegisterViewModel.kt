package com.example.accesibilidadapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesibilidadapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository // Inyectamos la interfaz del repositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    val isFormValid: Boolean
        get() = uiState.value.nombre.isNotBlank() &&
                uiState.value.email.contains("@") &&
                uiState.value.password.length >= 6

    // lógica de filtrado de nombre
    fun onNombreChanged(nuevoNombre: String) {
        val nombreProcesado = nuevoNombre.filter { char ->
            if (char.isDigit()) return@filter false
            if (char == '@' || char == '#') return@filter false
            true
        }
        _uiState.update { it.copy(nombre = nombreProcesado) }
    }

    fun onEmailChanged(nuevoEmail: String) {
        _uiState.update { it.copy(email = nuevoEmail) }
    }

    fun onPasswordChanged(nuevaPass: String) {
        _uiState.update { it.copy(password = nuevaPass) }
    }

    /**
     * Lógica Real de Registro con Firebase
     */
    fun registrarUsuario() {
        // Iniciamos estado de carga y limpiamos errores previos
        _uiState.update { it.copy(isLoading = true, error = null) }

        // Como la llamada a Firebase es 'suspend', usamos viewModelScope
        viewModelScope.launch {
            val resultado = repository.signUp(
                email = uiState.value.email,
                pass = uiState.value.password,
                name = uiState.value.nombre
            )

            resultado.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Ocurrió un error inesperado"
                    )
                }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}