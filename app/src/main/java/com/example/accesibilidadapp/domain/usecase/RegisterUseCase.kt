package com.example.accesibilidadapp.domain.usecase

import com.example.accesibilidadapp.data.repository.AuthRepository
import com.example.accesibilidadapp.domain.model.User
import com.example.accesibilidadapp.domain.model.esValido

class RegisterUseCase {

    operator fun invoke(user: User): Result<Unit> {

        // Regla de dominio 1: Validación básica
        if (!user.esValido) {
            return Result.failure(Exception("Los datos no cumplen con el formato requerido"))
        }

        // Regla de dominio 2: Integridad de datos (Usuario duplicado)
        if (AuthRepository.existeUsuario(user.email)) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        AuthRepository.guardarUsuario(user.nombre, user.email, user.password)
        return Result.success(Unit)
    }
}