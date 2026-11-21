// Configuración común: declara las versiones de los plugins para los módulos.
plugins { // Agrupa las herramientas de compilación disponibles en el proyecto.
    alias(libs.plugins.android.application) apply false // Declara Android; el módulo app será quien lo aplique.
    alias(libs.plugins.kotlin.compose) apply false // Declara el compilador Compose sin aplicarlo al proyecto raíz.
}
