package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.components.MarkdownEnBloques
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul
import com.duocuc.corpalivapp.ui.viewmodel.InstitucionUiState
import com.duocuc.corpalivapp.ui.viewmodel.InstitucionViewModel
import com.duocuc.corpalivapp.ui.viewmodel.SeccionInstitucion

/**
 * G10: INFORMACIÓN INSTITUCIONAL
 * Muestra a quién está dirigida la plataforma. Incluye la barra inferior
 * Ahora la barra inferior la muestra AppNavigation y el texto de cada sección se lee de un .md en assets/institucion.
 * Las dos secciones (Escuela Jan Van Dijk y Alba Lab) van una a cada costado y se muestran en bloques.
 * Tiene una barra superior con una flecha para volver al inicio.
 */
@Composable
fun InstitucionScreen(
    viewModel: InstitucionViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    InstitucionContenido(
        estado = estado,
        onSeleccion = viewModel::seleccionar,
        onBack = onBack
    )
}

/** Parte visual sin ViewModel, para poder verla en el @Preview. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstitucionContenido(
    estado: InstitucionUiState,
    onSeleccion: (SeccionInstitucion) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(titulo = "Institución", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Quiénes somos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de sección: una a cada costado; la elegida se resalta en azul
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SeccionInstitucion.entries.forEach { seccion ->
                    val elegida = seccion == estado.seccion

                    FilterChip(
                        selected = elegida,
                        onClick = { onSeleccion(seccion) },
                        label = {
                            Text(
                                text = seccion.etiqueta,
                                fontWeight = if (elegida) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = coloresChipAzul()
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    estado.error -> Text(
                        text = "No se pudo cargar la información. Intenta nuevamente.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    estado.contenido == null -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // key: al cambiar de sección el texto vuelve al inicio
                    else -> key(estado.seccion) {
                        MarkdownEnBloques(
                            markdown = estado.contenido,
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstitucionScreenPreview() {
    InstitucionContenido(
        estado = InstitucionUiState(
            contenido = "# Corpaliv\n\nTexto de ejemplo con **negrita**.\n\n- Primer punto\n- Segundo punto"
        ),
        onSeleccion = {},
        onBack = {}
    )
}
