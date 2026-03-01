package com.example.accesibilidadapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesibilidadapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado para la pantalla de Login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    // Inyectamos el caso de uso que ahora usa el repositorio de Firebase
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val isFormValid: Boolean
        get() = uiState.value.email.contains("@") &&
                uiState.value.password.length >= 6

    fun onEmailChanged(nuevoEmail: String) {
        _uiState.update { it.copy(email = nuevoEmail) }
    }

    fun onPasswordChanged(nuevaPass: String) {
        _uiState.update { it.copy(password = nuevaPass) }
    }

    /**
     * Lógica de Inicio de Sesión con Firebase
     */
    fun iniciarSesion() {
        // Iniciamos carga y limpiamos errores previos
        _uiState.update { it.copy(isLoading = true, error = null) }

        // Abrimos corrutina para la llamada asíncrona a Firebase
        viewModelScope.launch {
            val resultado = loginUseCase(
                email = uiState.value.email,
                pass = uiState.value.password
            )

            resultado.onSuccess {
                // Éxito: Firebase ya gestionó la sesión internamente
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                // Error: Capturamos el mensaje traducido
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Error al iniciar sesión"
                    )
                }
            }
        }
    }
}