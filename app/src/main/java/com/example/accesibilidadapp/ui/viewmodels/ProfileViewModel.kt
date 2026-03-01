package com.example.accesibilidadapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.accesibilidadapp.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = authRepository.getCurrentUserId()
        val email = authRepository.getCurrentUserEmail()

        if (uid == null) {
            _uiState.value = ProfileUiState.Error("No hay sesión activa")
            return
        }

        // Consultamos Firestore usando el UID del usuario autenticado
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "Usuario"
                _uiState.value = ProfileUiState.Success(name, email ?: "")
            }
            .addOnFailureListener {
                _uiState.value = ProfileUiState.Error("Error al conectar con el servidor")
            }
    }

    fun logout() {
        authRepository.signOut()
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val name: String, val email: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}