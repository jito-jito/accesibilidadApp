package com.example.accesibilidadapp.utils

// Función de extensión para imprimir logs rápidamente (Concepto 6)
fun Any.logClick() {
    android.util.Log.d("DEBUG_APP", "Click detectado en: ${this::class.java.simpleName}")
}

// Función inline para ejecución segura (Concepto 3)
inline fun safeAction(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        android.util.Log.e("DEBUG_APP", "Error: ${e.message}")
    }
}