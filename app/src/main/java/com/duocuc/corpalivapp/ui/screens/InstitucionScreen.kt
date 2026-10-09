package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duocuc.corpalivapp.navigation.Screen
import com.duocuc.corpalivapp.ui.components.AppBottomBar

/**
 * GUÍA 10 - PANTALLA 3: INFORMACIÓN INSTITUCIONAL
 *
 * Muestra a quién está dirigida la plataforma. Incluye la barra inferior
 * (NavigationBar) para recorrer las pantallas principales de la app.
 */
@Composable
fun InstitucionScreen(
    onNavigate: (Screen) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(selected = Screen.Institucion, onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quiénes somos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            TarjetaInstitucional(
                titulo = "CORPALIV",
                descripcion = "Corporación de Padres y Amigos por el Limitado Visual."
            )
            TarjetaInstitucional(
                titulo = "Escuela Especial Jean Van Dijk",
                descripcion = "Comunidad educativa que recibe el apoyo de empresas y donantes aliados."
            )
            TarjetaInstitucional(
                titulo = "Alba Lab",
                descripcion = "Red sociolaboral orientada a la vinculación laboral de jóvenes con discapacidad."
            )

            Button(
                onClick = { onNavigate(Screen.Home) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}

@Composable
private fun TarjetaInstitucional(titulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstitucionScreenPreview() {
    InstitucionScreen()
}
