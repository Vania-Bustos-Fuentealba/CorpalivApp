package com.duocuc.corpalivapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.motivosRegistro
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * G11: PANTALLA DE RESUMEN
 * Observa el MISMO UsuarioViewModel que RegistroScreen y muestra los datos ingresados sin recibir argumentos por la ruta.
 * Agradece el apoyo o la postulación según el motivo elegido e informa que la institución contactará a la persona.
 */
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onFinalizar: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val motivo = motivosRegistro.find { it.etiqueta == estado.motivo }

    // El formulario ya se envió: volver atrás termina el flujo y lleva al inicio
    BackHandler { onFinalizar() }

    Scaffold(
        topBar = { AppTopBar(titulo = "Registro completado", onBack = onFinalizar) }
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
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            )

            Text(
                text = motivo?.agradecimiento ?: "¡Gracias!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            motivo?.let {
                Text(
                    text = it.confirmacion.format(estado.oportunidad),
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
                    Text("Nombre: ${estado.nombre} ${estado.apellido}")
                    Text("Correo: ${estado.correo}")
                    Text("Oportunidad: ${estado.oportunidad}")
                    Text("Motivo: ${estado.motivo}")
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
