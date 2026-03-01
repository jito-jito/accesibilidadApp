package com.example.accesibilidadapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accesibilidadapp.data.tts.TtsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// TtsViewModel.kt
@HiltViewModel
class TtsViewModel @Inject constructor(
    private val ttsManager: TtsManager
) : ViewModel() {

    var textState by mutableStateOf("")
    var pitch by mutableStateOf(1.0f)
    var speed by mutableStateOf(1.0f)

    fun onTextChange(newValue: String) { textState = newValue }
    fun onPitchChange(newValue: Float) { pitch = newValue }
    fun onSpeedChange(newValue: Float) { speed = newValue }

    fun speak() {
        if (textState.isNotBlank()) {
            ttsManager.speak(textState, pitch, speed)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.stop() // Detiene el habla al cerrar el ViewModel
    }
}