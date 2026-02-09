package com.example.accesibilidadapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.accesibilidadapp.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado para la pantalla de Login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    // Inyectamos o instanciamos el caso de uso del dominio
    private val loginUseCase = LoginUseCase()

    // Estado interno (Concepto de Encapsulamiento)
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado expuesto a la UI (Concepto 4: Lambdas se usarán en la UI para observar esto)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Validación de UI (Filtro previo antes de ir al dominio)
    val isFormValid: Boolean
        get() = uiState.value.email.contains("@") &&
                uiState.value.password.length >= 6

    // Actualización de campos
    fun onEmailChanged(nuevoEmail: String) {
        _uiState.update { it.copy(email = nuevoEmail) }
    }

    fun onPasswordChanged(nuevaPass: String) {
        _uiState.update { it.copy(password = nuevaPass) }
    }

    /**
     * Lógica de Inicio de Sesión
     * Conecta la UI con la validación de negocio del Dominio.
     */
    fun iniciarSesion() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        // Llamamos al caso de uso (Dominio)
        val resultado = loginUseCase(
            email = uiState.value.email,
            pass = uiState.value.password
        )

        resultado.onSuccess { usuario ->
            // Si el login es exitoso, actualizamos el estado
            android.util.Log.d("ACCESIBILIDAD", "Login exitoso para: ${usuario.nombre}")
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }.onFailure { error ->
            // Si el UseCase lanza una Excepción (Concepto 8), se captura aquí (Concepto 9)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: "Error desconocido"
                )
            }
        }
    }
}