package com.duocuc.corpalivapp.navigation

/**
 * GUÍA 10 - PARTE 1 (Paso 3): RUTAS TIPO-SAFE CON SEALED CLASS
 *
 * Define en un único lugar todas las rutas de la app. Al ser una 'sealed class',
 * el compilador conoce todos los destinos posibles y evita errores de tipeo
 * al usar strings sueltos en la navegación.
 */
sealed class Screen(val route: String) {

    // Rutas simples (sin argumentos): 'data object' es un singleton seguro de tipos.

    /** Pantalla de inicio (adaptativa, con TopAppBar y menú lateral). */
    data object Home : Screen("home_page")

    /** Listado de oportunidades y proyectos RSE (con TopAppBar y NavigationBar). */
    data object Oportunidades : Screen("oportunidades_page")

    /** Información institucional de CORPALIV (con NavigationBar). */
    data object Institucion : Screen("institucion_page")

    // Ejemplo de ruta con argumentos (no se usa en el NavHost todavía, pero deja
    // lista la forma correcta de construirla): 'data class' encapsula el argumento.
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        override fun buildRoute(): String = route.replace("{itemId}", itemId)
    }

    /** Ruta final lista para navegar. Las rutas simples devuelven su propio 'route'. */
    open fun buildRoute(): String = route
}
