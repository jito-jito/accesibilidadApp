package com.example.accesibilidadapp.data.location

import android.location.Location

interface LocationProvider {
    suspend fun getCurrentLocation(): Location?
}