package com.example.accesibilidadapp.domain.usecase

import com.example.accesibilidadapp.domain.repository.AuthRepository
import com.example.accesibilidadapp.domain.model.User
import com.example.accesibilidadapp.domain.model.esValido
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Ejecuta la lógica de negocio para registrar un nuevo usuario.
     * @param user Objeto con los datos del formulario.
     */
    suspend operator fun invoke(user: User): Result<Unit> {

        // 1. Regla de dominio: Validación local (Sin red)
        if (!user.esValido) {
            return Result.failure(Exception("Los datos no cumplen con el formato requerido"))
        }

        // 2. Ejecución del registro en Firebase
        // No necesitamos verificar manualmente 'existeUsuario' porque
        // Firebase Auth lo hace automáticamente al intentar crear la cuenta.
        return repository.signUp(
            email = user.email,
            pass = user.password,
            name = user.nombre
        )
    }
}