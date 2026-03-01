package com.example.accesibilidadapp.di

import android.app.Application
import com.example.accesibilidadapp.data.location.LocationProvider
import com.example.accesibilidadapp.data.location.LocationProviderImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application) =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun provideLocationProvider(
        client: FusedLocationProviderClient,
        app: Application
    ): LocationProvider = LocationProviderImpl(client, app)
}