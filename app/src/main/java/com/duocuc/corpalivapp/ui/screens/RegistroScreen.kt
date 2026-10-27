package com.duocuc.corpalivapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.motivosRegistro
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * G11: PANTALLA DE REGISTRO
 *
 *  Si el formulario es válido, avisa mediante [onRegistrado] (el MainViewModel navega al resumen).
 *  Se abre al elegir una oportunidad: pide los datos y el motivo (aportar o postular).
 *  G12: al registrar se muestra una barra de carga y, al terminar, se pasa a la pantalla de agradecimiento.
 */
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onBack: () -> Unit = {},
    onRegistrado: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // Al terminar la barra de carga se navega una sola vez
    LaunchedEffect(estado.completado) {
        if (estado.completado) {
            viewModel.confirmarNavegacion()
            onRegistrado()
        }
    }

    // Mientras se envía no se puede volver atrás
    BackHandler(enabled = estado.enviando) {}

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Registro",
                onBack = { if (!estado.enviando) onBack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Oportunidad a la que se quiere aportar o postular
            if (estado.oportunidad.isNotBlank()) {
                Column {
                    Text(
                        text = "Oportunidad elegida",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = estado.oportunidad,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            CampoFormulario(
                valor = estado.nombre,
                onValorChange = viewModel::onNombreChange,
                etiqueta = "Nombre",
                error = estado.errores.nombre,
                enabled = !estado.enviando,
                capitalizacion = KeyboardCapitalization.Words
            )

            CampoFormulario(
                valor = estado.apellido,
                onValorChange = viewModel::onApellidoChange,
                etiqueta = "Apellido",
                error = estado.errores.apellido,
                enabled = !estado.enviando,
                capitalizacion = KeyboardCapitalization.Words
            )

            CampoFormulario(
                valor = estado.rut,
                onValorChange = viewModel::onRutChange,
                etiqueta = "RUT",
                error = estado.errores.rut,
                enabled = !estado.enviando,
                ayuda = "Ejemplo: 12.345.678-5"
            )

            CampoFormulario(
                valor = estado.correo,
                onValorChange = viewModel::onCorreoChange,
                etiqueta = "Correo electrónico",
                error = estado.errores.correo,
                enabled = !estado.enviando,
                tipoTeclado = KeyboardType.Email
            )

            CampoFormulario(
                valor = estado.direccion,
                onValorChange = viewModel::onDireccionChange,
                etiqueta = "Dirección",
                error = estado.errores.direccion,
                enabled = !estado.enviando
            )

            SelectorMotivo(
                motivo = estado.motivo,
                error = estado.errores.motivo,
                enabled = !estado.enviando,
                onSeleccion = viewModel::onMotivoChange
            )

            // Explica qué significa el motivo elegido
            motivosRegistro.find { it.etiqueta == estado.motivo }?.let { motivo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = motivo.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Botón de enviar
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        viewModel.enviarRegistro()
                    }
                },
                enabled = !estado.enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            // G12: barra de carga del envío (progreso de 0 a 100 %)
            if (estado.enviando) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Enviando tu registro… ${(estado.progreso * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LinearProgressIndicator(
                        progress = { estado.progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** Campo de texto del formulario: muestra el mensaje de error debajo si existe (o una ayuda). */
@Composable
private fun CampoFormulario(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    enabled: Boolean = true,
    ayuda: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    capitalizacion: KeyboardCapitalization = KeyboardCapitalization.None
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { (error ?: ayuda)?.let { Text(it) } },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            capitalization = capitalizacion
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

/** Campo desplegable de motivo: al tocarlo abre un menú con las opciones (aportar o postular). */
@Composable
private fun SelectorMotivo(
    motivo: String,
    error: String?,
    enabled: Boolean,
    onSeleccion: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = motivo,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text("Motivo") },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            isError = error != null,
            supportingText = { Text(error ?: "Elige cómo quieres participar") },
            modifier = Modifier.fillMaxWidth()
        )

        // Capa transparente: abre el menú al tocar cualquier parte del campo
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    enabled = enabled,
                    onClickLabel = "Elegir motivo",
                    role = Role.DropdownList
                ) { expandido = true }
        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            motivosRegistro.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.etiqueta) },
                    onClick = {
                        onSeleccion(opcion.etiqueta)
                        expandido = false
                    }
                )
            }
        }
    }
}
