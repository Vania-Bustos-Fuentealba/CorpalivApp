package com.duocuc.corpalivapp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.duocuc.corpalivapp.ui.components.AppBottomBar
import com.duocuc.corpalivapp.ui.screens.HomeScreenAdaptive
import com.duocuc.corpalivapp.ui.screens.InstitucionScreen
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

    // Ruta de la pantalla visible: sirve para resaltar la opción de la barra inferior
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    // El flujo de registro (formulario y resumen) cuenta como parte de "Oportunidades"
    val seccionActual: Screen? = when (rutaActual) {
        Screen.Home.route -> Screen.Home
        Screen.Oportunidades.route, Screen.Registro.route, Screen.Resumen.route -> Screen.Oportunidades
        Screen.Institucion.route -> Screen.Institucion
        else -> null
    }

    // G10: la barra inferior se declara una sola vez y se ve en todas las pantallas.
    // contentWindowInsets en 0: cada pantalla ya maneja su margen superior; aquí solo se reserva la barra.
    Scaffold(
        bottomBar = { AppBottomBar(selected = seccionActual, onNavigate = navegarDesdeMenu) },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                val fotoPerfil by perfilViewModel.imagenUri.collectAsStateWithLifecycle()
                HomeScreenAdaptive(
                    onNavigate = navegarDesdeMenu,
                    fotoPerfil = fotoPerfil
                )
            }
            composable(route = Screen.Oportunidades.route) {
                // Al elegir una oportunidad se guarda su título y se abre el formulario
                OportunidadesScreen(
                    onBack = volver,
                    onConectar = { oportunidad ->
                        usuarioViewModel.seleccionarOportunidad(oportunidad.titulo)
                        mainViewModel.navigateTo(Screen.Registro)
                    }
                )
            }
            composable(route = Screen.Institucion.route) {
                InstitucionScreen()
            }

            // GUÍA 11: formulario -> resumen (mismo UsuarioViewModel)
            composable(route = Screen.Registro.route) {
                RegistroScreen(
                    viewModel = usuarioViewModel,
                    onBack = volver,
                    onRegistrado = { mainViewModel.navigateTo(Screen.Resumen) }
                )
            }
            composable(route = Screen.Resumen.route) {
                ResumenScreen(
                    viewModel = usuarioViewModel,
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
}
