package com.example.accesibilidadapp.domain.repository

interface AuthRepository {
    suspend fun signUp(email: String, pass: String, name: String): Result<Unit>
    suspend fun signIn(email: String, pass: String): Result<Unit>
}