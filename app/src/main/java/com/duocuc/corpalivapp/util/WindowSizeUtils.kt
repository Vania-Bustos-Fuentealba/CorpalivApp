package com.duocuc.corpalivapp.util

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlin.OptIn

/**
 * GUÍA 9 - UTILITARIO DE TAMAÑO DE VENTANA:
 * Función composable reutilizable para calcular la WindowSizeClass actual
 * de la actividad en tiempo de ejecución.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(activity)
}