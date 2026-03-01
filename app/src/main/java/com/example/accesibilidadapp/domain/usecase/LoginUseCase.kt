package com.example.accesibilidadapp.domain.usecase

import com.example.accesibilidadapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Realiza el inicio de sesión.
     * Ahora es una función 'suspend' porque la red es asíncrona.
     */
    suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        // Validaciones previas de dominio antes de ir al repositorio
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(Exception("Los campos no pueden estar vacíos"))
        }

        if (!email.contains("@")) {
            return Result.failure(Exception("Formato de correo inválido"))
        }

        // Llamamos al repositorio que ahora gestiona Firebase
        return repository.signIn(email, pass)
    }
}