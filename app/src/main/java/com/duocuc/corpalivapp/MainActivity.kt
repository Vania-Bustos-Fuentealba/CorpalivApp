package com.duocuc.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duocuc.corpalivapp.navigation.NavigationEvent
import com.duocuc.corpalivapp.navigation.Screen
import com.duocuc.corpalivapp.ui.screens.HomeScreenAdaptive
import com.duocuc.corpalivapp.ui.screens.InstitucionScreen
import com.duocuc.corpalivapp.ui.screens.OportunidadesScreen
import com.duocuc.corpalivapp.ui.theme.CorpalivAppTheme
import com.duocuc.corpalivapp.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivAppTheme {
                CorpalivApp()
            }
        }
    }
}

/**
 * GUÍA 10 - PARTE 2 (Paso 7): NAVHOST Y NAVCONTROLLER
 *
 * Aquí viven el NavController y el NavHost. El flujo es:
 *   Pantalla -> MainViewModel (emite NavigationEvent) -> LaunchedEffect -> NavController
 *
 * Cada Scaffold de pantalla ya aplica sus propios márgenes del sistema
 * (edge-to-edge), por eso el NavHost ocupa toda la pantalla sin un Scaffold exterior.
 */
@Composable
private fun CorpalivApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    // Escucha los eventos de navegación emitidos por el ViewModel y los ejecuta.
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.buildRoute()) {
                        event.popUpToRoute?.let { destino ->
                            popUpTo(destino.route) { inclusive = event.inclusive }
                        }
                        launchSingleTop = event.singleTop
                        restoreState = true
                    }
                }

                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    // Menú lateral y barra inferior: vuelven a Inicio sin apilar pantallas repetidas.
    val navegarDesdeMenu: (Screen) -> Unit = remember(viewModel) {
        { destino ->
            viewModel.navigateTo(
                screen = destino,
                popUpToRoute = Screen.Home,
                singleTop = true
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.Home.route) {
            HomeScreenAdaptive(onNavigate = navegarDesdeMenu)
        }
        composable(route = Screen.Oportunidades.route) {
            OportunidadesScreen(
                onBack = { viewModel.navigateBack() },
                onNavigate = navegarDesdeMenu
            )
        }
        composable(route = Screen.Institucion.route) {
            InstitucionScreen(onNavigate = navegarDesdeMenu)
        }
    }
}
