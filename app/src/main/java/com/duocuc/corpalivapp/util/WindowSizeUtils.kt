package com.duocuc.corpalivapp.util // Declaración del paquete al que pertenece este archivo dentro de la capa de utilidades del proyecto

import android.app.Activity // Importación de la clase Activity de Android para obtener la referencia de la ventana actual
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi // Anotación de la API experimental de tamaños de ventana en Material 3
import androidx.compose.material3.windowsizeclass.WindowSizeClass // Clase que representa las categorías de tamaño de pantalla (Compact, Medium, Expanded)
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass // Función utilitaria de Material 3 para calcular las dimensiones de la ventana
import androidx.compose.runtime.Composable // Anotación Composable para funciones que construyen la UI en Jetpack Compose
import androidx.compose.ui.platform.LocalContext // Objeto para acceder al contexto local actual dentro de un árbol Composable
import kotlin.OptIn // Anotación de Kotlin para autorizar explícitamente el uso de APIs experimentales

/**
- G9 - Tamaño de Pantalla:

- Función composable reutilizable para calcular la WindowSizeClass actual de la actividad en tiempo de ejecución.

 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class) // Oculta las advertencias del compilador por usar la API experimental WindowSizeClass
@Composable // Identifica a esta función como un bloque Composable invocable dentro de la UI
fun obtenerWindowSizeClass(): WindowSizeClass { // Función utilitaria que calcula y retorna las métricas de tamaño de pantalla de la Activity
    val activity = LocalContext.current as Activity // Obtiene el contexto actual de Compose y lo castea a la clase Activity correspondiente
    return calculateWindowSizeClass(activity) // Calcula y devuelve las clasificaciones de ancho y alto de la ventana de la Activity
}