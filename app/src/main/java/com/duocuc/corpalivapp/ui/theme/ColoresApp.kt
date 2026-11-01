package com.duocuc.corpalivapp.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Azul oscuro con el que se resalta la opción elegida (tipo de formulario, secciones y filtros)
val AzulSeleccion = Color(0xFF0D47A1)

/** Colores de los chips: el elegido se ve en azul oscuro con texto blanco. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun coloresChipAzul() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = AzulSeleccion,
    selectedLabelColor = Color.White
)
