package com.example.accesibilidadapp.domain.usecase

import com.example.accesibilidadapp.data.repository.AuthRepository
import com.example.accesibilidadapp.domain.model.User
import com.example.accesibilidadapp.utils.safeRun

class LoginUseCase {
    // Concepto 1: Podríamos usar una función de orden superior si quisiéramos
    // aplicar una regla extra de logeo (ej. verificar si la cuenta está bloqueada).
    operator fun invoke(email: String, pass: String): Result<User> {
        return safeRun { // Concepto 3 (Inline) y Concepto 9 (Try/Catch)

            // Concepto 2: Usamos filter (u otra función de colección)
            // dentro del repositorio para encontrar al usuario.
            val usuarioEncontrado = AuthRepository.obtenerUsuarios().find {
                it.email == email && it.password == pass
            }

            // Concepto 8: Lanzamos una excepción si las credenciales no coinciden.
            if (usuarioEncontrado == null) {
                throw Exception("Correo o contraseña incorrectos")
            }

            usuarioEncontrado
        }
    }
}