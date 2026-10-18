package com.duocuc.corpalivapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duocuc.corpalivapp.ui.screens.HomeScreenAdaptive
import com.duocuc.corpalivapp.ui.screens.InstitucionScreen
import com.duocuc.corpalivapp.ui.screens.InteresScreen
import com.duocuc.corpalivapp.ui.screens.ModoEspecialScreen
import com.duocuc.corpalivapp.ui.screens.OportunidadesScreen
import com.duocuc.corpalivapp.ui.screens.PerfilScreen
import com.duocuc.corpalivapp.ui.screens.RegistroScreen
import com.duocuc.corpalivapp.ui.screens.ResumenScreen
import com.duocuc.corpalivapp.ui.viewmodel.MainViewModel
import com.duocuc.corpalivapp.ui.viewmodel.PerfilViewModel
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * GUÍA 10 y 11: NAVHOST Y NAVCONTROLLER
 * Los ViewModels que se comparten entre pantallas
 * Así Registro y Resumen ven los mismos datos sin pasar argumentos por la ruta.
 */
@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val navController = rememberNavController()

    // Aborda los eventos de navegación emitidos por el ViewModel y los ejecuta.
    LaunchedEffect(Unit) {
        mainViewModel.navigationEvents.collect { event ->
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
    val navegarDesdeMenu: (Screen) -> Unit = remember(mainViewModel) {
        { destino ->
            mainViewModel.navigateTo(
                screen = destino,
                popUpToRoute = Screen.Home,
                singleTop = true
            )
        }
    }
    val volver: () -> Unit = remember(mainViewModel) { { mainViewModel.navigateBack() } }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = Screen.Home.route) {
            val fotoPerfil by perfilViewModel.imagenUri.collectAsStateWithLifecycle()
            HomeScreenAdaptive(
                onNavigate = navegarDesdeMenu,
                fotoPerfil = fotoPerfil
            )
        }
        composable(route = Screen.Oportunidades.route) {
            OportunidadesScreen(
                onBack = volver,
                onNavigate = navegarDesdeMenu
            )
        }
        composable(route = Screen.Institucion.route) {
            InstitucionScreen(onNavigate = navegarDesdeMenu)
        }

        // GUÍA 11: formulario -> interés -> resumen (mismo UsuarioViewModel)
        composable(route = Screen.Registro.route) {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onBack = volver,
                onRegistrado = { mainViewModel.navigateTo(Screen.Interes) }
            )
        }
        composable(route = Screen.Interes.route) {
            InteresScreen(
                viewModel = usuarioViewModel,
                onBack = volver,
                onContinuar = { mainViewModel.navigateTo(Screen.Resumen) }
            )
        }
        composable(route = Screen.Resumen.route) {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onBack = volver,
                onFinalizar = {
                    usuarioViewModel.reiniciar()
                    mainViewModel.navigateTo(
                        screen = Screen.Home,
                        popUpToRoute = Screen.Home,
                        singleTop = true
                    )
                }
            )
        }

        // GUÍA 12: estado persistente y animaciones
        composable(route = Screen.ModoEspecial.route) {
            ModoEspecialScreen(onBack = volver)
        }

        // GUÍA 13: cámara y galería (PerfilViewModel compartido)
        composable(route = Screen.Perfil.route) {
            PerfilScreen(
                viewModel = perfilViewModel,
                onBack = volver
            )
        }
    }
}
