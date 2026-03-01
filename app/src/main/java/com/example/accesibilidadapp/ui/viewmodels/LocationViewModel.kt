package com.example.accesibilidadapp.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesibilidadapp.data.location.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.location.Geocoder
import java.util.Locale

sealed class LocationState {
    object Idle : LocationState()
    object Loading : LocationState()
    data class Success(val address: String) : LocationState()
    data class Error(val message: String) : LocationState()
}

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val application: Application
) : ViewModel() {

    var state by mutableStateOf<LocationState>(LocationState.Idle)
        private set

    fun fetchLocation() {
        viewModelScope.launch {
            state = LocationState.Loading
            val location = locationProvider.getCurrentLocation()

            if (location != null) {
                val address = getAddressFromCoords(location.latitude, location.longitude)
                state = LocationState.Success(address)
            } else {
                state = LocationState.Error("No se pudo obtener la ubicación. Revisa tu GPS y permisos.")
            }
        }
    }

    private fun getAddressFromCoords(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(application, Locale("es", "CL"))
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val addr = addresses?.firstOrNull()
            addr?.let { "${it.thoroughfare} ${it.subThoroughfare}, ${it.locality}" } ?: "Dirección no encontrada"
        } catch (e: Exception) {
            "Error al obtener dirección postal"
        }
    }
}