package com.duocuc.corpalivapp.navigation

/**
 * G10: RUTAS TIPO-SAFE CON SEALED CLASS
 * Define en un único lugar todas las rutas de la app.
 */
sealed class Screen(val route: String) {

    // Rutas simples (sin argumentos).

    /** Pantalla de inicio (adaptativa, con TopAppBar y menú lateral). */
    data object Home : Screen("home_page")

    /** Listado de oportunidades y proyectos RSE (con TopAppBar y NavigationBar). */
    data object Oportunidades : Screen("oportunidades_page")

    /** Información institucional de CORPALIV (con NavigationBar). */
    data object Institucion : Screen("institucion_page")

    /** GUÍA 11: formulario de registro de usuario. */
    data object Registro : Screen("registro_page")

    /** GUÍA 11: resumen con los datos ingresados (comparte el UsuarioViewModel). */
    data object Resumen : Screen("resumen_page")

    /** G12: aportes de personas y empresas, para administradores. */
    data object Dashboard : Screen("dashboard_page")

    /** GUÍA 13: perfil con foto desde cámara o galería. */
    data object Perfil : Screen("perfil_page")

    /** GUÍA 12: interruptor con animaciones. */
    data object ModoEspecial : Screen("modo_especial_page")

    // Ejemplo de ruta con argumentos 'data class' encapsula el argumento.
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        override fun buildRoute(): String = route.replace("{itemId}", itemId)
    }

    /** Ruta final lista para navegar. Las rutas simples devuelven su propio 'route'. */
    open fun buildRoute(): String = route
}
