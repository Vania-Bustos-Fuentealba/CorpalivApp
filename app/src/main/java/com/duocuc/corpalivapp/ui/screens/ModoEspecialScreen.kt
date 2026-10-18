package com.duocuc.corpalivapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.EstadoViewModel

private val ColorActivo = Color(0xFF4CAF50)
private val ColorInactivo = Color(0xFFB0BEC5)

/**
 * G12: ESTADOS Y ANIMACIONES
 */
@Composable
fun ModoEspecialScreen(
    onBack: () -> Unit = {},
    viewModel: EstadoViewModel = viewModel()
) {
    val activo by viewModel.activo.collectAsStateWithLifecycle()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsStateWithLifecycle()

    // Estado local de UI que sobrevive a rotaciones
    var detallesVisibles by rememberSaveable { mutableStateOf(false) }

    // Textos derivados: se recalculan solo cuando cambia 'activo'.
    val textoEstado by remember {
        derivedStateOf { if (activo == true) "Modo especial ACTIVADO" else "Modo especial DESACTIVADO" }
    }
    val textoBoton by remember {
        derivedStateOf { if (activo == true) "Desactivar" else "Activar" }
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Modo especial", onBack = onBack) }
    ) { innerPadding ->
        val estadoActual = activo

        if (estadoActual == null) {
            // Simulación de carga inicial antes de mostrar la UI principal
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Cargando preferencias…")
                }
            }
        } else {
            val colorAnimado by animateColorAsState(
                targetValue = if (estadoActual) ColorActivo else ColorInactivo,
                animationSpec = tween(durationMillis = 500),
                label = "colorModo"
            )
            val escalaIcono by animateFloatAsState(
                targetValue = if (estadoActual) 1.15f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "escalaIcono"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
                    .animateContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicador circular: cambia de color y alterna icono
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(escalaIcono)
                        .background(colorAnimado, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = estadoActual,
                        transitionSpec = {
                            (fadeIn(tween(300)) + scaleIn(initialScale = 0.6f)) togetherWith
                                (fadeOut(tween(300)) + scaleOut(targetScale = 0.6f))
                        },
                        label = "iconoModo"
                    ) { encendido ->
                        Icon(
                            imageVector = if (encendido) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = if (encendido) "Modo activado" else "Modo desactivado",
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = textoEstado,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.alternarEstado() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = textoBoton, style = MaterialTheme.typography.titleLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de éxito cada vez que el estado se guarda.
                AnimatedVisibility(
                    visible = mostrarMensaje,
                    enter = fadeIn(tween(300)) + slideInVertically { it / 2 },
                    exit = fadeOut(tween(300)) + slideOutVertically { it / 2 }
                ) {
                    Text(
                        text = "¡Estado guardado exitosamente!",
                        color = ColorActivo,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                TextButton(onClick = { detallesVisibles = !detallesVisibles }) {
                    Text(if (detallesVisibles) "Ocultar detalles" else "Ver detalles")
                }

                AnimatedVisibility(visible = detallesVisibles) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Esta preferencia se guarda en el dispositivo con DataStore " +
                                "Preferences, por lo que se mantiene aunque cierres la app.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
