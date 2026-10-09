package com.duocuc.corpalivapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.duocuc.corpalivapp.navigation.Screen

/**
 * GUÍA 10 - Destinos principales de la app.
 * Una sola lista alimenta tanto el menú lateral (NavigationDrawer) como la
 * barra inferior (NavigationBar), así ambos se mantienen siempre sincronizados.
 */
data class DestinoNavegacion(
    val screen: Screen,
    val etiqueta: String,
    val icono: ImageVector
)

val destinosNavegacion = listOf(
    DestinoNavegacion(Screen.Home, "Inicio", Icons.Default.Home),
    DestinoNavegacion(Screen.Oportunidades, "Oportunidades", Icons.Default.Favorite),
    DestinoNavegacion(Screen.Institucion, "Institución", Icons.Default.Info)
)
