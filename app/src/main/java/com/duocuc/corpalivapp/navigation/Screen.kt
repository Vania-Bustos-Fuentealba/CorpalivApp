package com.duocuc.corpalivapp.navigation // Declaración del paquete al que pertenece la clase de definición de rutas de navegación

/**
- G10: RUTAS TIPO-SAFE CON SEALED CLASS

- Define en un único lugar todas las rutas de la app.

 */
sealed class Screen(val route: String) { // Declaración de la clase sellada base para representar las rutas de la app con su identificador String

    // Rutas simples (sin argumentos).

    /** Pantalla de inicio (adaptativa, con TopAppBar y menú lateral). */
    data object Home : Screen("home_page") // Ruta para la pantalla principal de inicio ("home_page")

    /** Listado de oportunidades y proyectos RSE (con TopAppBar y NavigationBar). */
    data object Oportunidades : Screen("oportunidades_page") // Ruta para el listado de oportunidades ("oportunidades_page")

    /** Información institucional de CORPALIV (con NavigationBar). */
    data object Institucion : Screen("institucion_page") // Ruta para la pantalla con información institucional ("institucion_page")

    /** GUÍA 11: formulario de registro de usuario. */
    data object Registro : Screen("registro_page") // Ruta para la pantalla del formulario de registro ("registro_page")

    /** GUÍA 11: resumen con los datos ingresados (comparte el UsuarioViewModel). */
    data object Resumen : Screen("resumen_page") // Ruta para la pantalla de resumen de datos ("resumen_page")

    /** G12: aportes de personas y empresas, para administradores. */
    data object Dashboard : Screen("dashboard_page") // Ruta para el panel de administración / Dashboard ("dashboard_page")

    /** GUÍA 13: perfil con foto desde cámara o galería. */
    data object Perfil : Screen("perfil_page") // Ruta para la pantalla de perfil del usuario ("perfil_page")

    /** GUÍA 12: interruptor con animaciones. */
    data object ModoEspecial : Screen("modo_especial_page") // Ruta para la pantalla de Modo Especial con animaciones ("modo_especial_page")

    // Ejemplo de ruta con argumentos 'data class' encapsula el argumento.
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") { // Ruta parametrizada que recibe el ID de un elemento
        override fun buildRoute(): String = route.replace("{itemId}", itemId) // Sobrescribe buildRoute() para reemplazar el comodín {itemId} con el valor real del argumento
    }

    /** Ruta final lista para navegar. Las rutas simples devuelven su propio 'route'. */
    open fun buildRoute(): String = route // Método abierto que retorna la ruta formateada para ser consumida por el NavController
}
