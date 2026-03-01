package com.example.accesibilidadapp

import com.example.accesibilidadapp.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AuthRepositoryTest {

    private val firebaseAuth: FirebaseAuth = mock(FirebaseAuth::class.java)
    private val db: FirebaseFirestore = mock(FirebaseFirestore::class.java)
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setup() {
        authRepository = AuthRepositoryImpl(firebaseAuth, db)
    }

    @Test
    fun `signIn debe devolver falla si las credenciales son invalidas`() = runTest {
        // 1. Creamos la excepción real que queremos simular
        val exception = com.google.firebase.auth.FirebaseAuthInvalidCredentialsException("error", "desc")

        // 2. IMPORTANTE: Usamos Tasks.forException para crear una Task que ya está COMPLETADA con error.
        // Esto permite que el .await() en tu AuthRepositoryImpl avance sin quedarse bloqueado.
        val failedTask = com.google.android.gms.tasks.Tasks.forException<com.google.firebase.auth.AuthResult>(exception)

        // 3. Configuramos el mock de FirebaseAuth para que devuelva esta Task ya fallida
        `when`(firebaseAuth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(failedTask)

        // 4. Ejecutamos el método del repositorio
        val result = authRepository.signIn("email@error.com", "123456")

        // 5. Verificamos que el resultado sea Failure
        // Asegúrate de importar: org.junit.Assert.assertTrue
        assertTrue("El resultado debería ser una falla", result.isFailure)


    }
}