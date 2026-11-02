package com.duocuc.corpalivapp // Declaración del paquete raíz al que pertenece la actividad principal de la aplicación

import android.os.Bundle // Importación de la clase Bundle para gestionar la restauración del estado guardado de la actividad
import androidx.activity.ComponentActivity // Clase base para actividades que soportan composición y características modernas de Android
import androidx.activity.compose.setContent // Función de extensión que establece el contenido de la UI utilizando Jetpack Compose
import androidx.activity.enableEdgeToEdge // Función para habilitar el diseño de pantalla completa (Edge-to-Edge)
import com.duocuc.corpalivapp.navigation.AppNavigation // Grafo principal de navegación de la aplicación
import com.duocuc.corpalivapp.ui.theme.CorpalivAppTheme // Tema personalizado de Material Design para la aplicación CorpalivApp

/**
- Punto de entrada de la app. Toda la navegación incluida.

 */
class MainActivity : ComponentActivity() { // Actividad principal de Android que actúa como contenedor único de la app
    override fun onCreate(savedInstanceState: Bundle?) { // Método del ciclo de vida ejecutado al crear la actividad
        super.onCreate(savedInstanceState) // Invocación obligatoria a la implementación de la clase base
        enableEdgeToEdge() // Configura la interfaz para extenderse debajo de las barras del sistema (estado y navegación)
        setContent { // Define el árbol de composición de Jetpack Compose que se dibujará en pantalla
            CorpalivAppTheme { // Envoltorio que aplica los colores, tipografías y formas del tema corporativo
                AppNavigation() // Componente que administra el flujo completo de pantallas y rutas de la app
            }
        }
    }
}