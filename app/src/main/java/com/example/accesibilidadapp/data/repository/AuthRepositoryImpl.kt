package com.example.accesibilidadapp.data.repository

import com.example.accesibilidadapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(email: String, pass: String, name: String): Result<Unit> {
        return try {
            // 1. Crear el usuario en Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid ?: throw Exception("No se pudo obtener el ID de usuario")

            // 2. Crear el perfil en Firestore (Colección "users")
            val userMap = mapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )

            db.collection("users").document(uid).set(userMap).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override suspend fun signIn(email: String, pass: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override fun getCurrentUserEmail(): String? = auth.currentUser?.email

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    private fun mapFirebaseError(e: Exception): String {
        return when (e) {
            is FirebaseAuthUserCollisionException -> "Este correo electrónico ya está registrado."
            is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta o el formato del correo es inválido."
            is FirebaseAuthInvalidUserException -> "No existe ninguna cuenta con este correo."
            else -> "Error de conexión: Por favor, intenta de nuevo más tarde."
        }
    }
}