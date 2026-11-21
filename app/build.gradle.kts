
import java.util.Properties // Lee pares clave=valor del archivo privado de firma.

/*
 * ============================================================
 * PROYECTO: CORPALIV APP
 * ARCHIVO: build.gradle.kts (Module :app)
 * ============================================================
 *
 * DESCRIPCIÓN:
 * Este archivo configura la compilación de la aplicación
 * Android desarrollada en Kotlin y Jetpack Compose.
 *
 * FUNCIONALIDADES CONFIGURADAS:
 *
 * 1. Configuración principal de Android.
 * 2. Dependencias para Jetpack Compose.
 * 3. Navegación entre pantallas.
 * 4. Consumo de APIs REST mediante Retrofit.
 * 5. Gestión de sesiones y persistencia de datos.
 * 6. Pruebas unitarias con JUnit y MockK.
 * 7. Pruebas de corrutinas y ViewModels.
 * 8. Medición de cobertura mediante JaCoCo.
 *
 * IMPORTANTE:
 * Se mantienen las dependencias originales del proyecto
 * para conservar la compatibilidad con sus funcionalidades.
 *
 * ============================================================
 */


// ============================================================
// 1. PLUGINS DEL PROYECTO
// ============================================================

plugins {

    // Permite compilar el módulo como aplicación Android.
    alias(libs.plugins.android.application)

    // Habilita Kotlin y Jetpack Compose.
    alias(libs.plugins.kotlin.compose)

    // Habilita la herramienta de cobertura de código.
    jacoco
}


// ============================================================
// 2. CONFIGURACIÓN GENERAL DE ANDROID
// ============================================================

val coverageVersion = "0.8.14" // Unifica el motor de JaCoCo de Android y del informe; admite Java 25.
val signingProperties = Properties().apply { // Prepara las propiedades sin incluir contraseñas en este archivo.
    val propertiesFile = rootProject.file("keystore.properties") // Busca la configuración privada en la raíz.
    if (propertiesFile.isFile) { // Permite ejecutar pruebas aunque todavía no exista una clave de entrega.
        propertiesFile.inputStream().use { load(it) } // Lee las propiedades y cierra automáticamente el archivo.
    }
}

android {

    // Espacio de nombres de la aplicación.
    namespace = "com.duocuc.corpalivapp"

    // Versión del SDK utilizada para compilar.
    compileSdk = 35

    defaultConfig {

        // Identificador único de la aplicación.
        applicationId = "com.duocuc.corpalivapp"

        // Versión mínima de Android soportada.
        minSdk = 24

        // Versión de Android objetivo.
        targetSdk = 35

        // Versión interna de la aplicación.
        versionCode = 1

        // Versión visible para el usuario.
        versionName = "1.0"

        // Configuración para pruebas instrumentadas.
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }


    // ========================================================
    // 3. TIPOS DE COMPILACIÓN
    // ========================================================

    signingConfigs { // Declara cómo se firmará el APK de entrega.
        create("release") { // Crea una configuración independiente de la clave de depuración.
            storeFile = signingProperties.getProperty("storeFile")?.let { rootProject.file(it) } // Resuelve la ruta privada del .jks.
            storePassword = signingProperties.getProperty("storePassword") // Lee la contraseña del almacén, sin escribirla en Git.
            keyAlias = signingProperties.getProperty("keyAlias") // Selecciona la entrada de clave que se usará dentro del almacén.
            keyPassword = signingProperties.getProperty("keyPassword") // Lee la contraseña de esa entrada de clave.
        }
    }

    testCoverage { // Configura el agente con el que Android registra la ejecución de las pruebas.
        jacocoVersion = coverageVersion // Usa la misma versión que analizará los datos en el informe.
    }

    buildTypes {

        // Configuración de desarrollo.
        debug {

            // Habilita la medición de cobertura
            // durante las pruebas unitarias locales.
            enableUnitTestCoverage = true
        }

        // Configuración para generar el APK de entrega.
        release {
            signingConfig = signingConfigs.getByName("release") // Exige la firma privada al empaquetar esta variante.

            // Mantiene desactivada la reducción de código.
            isMinifyEnabled = false

            // Reglas utilizadas por ProGuard/R8.
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }


    // ========================================================
    // 4. CONFIGURACIÓN DE JAVA
    // ========================================================

    compileOptions {

        // Versión de Java utilizada por el proyecto.
        sourceCompatibility = JavaVersion.VERSION_11

        // Versión de bytecode Java generado.
        targetCompatibility = JavaVersion.VERSION_11
    }


    // ========================================================
    // 5. JETPACK COMPOSE
    // ========================================================

    buildFeatures {

        // Habilita la construcción de interfaces declarativas.
        compose = true
    }
}


// ============================================================
// 6. DEPENDENCIAS DEL PROYECTO
// ============================================================

dependencies {

    // ========================================================
    // 6.1 COMPONENTES PRINCIPALES DE ANDROID
    // ========================================================

    // Funcionalidades básicas de Android con Kotlin.
    implementation(libs.androidx.core.ktx)

    // Gestión del ciclo de vida de componentes.
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Integración del ciclo de vida con Compose.
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Integración de ViewModel con Compose.
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Integración de Activity con Compose.
    implementation(libs.androidx.activity.compose)


    // ========================================================
    // 6.2 JETPACK COMPOSE
    // ========================================================

    // Mantiene compatibles las versiones de Compose.
    implementation(platform(libs.androidx.compose.bom))

    // Componentes principales de interfaz.
    implementation(libs.androidx.compose.ui)

    // Herramientas gráficas para Compose.
    implementation(libs.androidx.compose.ui.graphics)

    // Permite previsualizar componentes.
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Componentes visuales de Material Design 3.
    implementation(libs.androidx.compose.material3)

    // Íconos básicos utilizados en la navegación.
    implementation(libs.androidx.compose.material.icons.core)


    // ========================================================
    // 6.3 RETROFIT Y CONSUMO DE APIs
    // ========================================================

    // Cliente para consumir servicios REST.
    implementation(
        "com.squareup.retrofit2:retrofit:2.9.0"
    )

    // Convierte respuestas JSON en objetos Kotlin.
    implementation(
        "com.squareup.retrofit2:converter-gson:2.9.0"
    )

    // Cliente HTTP utilizado por Retrofit.
    // También permite interceptores y autenticación JWT.
    implementation(
        "com.squareup.okhttp3:okhttp:4.12.0"
    )


    // ========================================================
    // 6.4 CARGA DE IMÁGENES
    // ========================================================

    // Permite cargar imágenes dentro de Jetpack Compose.
    implementation(
        "io.coil-kt:coil-compose:2.6.0"
    )


    // ========================================================
    // 6.5 NAVEGACIÓN Y CORRUTINAS
    // ========================================================

    // Permite navegar entre las pantallas de la aplicación.
    implementation(libs.androidx.navigation.compose)

    // Permite realizar operaciones asíncronas.
    implementation(libs.kotlinx.coroutines.android)


    // ========================================================
    // 6.6 PERSISTENCIA DE DATOS
    // ========================================================

    // Almacenamiento local mediante DataStore.
    implementation(libs.androidx.datastore.preferences)

    // Permite serializar y deserializar información JSON.
    implementation(
        "com.google.code.gson:gson:2.10.1"
    )


    // ========================================================
    // 6.7 ADAPTACIÓN A DIFERENTES PANTALLAS
    // ========================================================

    // Permite adaptar la interfaz a móviles y tablets.
    implementation(
        "androidx.compose.material3:material3-window-size-class"
    )


    // ========================================================
    // 7. PRUEBAS UNITARIAS
    // ========================================================

    // Framework principal para las pruebas unitarias.
    testImplementation(libs.junit)

    // ========================================================
    // 7.1 MOCKK
    // ========================================================

    // Permite crear objetos simulados (mocks).
    //
    // Se utilizará para simular las respuestas de
    // ExternalApiService y ExternalPostRepository.
    //
    // Esto permite probar la aplicación sin depender
    // de una conexión real a Internet.
    testImplementation(
        "io.mockk:mockk:1.14.11"
    )


    // ========================================================
    // 7.2 PRUEBAS DE CORRUTINAS
    // ========================================================

    // Permite controlar la ejecución de corrutinas
    // en las pruebas de funciones suspendidas.
    //
    // Se utilizará para probar las solicitudes
    // realizadas por Repository y ViewModel.
    testImplementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2"
    )


    // ========================================================
    // 7.3 PRUEBAS DE VIEWMODEL
    // ========================================================

    // Herramientas de arquitectura para pruebas locales.
    // Incluye InstantTaskExecutorRule para componentes
    // que trabajan con mecanismos de ejecución de Android.
    testImplementation(
        "androidx.arch.core:core-testing:2.2.0"
    )


    // ========================================================
    // 8. PRUEBAS INSTRUMENTADAS
    // ========================================================

    // Extensión de JUnit para pruebas en Android.
    androidTestImplementation(
        libs.androidx.junit
    )

    // Permite automatizar interacciones con la interfaz.
    androidTestImplementation(
        libs.androidx.espresso.core
    )

    // Alinea las versiones de Compose para pruebas.
    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    // Permite realizar pruebas de interfaces Compose.
    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )


    // ========================================================
    // 9. HERRAMIENTAS DE DESARROLLO
    // ========================================================

    // Herramientas de inspección y previsualización.
    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    // Recursos adicionales para pruebas de Compose.
    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}


// ============================================================
// 10. CONFIGURACIÓN DE JACOCO
// ============================================================

// JaCoCo mide qué partes del código fueron ejecutadas
// durante las pruebas unitarias.
//
// Permite obtener indicadores de cobertura de:
// - Clases.
// - Métodos.
// - Líneas.
// - Instrucciones.
// - Ramas de decisión.

jacoco {

    // Versión del motor de cobertura.
    toolVersion = coverageVersion
}


// ============================================================
// 11. TAREA PARA GENERAR INFORME DE COBERTURA
// ============================================================

// Esta tarea se puede ejecutar desde Gradle mediante:
//
// :app:jacocoTestReport
//
// Primero ejecuta las pruebas unitarias de debug.
// Después analiza los datos de cobertura generados.

tasks.register<JacocoReport>("jacocoTestReport") {

    // Las pruebas deben ejecutarse antes del informe.
    dependsOn("testDebugUnitTest")


    // ========================================================
    // 11.1 DATOS DE EJECUCIÓN
    // ========================================================

    // Archivos que registran las instrucciones ejecutadas
    // durante las pruebas unitarias.
    executionData.setFrom(
        fileTree(layout.buildDirectory) {

            include(
                "jacoco/testDebugUnitTest.exec"
            )

            include(
                "outputs/unit_test_code_coverage/" +
                        "debugUnitTest/testDebugUnitTest.exec"
            )
        }
    )


    // ========================================================
    // 11.2 CLASES QUE SERÁN ANALIZADAS
    // ========================================================

    // Se incluyen las clases compiladas de Kotlin y Java.
    //
    // Se excluyen archivos generados automáticamente
    // porque no representan lógica escrita manualmente.

    classDirectories.setFrom(

        fileTree(
            layout.buildDirectory.dir(
                "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes" // Salida real de Kotlin integrado en AGP 9.
            )
        ) {

            exclude("**/R.class")
            exclude("**/R$*.class")
            exclude("**/BuildConfig.*")
        },

        fileTree(
            layout.buildDirectory.dir(
                "intermediates/javac/debug/compileDebugJavaWithJavac/classes"
            )
        ) {

            exclude("**/R.class")
            exclude("**/R$*.class")
            exclude("**/BuildConfig.*")
        }
    )


    // ========================================================
    // 11.3 ARCHIVOS DE CÓDIGO FUENTE
    // ========================================================

    // Directorios donde se encuentra el código original
    // utilizado para asociar cada línea con su cobertura.

    sourceDirectories.setFrom(
        files(
            "src/main/java",
            "src/main/kotlin"
        )
    )


    // ========================================================
    // 11.4 FORMATOS DEL INFORME
    // ========================================================

    reports {

        // Informe HTML para visualizar en navegador.
        html.required.set(true)

        // Informe XML para herramientas externas.
        xml.required.set(true)

        // Desactiva el formato CSV.
        csv.required.set(false)
    }
}
