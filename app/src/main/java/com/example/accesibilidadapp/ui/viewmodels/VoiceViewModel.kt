package com.example.accesibilidadapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accesibilidadapp.data.speech.SpeechToTextManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val sttManager: SpeechToTextManager
) : ViewModel() {

    var uiState by mutableStateOf(VoiceUiState())
        private set

    fun startTranscription() {
        sttManager.startListening(
            onResults = { text -> uiState = uiState.copy(textResult = text) },
            onError = { error -> uiState = uiState.copy(error = error, isListening = false) },
            onStateChange = { isListening -> uiState = uiState.copy(isListening = isListening) }
        )
    }

    fun stopTranscription() {
        sttManager.stopListening()
        uiState = uiState.copy(isListening = false)
    }

    override fun onCleared() {
        super.onCleared()
        sttManager.destroy()
    }

    fun toggleListening() {
        if (uiState.isListening) {
            sttManager.stopListening()
            uiState = uiState.copy(isListening = false)
        } else {
            startTranscription()
        }
    }
}

data class VoiceUiState(
    val textResult: String = "",
    val isListening: Boolean = false,
    val error: String? = null
)