package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duocuc.corpalivapp.R
import kotlin.OptIn

/**
 * GUÍA 8 - CONSTRUCCIÓN VISUAL DE PANTALLA BASE CON JETPACK COMPOSE:
 *
 * Implementación de la pantalla base mediante Scaffold, organizando jerárquicamente
 * TopAppBar, Column, Text, Button e Image con estilos de Material3 y Modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToOportunidades: () -> Unit = {}
) {
    // GUÍA 8 - ESTRUCTURA BASE SCAFFOLD: Provee la barra superior y gestiona los márgenes seguros
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CORPALIV - Inclusión & RSE",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // GUÍA 8 - MÁRGENES Y ESPACIADO UNIFORME:
        // Se aplica Modifier.padding(innerPadding) para respetar el Scaffold y Arrangement.spacedBy(20.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // GUÍA 8 - ELEMENTO TEXT: Encabezado principal de bienvenida
            Text(
                text = "¡Bienvenido a CORPALIV!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // GUÍA 8 - ELEMENTO TEXT: Bajada descriptiva de contexto institucional
            Text(
                text = "Plataforma de vinculación para Escuela Especial Jean Van Dijk y Taller Ocupacional Alba Lab.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // GUÍA 8 - ELEMENTO IMAGE: Carga del logotipo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo Institucional CORPALIV",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            // GUÍA 8 - ELEMENTO BUTTON: Acción principal de navegación e interacción
            Button(
                onClick = { onNavigateToOportunidades() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ver Oportunidades y Proyectos RSE",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

// GUÍA 8 - PREVIEW: Permite previsualizar la interfaz en tiempo de diseño en Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}