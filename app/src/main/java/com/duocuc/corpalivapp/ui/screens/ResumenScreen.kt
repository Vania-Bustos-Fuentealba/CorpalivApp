package com.duocuc.corpalivapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.delay

/**
 * G11: PANTALLA DE RESUMEN
 * Observa el MISMO UsuarioViewModel que RegistroScreen y muestra los datos ingresados sin recibir argumentos por la ruta.
 */
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onBack: () -> Unit = {},
    onFinalizar: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // G11: tras enviar, se deja ver la confirmación unos segundos y se finaliza el flujo
    LaunchedEffect(estado.enviado) {
        if (estado.enviado) {
            delay(2500)
            onFinalizar()
        }
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Resumen del registro", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumen del Registro",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nombre: ${estado.nombre}")
                    Text("Correo: ${estado.correo}")
                    Text("Dirección: ${estado.direccion}")
                    // La contraseña nunca se muestra: solo se enmascara con asteriscos
                    Text("Contraseña: ${"*".repeat(estado.clave.length)}")
                    Text("Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")
                    Text("Modalidad: ${estado.modalidad}")
                    Text("Tipo de entidad: ${estado.tipoEntidad}")
                    Text("Mensaje: ${estado.mensaje}")
                }
            }

            // Confirmación del envío (liveRegion: los lectores de pantalla la anuncian)
            AnimatedVisibility(
                visible = estado.enviado,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Text("¡Solicitud enviada! Gracias por querer colaborar con CORPALIV.")
                    }
                }
            }

            OutlinedButton(
                onClick = onBack,
                enabled = !estado.enviado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar datos")
            }

            Button(
                onClick = { viewModel.enviar() },
                enabled = !estado.enviado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar solicitud")
            }
        }
    }
}
