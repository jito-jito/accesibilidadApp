package com.example.accesibilidadapp.domain.model

data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val password: String // parámetro de prueba
)

val User.esValido: Boolean
    get() = email.contains("@") && password.length >= 6 && nombre.isNotBlank()