package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.modalidadesColaboracion
import com.duocuc.corpalivapp.data.model.tiposEntidad
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * G11: PANTALLA DE INTERÉS DE COLABORACIÓN
 * Segundo paso del flujo: modalidad, tipo de entidad y mensaje. Usa el mismo UsuarioViewModel.
 */
@Composable
fun InteresScreen(
    viewModel: UsuarioViewModel,
    onBack: () -> Unit = {},
    onContinuar: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(titulo = "Interés de colaboración", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GrupoOpciones(
                titulo = "¿Cómo quieres colaborar?",
                opciones = modalidadesColaboracion,
                seleccionada = estado.modalidad,
                onSeleccion = viewModel::onModalidadChange,
                error = estado.errores.modalidad
            )

            GrupoOpciones(
                titulo = "Tipo de entidad",
                opciones = tiposEntidad,
                seleccionada = estado.tipoEntidad,
                onSeleccion = viewModel::onTipoEntidadChange,
                error = estado.errores.tipoEntidad
            )

            OutlinedTextField(
                value = estado.mensaje,
                onValueChange = viewModel::onMensajeChange,
                label = { Text("Mensaje") },
                isError = estado.errores.mensaje != null,
                // Muestra el error o, si no hay, una ayuda con el mínimo requerido
                supportingText = { Text(estado.errores.mensaje ?: "Mínimo 10 caracteres") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (viewModel.validarInteres()) {
                        onContinuar()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar al resumen")
            }
        }
    }
}

/** Lista de opciones con RadioButton: toda la fila es clicable (área táctil mínima de 48 dp). */
@Composable
private fun GrupoOpciones(
    titulo: String,
    opciones: List<String>,
    seleccionada: String,
    onSeleccion: (String) -> Unit,
    error: String?
) {
    Column(modifier = Modifier.selectableGroup()) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .selectable(
                        selected = opcion == seleccionada,
                        onClick = { onSeleccion(opcion) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // onClick = null: el clic lo maneja la fila completa
                RadioButton(selected = opcion == seleccionada, onClick = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(opcion)
            }
        }

        error?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
