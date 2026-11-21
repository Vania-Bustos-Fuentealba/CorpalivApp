
package com.duocuc.corpalivapp.navigation

/**
 * G10: RUTAS TIPO-SAFE CON SEALED CLASS
 *
 * Define en un único lugar todas las rutas de la app.
 */
sealed class Screen(val route: String) {

    // Pantalla principal de inicio.
    data object Home : Screen("home_page")

    // Listado de oportunidades y proyectos RSE.
    data object Oportunidades : Screen("oportunidades_page")

    // Información institucional de CORPALIV.
    data object Institucion : Screen("institucion_page")

    /** PARCIAL 4: formulario para crear o modificar una oportunidad (solo administradores). */
    data object OportunidadForm : Screen("oportunidad_form_page") // Ruta para el formulario de oportunidades ("oportunidad_form_page")

    // Formulario de registro de usuario.
    data object Registro : Screen("registro_page")

    // Resumen con los datos ingresados.
    data object Resumen : Screen("resumen_page")

    // Panel de administración / Dashboard.
    data object Dashboard : Screen("dashboard_page")

    // Perfil del usuario.
    data object Perfil : Screen("perfil_page")

    // Pantalla de Modo Especial con animaciones.
    data object ModoEspecial : Screen("modo_especial_page")

    // NUEVA: pantalla que consume una API externa.
    data object ApiExterna : Screen("api_externa_page")

    // Ruta parametrizada para mostrar detalles.
    data class Detail(val itemId: String) :
        Screen("detail_page/{itemId}") {

        override fun buildRoute(): String =
            route.replace("{itemId}", itemId)
    }

    // Devuelve la ruta que utilizará NavController.
    open fun buildRoute(): String = route
}
