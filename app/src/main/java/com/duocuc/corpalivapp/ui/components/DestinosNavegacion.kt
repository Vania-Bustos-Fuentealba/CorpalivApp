package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este archivo de configuración dentro de los componentes de la UI

import androidx.compose.material.icons.Icons // Importación del objeto contenedor global de íconos de Material Design
import androidx.compose.material.icons.filled.Favorite // Importación del ícono de corazón de Material para representar Oportunidades
import androidx.compose.material.icons.filled.Home // Importación del ícono de casa de Material para representar el Inicio
import androidx.compose.material.icons.filled.Info // Importación del ícono de información de Material para representar Institución
import androidx.compose.material.icons.filled.Person // Importación del ícono de usuario de Material para representar el Perfil
import androidx.compose.material.icons.filled.Settings // Importación del ícono de engranaje/configuración de Material para representar Modo Especial
import androidx.compose.ui.graphics.vector.ImageVector // Importación del tipo ImageVector usado para representar gráficos vectoriales en Compose
import com.duocuc.corpalivapp.navigation.Screen // Importación de la sealed class Screen para mapear los destinos a sus rutas tipadas

/**
- G10 - Destinos principales de la app.

- [destinosNavegacion]: pantallas principales; alimentan la barra inferior
- [destinosFuncionales]: pantallas de las guías 11, 12 y 13; solo van en el menú lateral.
 */
data class DestinoNavegacion( // Data class encargada de agrupar los metadatos visuales y de ruta para los menús de navegación
    val screen: Screen, // Propiedad que referencia el destino tipado de la pantalla
    val etiqueta: String, // Texto visible que se muestra en los botones o ítems de navegación
    val icono: ImageVector // Gráfico vectorial asignado como ícono del ítem de navegación
)

val destinosNavegacion = listOf( // Lista inmutable que contiene los destinos principales mostrados en la barra de navegación inferior y menú lateral
    DestinoNavegacion(Screen.Home, "Inicio", Icons.Default.Home), // Elemento para la pantalla principal de Inicio con el ícono Home
    DestinoNavegacion(Screen.Oportunidades, "Oportunidades", Icons.Default.Favorite), // Elemento para la pantalla de Oportunidades con el ícono Favorite
    DestinoNavegacion(Screen.Institucion, "Institución", Icons.Default.Info) // Elemento para la pantalla de Institución con el ícono Info
)

val destinosFuncionales = listOf( // Lista inmutable con las secciones secundarias/herramientas adicionales mostradas en el menú lateral
    DestinoNavegacion(Screen.Perfil, "Mi perfil", Icons.Default.Person), // Elemento para la gestión de Perfil de usuario con el ícono Person
    DestinoNavegacion(Screen.ModoEspecial, "Modo especial", Icons.Default.Settings) // Elemento para la pantalla de Modo Especial con el ícono Settings
)
