package com.example.accesibilidadapp.data.location

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import javax.inject.Inject
import android.Manifest

class LocationProviderImpl @Inject constructor(
    private val client: FusedLocationProviderClient,
    private val application: Application
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        // 1. Verificamos si tenemos permiso antes de pedirle nada al GPS
        val hasPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return null

        // 2. Convertimos el Callback de Google en una Corrutina de Kotlin
        return suspendCancellableCoroutine { continuation ->
            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location: Location? ->
                // Si todo sale bien, retornamos la ubicación (puede ser null si el GPS está apagado)
                continuation.resume(location)
            }.addOnFailureListener {
                // Si hay un error de hardware o sistema, retornamos null
                continuation.resume(null)
            }
        }
    }
}