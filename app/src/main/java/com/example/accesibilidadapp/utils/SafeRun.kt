package com.example.accesibilidadapp.utils

/**
 * Concepto 3: Funciones inline para optimizar el rendimiento.
 * Concepto 9: Uso de bloques Try/Catch para manejo de errores.
 */
inline fun <T> safeRun(block: () -> T): Result<T> {
    return try {
        // Ejecuta el bloque de código pasado como parámetro
        Result.success(block())
    } catch (e: Exception) {
        // Captura cualquier excepción para evitar que la app se detenga
        Result.failure(e)
    }
}