package com.example.accesibilidadapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.accesibilidadapp.domain.repository.AuthRepository
import com.example.accesibilidadapp.ui.viewmodels.ProfileUiState
import com.example.accesibilidadapp.ui.viewmodels.ProfileViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileViewModel

    // Mocks de las dependencias principales
    private val authRepository: AuthRepository = mock(AuthRepository::class.java)
    private val firestore: FirebaseFirestore = mock(FirebaseFirestore::class.java)

    // Mocks para simular la estructura de Firebase Firestore
    private val mockCollection: CollectionReference = mock(CollectionReference::class.java)
    private val mockDocument: DocumentReference = mock(DocumentReference::class.java)
    private val mockTask: Task<DocumentSnapshot> = mock(Task::class.java) as Task<DocumentSnapshot>

    @Before
    fun setup() {
        // 1. Simular la sesión de usuario activa
        `when`(authRepository.getCurrentUserId()).thenReturn("user123")
        `when`(authRepository.getCurrentUserEmail()).thenReturn("test@test.com")

        // 2. Simular la cadena de llamadas: db.collection().document().get()
        `when`(firestore.collection(anyString())).thenReturn(mockCollection)
        `when`(mockCollection.document(anyString())).thenReturn(mockDocument)
        `when`(mockDocument.get()).thenReturn(mockTask)

        // 3.  Permitir el encadenamiento (listeners) de la Task
        `when`(mockTask.addOnSuccessListener(any())).thenReturn(mockTask)
        `when`(mockTask.addOnFailureListener(any())).thenReturn(mockTask)

        // 4. Inicializar el ViewModel
        viewModel = ProfileViewModel(authRepository, firestore)
    }

    @Test
    fun `cuando inicia el ViewModel, el estado inicial es Loading`() = runTest {
        // Obtenemos el valor actual del StateFlow
        val currentState = viewModel.uiState.value

        // Verificamos que sea de tipo Loading para cumplir con los requisitos de accesibilidad visual
        assertTrue("El estado debería ser Loading al inicio", currentState is ProfileUiState.Loading)
    }
}