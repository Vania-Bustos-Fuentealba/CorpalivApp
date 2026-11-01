package com.duocuc.corpalivapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR
import com.duocuc.corpalivapp.data.model.TipoRegistro
import com.duocuc.corpalivapp.data.model.motivosRegistro
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * G11: PANTALLA DE RESUMEN
 * Observa el MISMO UsuarioViewModel que RegistroScreen y muestra los datos ingresados sin recibir argumentos por la ruta.
 * Agradece el apoyo o la postulación según el motivo elegido (o el interés de la empresa) e informa que la institución contactará a la persona.
 * G12: si el registro es un aporte, muestra por 5 segundos el mensaje de agradecimiento y vuelve solo al inicio.
 */
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onFinalizar: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val persona = estado.persona
    val empresa = estado.empresa
    val esEmpresa = estado.tipo == TipoRegistro.EMPRESA

    // Se guarda al abrir la pantalla: al finalizar el formulario se limpia y la vista no debe cambiar
    val esAporte = remember { estado.esAporte }

    // Las empresas no eligen motivo: solo las personas tienen agradecimiento según su motivo
    val motivo = if (esEmpresa) null else motivosRegistro.find { it.etiqueta == persona.motivo }

    // El formulario ya se envió: volver atrás termina el flujo y lleva al inicio
    BackHandler { onFinalizar() }

    Scaffold(
        topBar = { AppTopBar(titulo = "Registro completado", onBack = onFinalizar) }
    ) { innerPadding ->
        if (esAporte) {
            MensajeAporte(
                onFinalizar = onFinalizar,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(96.dp)
                )

                Text(
                    text = if (esEmpresa) {
                        "¡Gracias por tu interés en colaborar!"
                    } else {
                        motivo?.agradecimiento ?: "¡Gracias!"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                val confirmacion = if (esEmpresa) {
                    "Registramos la solicitud de «${empresa.razonSocial}» para colaborar con «${estado.oportunidad}»."
                } else {
                    motivo?.confirmacion?.format(estado.oportunidad)
                }
                confirmacion?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                // Aviso de contacto de la institución
                Text(
                    text = "Serás contactado/a por la institución en un día hábil, vía WhatsApp o teléfono.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Resumen de tu registro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (esEmpresa) {
                            Text("Razón social: ${empresa.razonSocial}")
                            Text("Representante legal: ${empresa.representante}")
                            Text("Correo: ${empresa.correo}")
                            Text("Teléfono: +56 ${empresa.telefono}")
                            Text("Oportunidad: ${estado.oportunidad}")
                        } else {
                            Text("Nombre: ${persona.nombre} ${persona.apellido}")
                            Text("Correo: ${persona.correo}")
                            Text("Teléfono: +569 ${persona.telefono}")
                            Text("Oportunidad: ${estado.oportunidad}")
                            Text("Motivo: ${persona.motivo}")
                            if (persona.motivo == MOTIVO_APORTAR) {
                                Text("Frecuencia de la donación: ${persona.frecuenciaDonacion}")
                            }
                        }
                    }
                }

                Button(
                    onClick = onFinalizar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}

// Mensaje que se muestra al confirmar un aporte (dura 5 segundos)
private const val MENSAJE_APORTE =
    "Gracias por tu aporte, ayudarás a la causa que elegiste y harás que este mundo pueda dar un lugar a más personas"
private const val DURACION_MENSAJE_MS = 5_000

/**
 * G12: mensaje de agradecimiento de un aporte. La barra avanza durante 5 segundos
 * y al terminar se vuelve al inicio; el botón permite volver antes.
 */
@Composable
private fun MensajeAporte(
    onFinalizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avance = remember { Animatable(0f) }
    val finalizar by rememberUpdatedState(onFinalizar)

    LaunchedEffect(Unit) {
        avance.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = DURACION_MENSAJE_MS, easing = LinearEasing)
        )
        finalizar()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // liveRegion: los lectores de pantalla anuncian el mensaje al aparecer
        Text(
            text = MENSAJE_APORTE,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Aviso de contacto de la institución
        Text(
            text = "Serás contactado/a por la institución en un día hábil, vía WhatsApp o teléfono.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        LinearProgressIndicator(
            progress = { avance.value },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Volviendo al inicio…",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        TextButton(onClick = onFinalizar) {
            Text("Ir al inicio ahora")
        }
    }
}
