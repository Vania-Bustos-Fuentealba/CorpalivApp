pluginManagement { // Define dónde Gradle buscará los plugins de compilación.
    repositories { // Enumera los repositorios de plugins permitidos.
        google { // Consulta el repositorio oficial de herramientas Android.
            content { // Limita esta búsqueda a los grupos que publica Google.
                includeGroupByRegex("com\\.android.*") // Incluye las herramientas del Android Gradle Plugin.
                includeGroupByRegex("com\\.google.*") // Incluye plugins del grupo Google.
                includeGroupByRegex("androidx.*") // Incluye herramientas del grupo AndroidX.
            }
        }
        mavenCentral() // Permite resolver plugins y sus dependencias publicadas en Maven Central.
        gradlePluginPortal() // Incluye el portal oficial de plugins de Gradle.
    }
}
plugins { // Aplica plugins que configuran el conjunto del proyecto.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" // Ayuda a localizar el JDK requerido por las toolchains.
}
dependencyResolutionManagement { // Centraliza dónde se descargan las librerías de todos los módulos.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Evita que un módulo agregue repositorios distintos sin declararlos aquí.
    repositories { // Define las fuentes comunes de dependencias.
        google() // Proporciona AndroidX, Compose y otras librerías Android.
        mavenCentral() // Proporciona Retrofit, MockK, corrutinas y otras librerías JVM.
    }
}

rootProject.name = "CorpalivApp" // Nombre mostrado por Gradle y Android Studio.
include(":app") // Incluye el módulo que contiene la aplicación Android.
