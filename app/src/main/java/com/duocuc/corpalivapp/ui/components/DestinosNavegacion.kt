package com.duocuc.corpalivapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.duocuc.corpalivapp.navigation.Screen

/**
 * G10 - Destinos principales de la app.
 * - [destinosNavegacion]: pantallas principales; alimentan la barra inferior
 * - [destinosFuncionales]: pantallas de las guías 11, 12 y 13; solo van en el menú lateral.
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

val destinosFuncionales = listOf(
    DestinoNavegacion(Screen.Perfil, "Mi perfil", Icons.Default.Person),
    DestinoNavegacion(Screen.ModoEspecial, "Modo especial", Icons.Default.Settings)
)
