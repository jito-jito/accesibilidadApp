package com.example.accesibilidadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.accesibilidadapp.ui.screens.HomeScreen
import com.example.accesibilidadapp.ui.screens.LoginScreen
import com.example.accesibilidadapp.ui.screens.RegisterScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.accesibilidadapp.ui.screens.LocationDetailScreen
import com.example.accesibilidadapp.ui.screens.VoiceTranscriptionScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    // 1. Crear el controlador de navegación
    val navController = rememberNavController()

    // 2. Definir el NavHost
    NavHost(
        navController = navController,
        startDestination = "login" // La pantalla inicial
    ) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterNavigate = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            RegisterScreen(
                onBackClick = {
                    navController.navigate("login")
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToLiveTranscribe = {
                    navController.navigate("voice_transcription")
                },
                onLocationDetailsClick = {
                    navController.navigate("location_detail")
                }
            )
        }

        composable("voice_transcription") {
            VoiceTranscriptionScreen(
                onBack = { navController.popBackStack() },
                onFinish = { result ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("transcribed_text", result)
                    navController.popBackStack()
                }
            )
        }

        composable("location_detail") {
            LocationDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}