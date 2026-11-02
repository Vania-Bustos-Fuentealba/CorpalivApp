package com.duocuc.corpalivapp.navigation // Declaración del paquete al que pertenece la clase de gestión de navegación principal

import androidx.compose.foundation.layout.WindowInsets // Importación para manejar los insets y márgenes del sistema
import androidx.compose.foundation.layout.consumeWindowInsets // Importación para consumir insets dentro de Jetpack Compose
import androidx.compose.foundation.layout.fillMaxSize // Modificador para que el contenedor ocupe todo el espacio disponible
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o el padding provisto por Scaffold
import androidx.compose.material3.Scaffold // Componente de la guía de diseño Material 3 para la estructura visual base de la app
import androidx.compose.runtime.Composable // Anotación para declarar la función como un componente Compose
import androidx.compose.runtime.LaunchedEffect // Efecto secundario que ejecuta corrutinas vinculadas al ciclo de vida
import androidx.compose.runtime.getValue // Importación del delegado 'getValue' para la lectura de estados de Compose
import androidx.compose.runtime.remember // Función para preservar y memorizar instancias a través de las recomposiciones
import androidx.compose.ui.Modifier // Modificador de UI para alterar el diseño, estilo y comportamiento de los composables
import androidx.compose.ui.unit.dp // Unidad de medida en densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Extensión para recolectar flujos de forma consciente del ciclo de vida
import androidx.lifecycle.viewmodel.compose.viewModel // Extensión para instanciar y obtener ViewModels en Jetpack Compose
import androidx.navigation.compose.NavHost // Contenedor que hospeda el grafo de navegación y gestiona el intercambio de pantallas
import androidx.navigation.compose.composable // Función de extensión para declarar cada ruta y su composable asociado
import androidx.navigation.compose.currentBackStackEntryAsState // Extensión para obtener la pantalla activa de la pila de navegación como un State
import androidx.navigation.compose.rememberNavController // Función que crea y recuerda el NavController durante recomposiciones
import com.duocuc.corpalivapp.data.model.RolUsuario // Importación de la enumeración de roles de usuario
import com.duocuc.corpalivapp.ui.components.AppBottomBar // Importación del componente de la barra de navegación inferior
import com.duocuc.corpalivapp.ui.screens.DashboardScreen // Pantalla del panel de administración/dashboard
import com.duocuc.corpalivapp.ui.screens.HomeScreenAdaptive // Pantalla principal de inicio con soporte adaptativo
import com.duocuc.corpalivapp.ui.screens.InstitucionScreen // Pantalla informativa de la institución
import com.duocuc.corpalivapp.ui.screens.LoginScreen // Pantalla de autenticación/inicio de sesión
import com.duocuc.corpalivapp.ui.screens.ModoEspecialScreen // Pantalla para el modo especial de visualización
import com.duocuc.corpalivapp.ui.screens.OportunidadesScreen // Pantalla que lista las oportunidades o programas de la ONG
import com.duocuc.corpalivapp.ui.screens.PerfilScreen // Pantalla con la información del perfil del usuario
import com.duocuc.corpalivapp.ui.screens.RegistroScreen // Pantalla del formulario de registro o aporte
import com.duocuc.corpalivapp.ui.screens.ResumenScreen // Pantalla de resumen y confirmación del registro
import com.duocuc.corpalivapp.ui.viewmodel.DashboardViewModel // ViewModel para gestionar la lógica del Dashboard
import com.duocuc.corpalivapp.ui.viewmodel.InicioViewModel // ViewModel que provee datos para la pantalla de inicio
import com.duocuc.corpalivapp.ui.viewmodel.MainViewModel // ViewModel central que administra los eventos globales de navegación
import com.duocuc.corpalivapp.ui.viewmodel.PerfilViewModel // ViewModel para la captura de fotos y gestión del perfil
import com.duocuc.corpalivapp.ui.viewmodel.SesionViewModel // ViewModel que administra la autenticación y estado de sesión
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel // ViewModel que almacena y gestiona los datos del formulario

/**
- GUÍA 10 y 11: NAVHOST Y NAVCONTROLLER

- Los ViewModels que se comparten entre pantallas

- Así Registro y Resumen ven los mismos datos sin pasar argumentos por la ruta.

 */
@Composable
fun AppNavigation( // Declaración del composable principal encargado de coordinar la navegación e inyección de ViewModels
    mainViewModel: MainViewModel = viewModel(), // ViewModel compartido para la gestión centralizada de eventos de navegación
    usuarioViewModel: UsuarioViewModel = viewModel(), // ViewModel compartido para los datos del formulario de registro y resumen
    perfilViewModel: PerfilViewModel = viewModel(), // ViewModel compartido para la foto y perfil de usuario
    dashboardViewModel: DashboardViewModel = viewModel(), // ViewModel compartido para administrar los registros persistidos
    inicioViewModel: InicioViewModel = viewModel(), // ViewModel para alimentar los datos dinamicos del Home
    sesionViewModel: SesionViewModel = viewModel() // ViewModel para coordinar el estado de autenticación de la app
) {
    val navController = rememberNavController() // Crea y recuerda el objeto NavController que dirige las transiciones

    // Cuenta con sesión iniciada (null = nadie ha ingresado)
    val sesion by sesionViewModel.usuario.collectAsStateWithLifecycle() // Observa el flujo de estado del usuario autenticado respetando el ciclo de vida

    // Aborda los eventos de navegación emitidos por el ViewModel y los ejecuta.
    LaunchedEffect(Unit) { // Ejecuta una corrutina de efecto lanzado una sola vez cuando el composable entra en la composición
        mainViewModel.navigationEvents.collect { event -> // Escucha de manera asíncrona cada evento de navegación emitido por MainViewModel
            when (event) { // Clasifica el tipo de evento de navegación a ejecutar
                is NavigationEvent.NavigateTo -> { // Maneja el evento de ir a una nueva pantalla
                    navController.navigate(event.route.buildRoute()) { // Ejecuta la transición hacia la ruta construida
                        event.popUpToRoute?.let { destino -> // Si se definió un punto de retorno, remueve pantallas hasta ese destino
                            popUpTo(destino.route) { inclusive = event.inclusive } // Limpia la pila hasta el destino (e inclusive si se indica)
                        }
                        launchSingleTop = event.singleTop // Evita duplicar pantallas si la ruta ya está en el tope de la pila
                        restoreState = true // Restaura el estado previo guardado de la pantalla al volver a ella
                    }
                }

                is NavigationEvent.PopBackStack -> navController.popBackStack() // Remueve la pantalla actual de la pila y vuelve a la anterior
                is NavigationEvent.NavigateUp -> navController.navigateUp() // Navega hacia arriba en la jerarquía de la app
            }
        }
    }

    // Menú lateral y barra inferior: vuelven a Inicio sin apilar pantallas repetidas.
    val navegarDesdeMenu: (Screen) -> Unit = remember(mainViewModel) { // Callback de navegación optimizado mediante 'remember'
        { destino ->
            mainViewModel.navigateTo( // Solicita la navegación al MainViewModel configurando popUpTo en la pantalla Home
                screen = destino,
                popUpToRoute = Screen.Home,
                singleTop = true
            )
        }
    }
    val volver: () -> Unit = remember(mainViewModel) { { mainViewModel.navigateBack() } } // Callback que ejecuta la acción de retroceso

    // Ruta de la pantalla visible: sirve para resaltar la opción de la barra inferior
    val entradaActual by navController.currentBackStackEntryAsState() // Recopila como estado la pantalla actual en la pila de navegación
    val rutaActual = entradaActual?.destination?.route // Obtiene la cadena de texto con la ruta del destino activo

    // El flujo de registro (formulario y resumen) cuenta como parte de "Oportunidades"
    val seccionActual: Screen? = when (rutaActual) { // Mapea la ruta actual a una sección principal para la barra inferior
        Screen.Home.route -> Screen.Home // Identifica la sección de Inicio
        Screen.Oportunidades.route, Screen.Registro.route, Screen.Resumen.route -> Screen.Oportunidades // Agrupa el flujo de registro bajo Oportunidades
        Screen.Institucion.route -> Screen.Institucion // Identifica la sección de Institución
        else -> null // Otras rutas no resaltan ninguna opción en la barra inferior
    }

    // G10: la barra inferior se declara una sola vez y se ve en todas las pantallas.
    // contentWindowInsets en 0: cada pantalla ya maneja su margen superior; aquí solo se reserva la barra.
    Scaffold( // Contenedor estructural básico para la pantalla con soporte para barras superiores e inferiores
        bottomBar = { AppBottomBar(selected = seccionActual, onNavigate = navegarDesdeMenu) }, // Inserta la barra de navegación inferior
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) // Define insets vacíos para dejar el control de márgenes a cada pantalla
    ) { innerPadding -> // Parámetro con el padding de reserva del espacio de la barra inferior
        NavHost( // Contenedor del grafo de navegación de la app
            navController = navController, // Vincula el NavController que gestiona el estado del grafo
            startDestination = Screen.Home.route, // Define la pantalla inicial por defecto del grafo (Home)
            modifier = Modifier
                .fillMaxSize() // Expande el NavHost a toda la superficie
                .padding(innerPadding) // Aplica el margen provisto por Scaffold para no superponerse a la barra inferior
                .consumeWindowInsets(innerPadding) // Marca los insets del Scaffold como consumidos
        ) {
            composable(route = Screen.Home.route) { // Define el nodo de la pantalla de Inicio
                val fotoPerfil by perfilViewModel.imagenUri.collectAsStateWithLifecycle() // Obtiene la foto de perfil activa
                val textoInicio by inicioViewModel.contenido.collectAsStateWithLifecycle() // Obtiene el contenido redactado para el Home
                HomeScreenAdaptive( // Invoca la pantalla adaptativa de inicio
                    onNavigate = navegarDesdeMenu, // Pasa la lambda de navegación
                    fotoPerfil = fotoPerfil, // Pasa la foto de perfil en estado
                    contenido = textoInicio // Pasa el texto informativo de inicio
                )
            }
            composable(route = Screen.Oportunidades.route) { // Define el nodo para la lista de oportunidades
                // Al elegir una oportunidad se guarda su título y se abre el formulario
                OportunidadesScreen(
                    onBack = volver, // Callback para regresar
                    onConectar = { oportunidad -> // Acción al hacer clic en conectar con una oportunidad
                        usuarioViewModel.seleccionarOportunidad(oportunidad.titulo) // Almacena el título en el ViewModel compartido
                        mainViewModel.navigateTo(Screen.Registro) // Navega hacia la pantalla de formulario de registro
                    }
                )
            }
            composable(route = Screen.Institucion.route) { // Define el nodo de la pantalla Institución
                InstitucionScreen(onBack = volver) // Renderiza la pantalla pasando la acción de volver
            }

            // GUÍA 11: formulario -> resumen (mismo UsuarioViewModel)
            composable(route = Screen.Registro.route) { // Define el nodo del formulario de registro
                RegistroScreen(
                    viewModel = usuarioViewModel, // Pasa el UsuarioViewModel compartido
                    onBack = volver, // Callback para volver atrás
                    onRegistrado = { // Acción que se dispara cuando el formulario es enviado
                        // Los aportes (personas que aportan y empresas) quedan guardados para el Dashboard
                        usuarioViewModel.construirRegistro()?.let { dashboardViewModel.guardar(it) } // Si es un aporte, guarda el registro en DataStore
                        mainViewModel.navigateTo(Screen.Resumen) // Navega a la pantalla de resumen
                    }
                )
            }
            composable(route = Screen.Resumen.route) { // Define el nodo de la pantalla de Resumen
                ResumenScreen(
                    viewModel = usuarioViewModel, // Usa el mismo UsuarioViewModel compartido para leer el estado
                    onFinalizar = { // Acción al presionar el botón de finalizar
                        usuarioViewModel.reiniciar() // Limpia el estado del formulario a sus valores por defecto
                        mainViewModel.navigateTo( // Redirige a la pantalla principal limpiando el backstack
                            screen = Screen.Home,
                            popUpToRoute = Screen.Home,
                            singleTop = true
                        )
                    }
                )
            }

            // G12: aportes registrados; solo con sesión de administrador (si no, pide iniciar sesión)
            composable(route = Screen.Dashboard.route) { // Define el nodo del Dashboard administrativo
                if (sesion?.rol == RolUsuario.ADMINISTRADOR) { // Verifica si hay una sesión activa con rol de ADMINISTRADOR
                    DashboardScreen( // Si es administrador, despliega el Dashboard con sus métricas y registros
                        viewModel = dashboardViewModel,
                        onBack = volver
                    )
                } else {
                    LoginScreen(viewModel = sesionViewModel, onBack = volver) // Si no es administrador, solicita el inicio de sesión
                }
            }

            // GUÍA 12: estado persistente y animaciones
            composable(route = Screen.ModoEspecial.route) { // Define el nodo para la pantalla de Modo Especial
                ModoEspecialScreen(onBack = volver) // Renderiza la pantalla con soporte para animaciones
            }

            // GUÍA 13: cámara y galería (PerfilViewModel compartido)
            // El perfil es de la cuenta: sin sesión se pide iniciarla y al ingresar se muestra el perfil
            composable(route = Screen.Perfil.route) { // Define el nodo para la pantalla de Perfil de usuario
                val cuenta = sesion // Obtiene la sesión activa

                if (cuenta == null) { // Evalúa si la cuenta es nula (sin usuario autenticado)
                    LoginScreen(viewModel = sesionViewModel, onBack = volver) // Redirige a pantalla de Login
                } else {
                    PerfilScreen( // Despliega la pantalla de Perfil si el usuario inició sesión
                        viewModel = perfilViewModel, // Pasa el ViewModel del Perfil
                        sesion = cuenta, // Pasa los datos de la sesión activa
                        onDashboard = { mainViewModel.navigateTo(Screen.Dashboard) }, // Navegación directa al Dashboard
                        onCerrarSesion = { // Acción ejecutada al presionar cerrar sesión
                            // La foto es de la cuenta: se quita al cerrar la sesión
                            perfilViewModel.quitarImagen() // Elimina la imagen de perfil capturada/cargada
                            sesionViewModel.cerrarSesion() // Finaliza la sesión en el ViewModel de sesión
                            mainViewModel.navigateTo( // Redirige al inicio y limpia el backstack
                                screen = Screen.Home,
                                popUpToRoute = Screen.Home,
                                singleTop = true
                            )
                        },
                        onBack = volver // Callback para regresar
                    )
                }
            }
        }
    }
}