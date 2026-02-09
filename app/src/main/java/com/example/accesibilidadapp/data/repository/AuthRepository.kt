package com.example.accesibilidadapp.data.repository

import com.example.accesibilidadapp.domain.model.User
import java.util.UUID

object AuthRepository {
    // Lista privada que actúa como nuestra base de datos en memoria
    private val usuariosRegistrados = mutableListOf<User>()

    // Bloque de inicialización para datos de prueba
    init {
        usuariosRegistrados.add(
            User(
                id = "default-id",
                nombre = "Usuario de Prueba",
                email = "test@mail.com",
                password = "password123"
            )
        )
        android.util.Log.d("DEBUG_APP", "AuthRepository: Usuario por defecto cargado (test@mail.com)")
    }
    /**
     * Concepto 2: Filter
     * Usamos filter para verificar si el correo ya existe en la lista.
     */
    fun existeUsuario(email: String): Boolean {
        return usuariosRegistrados.filter { it.email == email }.isNotEmpty()
    }

    /**
     * Agrega un nuevo usuario a la memoria
     */
    fun guardarUsuario(nombre: String, email: String, pass: String): User {
        val nuevoUsuario = User(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            email = email,
            password = pass
        )
        usuariosRegistrados.add(nuevoUsuario)
        return nuevoUsuario
    }

    /**
     * Permite obtener todos los usuarios
     */
    fun obtenerUsuarios(): List<User> = usuariosRegistrados.toList()
}