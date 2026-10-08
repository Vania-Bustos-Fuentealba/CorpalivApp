package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import com.duocuc.corpalivapp.util.obtenerWindowSizeClass
import kotlin.OptIn

// ======================================================================================
// 1. VISTA COMPACTA (Smartphones en orientación vertical)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(onNavigateToOportunidades: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV Mobile", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido a CORPALIV!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo CORPALIV",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Red de apoyo para Escuela Especial Jean Van Dijk y Taller Ocupacional Alba Lab.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onNavigateToOportunidades,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Oportunidades RSE")
            }
        }
    }
}

// ======================================================================================
// 2. VISTA MEDIANA (Tablets pequeñas / Dispositivos plegables)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(onNavigateToOportunidades: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV - Vista Tablet / Plegable", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Portal Institucional CORPALIV",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo CORPALIV Mediano",
                        modifier = Modifier
                            .width(180.dp)
                            .height(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Inclusión Social y Educativa",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Gestiona programas de financiamiento y proyectos RSE con mayor espacio en pantalla.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Button(
                onClick = onNavigateToOportunidades,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text("Explorar Proyectos RSE")
            }
        }
    }
}

// ======================================================================================
// 3. VISTA EXPANDIDA (Tablets en horizontal / Pantallas grandes / Escritorio)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(onNavigateToOportunidades: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV - Panel de Administración Expandido", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Panel Izquierdo: Logotipo y Marca
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo Expandido",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Corporación CORPALIV",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Panel Derecho: Descripción e Interacción
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Bienvenido a la Plataforma Adaptativa de Gestión",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Accede a la información consolidada de proyectos para la Escuela Especial Jean Van Dijk y el taller ocupacional Alba Lab.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onNavigateToOportunidades,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(56.dp)
                ) {
                    Text("Ver Oportunidades y Proyectos")
                }
            }
        }
    }
}

// ======================================================================================
// 4. CONTENEDOR ADAPTATIVO PRINCIPAL (Guía 9)
// ======================================================================================
@Composable
fun HomeScreenAdaptive(onNavigateToOportunidades: () -> Unit = {}) {
    val windowSizeClass = obtenerWindowSizeClass()

    // Evaluador exhaustivo del tamaño de ancho de ventana
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(onNavigateToOportunidades)
        WindowWidthSizeClass.Medium -> HomeScreenMediana(onNavigateToOportunidades)
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(onNavigateToOportunidades)
    }
}

// ======================================================================================
// PREVIEWS PARA CADA TAMAÑO DE PANTALLA (Guía 9 - Requerimiento 5)
// ======================================================================================
@Preview(name = "Pantalla Compacta (Smartphone)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewCompact() {
    HomeScreenCompacta()
}

@Preview(name = "Pantalla Mediana (Tablet / Plegable)", widthDp = 700, heightDp = 800, showBackground = true)
@Composable
fun PreviewMedium() {
    HomeScreenMediana()
}

@Preview(name = "Pantalla Expandida (Desktop / Tablet Horizontal)", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
fun PreviewExpanded() {
    HomeScreenExpandida()
}