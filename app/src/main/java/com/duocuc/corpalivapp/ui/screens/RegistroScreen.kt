package com.duocuc.corpalivapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.DatosEmpresa
import com.duocuc.corpalivapp.data.model.DatosPersona
import com.duocuc.corpalivapp.data.model.ErroresEmpresa
import com.duocuc.corpalivapp.data.model.ErroresPersona
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR
import com.duocuc.corpalivapp.data.model.TipoRegistro
import com.duocuc.corpalivapp.data.model.frecuenciasDonacion
import com.duocuc.corpalivapp.data.model.motivosRegistro
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel

/**
 * G11: PANTALLA DE REGISTRO
 *
 *  Si el formulario es válido, avisa mediante [onRegistrado] (el MainViewModel navega al resumen).
 *  Se abre al elegir una oportunidad. Arriba se elige el tipo de formulario (persona o empresa)
 *  y los datos escritos en cada uno se conservan al cambiar de tipo.
 *  G12: al registrar se muestra una barra de carga y, al terminar, se pasa a la pantalla de agradecimiento.
 */
@OptIn(ExperimentalMaterial3Api::class)
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
        // Aportes: mientras se procesan se muestra una pantalla de carga completa
        if (estado.enviando && estado.esAporte) {
            PantallaCarga(
                progreso = estado.progreso,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
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

                // Selector del tipo de formulario: cambia los campos sin borrar lo ya escrito
                Text(
                    text = "Tipo de registro",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TipoRegistro.entries.forEach { tipo ->
                        val elegido = tipo == estado.tipo

                        FilterChip(
                            selected = elegido,
                            onClick = { viewModel.onTipoChange(tipo) },
                            enabled = !estado.enviando,
                            label = {
                                Text(
                                    text = tipo.etiqueta,
                                    fontWeight = if (elegido) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            // El tipo elegido se ve en azul oscuro con texto blanco
                            colors = coloresChipAzul(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                when (estado.tipo) {
                    TipoRegistro.PERSONA -> FormularioPersona(
                        persona = estado.persona,
                        errores = estado.errores.persona,
                        enabled = !estado.enviando,
                        viewModel = viewModel
                    )

                    TipoRegistro.EMPRESA -> FormularioEmpresa(
                        empresa = estado.empresa,
                        errores = estado.errores.empresa,
                        enabled = !estado.enviando,
                        viewModel = viewModel
                    )
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
}

/** Campos de una persona: datos de contacto y motivo (con frecuencia de la donación si aporta). */
@Composable
private fun FormularioPersona(
    persona: DatosPersona,
    errores: ErroresPersona,
    enabled: Boolean,
    viewModel: UsuarioViewModel
) {
    CampoFormulario(
        valor = persona.nombre,
        onValorChange = viewModel::onNombreChange,
        etiqueta = "Nombre",
        error = errores.nombre,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario(
        valor = persona.apellido,
        onValorChange = viewModel::onApellidoChange,
        etiqueta = "Apellido",
        error = errores.apellido,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario(
        valor = persona.run,
        onValorChange = viewModel::onRunChange,
        etiqueta = "RUN",
        error = errores.run,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario(
        valor = persona.correo,
        onValorChange = viewModel::onCorreoChange,
        etiqueta = "Correo electrónico",
        error = errores.correo,
        enabled = enabled,
        tipoTeclado = KeyboardType.Email
    )

    CampoFormulario(
        valor = persona.telefono,
        onValorChange = viewModel::onTelefonoChange,
        etiqueta = "Teléfono",
        error = errores.telefono,
        enabled = enabled,
        ayuda = "Formato: +569 XXXXXXXX",
        prefijo = "+569 ",
        tipoTeclado = KeyboardType.Phone
    )

    SelectorMotivo(
        motivo = persona.motivo,
        error = errores.motivo,
        enabled = enabled,
        onSeleccion = viewModel::onMotivoChange
    )

    // Explica qué significa el motivo elegido
    motivosRegistro.find { it.etiqueta == persona.motivo }?.let { motivo ->
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

    // La frecuencia aparece solo si el motivo es "Aportar"
    AnimatedVisibility(visible = persona.motivo == MOTIVO_APORTAR) {
        GrupoOpciones(
            titulo = "Frecuencia de la donación",
            opciones = frecuenciasDonacion,
            seleccionada = persona.frecuenciaDonacion,
            onSeleccion = viewModel::onFrecuenciaChange,
            error = errores.frecuencia,
            enabled = enabled
        )
    }
}

/** Campos de una empresa: datos legales, representante legal y contacto comercial. */
@Composable
private fun FormularioEmpresa(
    empresa: DatosEmpresa,
    errores: ErroresEmpresa,
    enabled: Boolean,
    viewModel: UsuarioViewModel
) {
    CampoFormulario(
        valor = empresa.razonSocial,
        onValorChange = viewModel::onRazonSocialChange,
        etiqueta = "Razón social",
        error = errores.razonSocial,
        enabled = enabled,
        ayuda = "Nombre legal completo de la empresa",
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario(
        valor = empresa.direccion,
        onValorChange = viewModel::onDireccionEmpresaChange,
        etiqueta = "Dirección comercial",
        error = errores.direccion,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario(
        valor = empresa.rut,
        onValorChange = viewModel::onRutEmpresaChange,
        etiqueta = "RUT de la empresa",
        error = errores.rut,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario(
        valor = empresa.giro,
        onValorChange = viewModel::onGiroChange,
        etiqueta = "Giro o actividad económica",
        error = errores.giro,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Sentences
    )

    CampoFormulario(
        valor = empresa.representante,
        onValorChange = viewModel::onRepresentanteChange,
        etiqueta = "Representante legal (nombre)",
        error = errores.representante,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario(
        valor = empresa.rutRepresentante,
        onValorChange = viewModel::onRutRepresentanteChange,
        etiqueta = "RUT del representante legal",
        error = errores.rutRepresentante,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario(
        valor = empresa.correo,
        onValorChange = viewModel::onCorreoEmpresaChange,
        etiqueta = "Correo electrónico corporativo",
        error = errores.correo,
        enabled = enabled,
        tipoTeclado = KeyboardType.Email
    )

    CampoFormulario(
        valor = empresa.telefono,
        onValorChange = viewModel::onTelefonoEmpresaChange,
        etiqueta = "Teléfono",
        error = errores.telefono,
        enabled = enabled,
        ayuda = "9 dígitos (celular o fijo)",
        prefijo = "+56 ",
        tipoTeclado = KeyboardType.Phone
    )
}

/** Pantalla de carga completa que se ve mientras se procesa un aporte (el avance es de 0 a 100 %). */
@Composable
private fun PantallaCarga(
    progreso: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Procesando tu aporte… ${(progreso * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier.fillMaxWidth()
        )
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
    prefijo: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    capitalizacion: KeyboardCapitalization = KeyboardCapitalization.None
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { (error ?: ayuda)?.let { Text(it) } },
        // Texto fijo antes de lo que escribe la persona (ej: +569)
        prefix = prefijo?.let { { Text(it) } },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            capitalization = capitalizacion
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

/** Campo desplegable de motivo: al tocarlo abre un menú con las opciones (aportar o ingresar a programa). */
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

/** Lista de opciones con RadioButton: toda la fila es clicable (área táctil mínima de 48 dp). */
@Composable
private fun GrupoOpciones(
    titulo: String,
    opciones: List<String>,
    seleccionada: String,
    onSeleccion: (String) -> Unit,
    error: String?,
    enabled: Boolean
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
                        enabled = enabled,
                        onClick = { onSeleccion(opcion) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // onClick = null: el clic lo maneja la fila completa
                RadioButton(selected = opcion == seleccionada, onClick = null, enabled = enabled)
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
