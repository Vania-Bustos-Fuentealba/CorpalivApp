package com.duocuc.corpalivapp.ui.theme // Declaración del paquete al que pertenece este archivo dentro de la capa de tema de la interfaz de usuario (UI)

import androidx.compose.material3.ExperimentalMaterial3Api // Anotación para permitir el uso de APIs experimentales de Material 3
import androidx.compose.material3.FilterChipDefaults // Objeto que provee valores y configuraciones de colores predeterminadas para FilterChip
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.ui.graphics.Color // Importación de la clase Color de Jetpack Compose para manejar valores cromáticos en hexadecimal

// Azul oscuro con el que se resalta la opción elegida (tipo de formulario, secciones y filtros)
val AzulSeleccion = Color(0xFF0D47A1) // Constante global que define el tono de azul primario intenso (hexadecimal 0xFF0D47A1) utilizado para marcar estados seleccionados

/** Colores de los chips: el elegido se ve en azul oscuro con texto blanco. */
@OptIn(ExperimentalMaterial3Api::class) // Consiente el uso de la API experimental de FilterChipDefaults
@Composable
fun coloresChipAzul() = FilterChipDefaults.filterChipColors( // Función composable auxiliar que retorna una instancia de SelectableChipColors configurada
    selectedContainerColor = AzulSeleccion, // Asigna el fondo azul oscuro (AzulSeleccion) cuando el chip está en estado seleccionado
    selectedLabelColor = Color.White // Asigna el color blanco al texto de la etiqueta cuando el chip está seleccionado para asegurar contraste
)
