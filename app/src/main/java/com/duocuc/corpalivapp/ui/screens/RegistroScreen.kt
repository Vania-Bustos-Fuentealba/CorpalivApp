package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.activity.compose.BackHandler // Componente de Compose para interceptar y gestionar el evento del botón físico/gesto de volver atrás
import androidx.compose.animation.AnimatedVisibility // Componente para animar la visibilidad de elementos de la UI
import androidx.compose.foundation.clickable // Modificador para habilitar clics e interacciones sobre composables
import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado entre elementos
import androidx.compose.foundation.layout.Box // Contenedor Box para apilar o alinear componentes en el mismo espacio
import androidx.compose.foundation.layout.Column // Contenedor de diseño vertical Column
import androidx.compose.foundation.layout.Row // Contenedor de diseño horizontal Row
import androidx.compose.foundation.layout.Spacer // Componente Spacer para insertar espacios vacíos ajustables
import androidx.compose.foundation.layout.consumeWindowInsets // Modificador para consumir insets de la ventana y evitar solapamientos
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.height // Modificador para fijar la altura de un componente
import androidx.compose.foundation.layout.heightIn // Modificador para fijar un rango de alturas mínimas y máximas
import androidx.compose.foundation.layout.imePadding // Modificador que añade un relleno dinámico cuando se despliega el teclado (IME)
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios de relleno interno
import androidx.compose.foundation.layout.width // Modificador para fijar un ancho específico a un componente
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado de desplazamiento de un contenedor
import androidx.compose.foundation.selection.selectable // Modificador para marcar un elemento como seleccionable dentro de un grupo
import androidx.compose.foundation.selection.selectableGroup // Modificador para agrupar semánticamente opciones seleccionables (RadioButtons)
import androidx.compose.foundation.text.KeyboardOptions // Configuración de opciones del teclado (tipo, capitalización, etc.)
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical en un contenedor
import androidx.compose.material.icons.Icons // Objeto contenedor global de íconos de Material Design
import androidx.compose.material.icons.filled.ArrowDropDown // Ícono de flecha hacia abajo para menús desplegables
import androidx.compose.material3.Button // Componente de botón principal de Material 3
import androidx.compose.material3.Card // Componente de tarjeta contenedora de Material 3
import androidx.compose.material3.CircularProgressIndicator // Componente de rueda de carga de Material 3
import androidx.compose.material3.CardDefaults // Valores y configuraciones predeterminadas para componentes Card
import androidx.compose.material3.DropdownMenu // Componente para mostrar un menú desplegable
import androidx.compose.material3.DropdownMenuItem // Elemento individual dentro de un DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api // Anotación para permitir el uso de APIs experimentales de Material 3
import androidx.compose.material3.FilterChip // Componente chip de filtro seleccionable de Material 3
import androidx.compose.material3.Icon // Componente Icon de Material 3 para desplegar vectores gráficos
import androidx.compose.material3.LinearProgressIndicator // Componente de barra de progreso lineal
import androidx.compose.material3.MaterialTheme // Acceso a la paleta de colores, tipografía y formas del tema Material
import androidx.compose.material3.OutlinedTextField // Campo de texto de entrada con borde delineado
import androidx.compose.material3.RadioButton // Componente de botón de opción única (RadioButton)
import androidx.compose.material3.Scaffold // Estructura contenedora base con soporte para barra superior
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.runtime.LaunchedEffect // Efecto de Compose que se ejecuta al entrar en la composición o al cambiar sus claves
import androidx.compose.runtime.getValue // Delegado de propiedad para la lectura simplificada de estados en Compose
import androidx.compose.runtime.mutableStateOf // Crea un estado mutable observable por Compose
import androidx.compose.runtime.remember // Función para recordar objetos a través de recomposiciones
import androidx.compose.runtime.setValue // Delegado de propiedad para la escritura simplificada en estados
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador de UI base para configurar el aspecto y comportamiento de composables
import androidx.compose.ui.semantics.Role // Define el rol semántico del componente para accesibilidad (TalkBack)
import androidx.compose.ui.text.font.FontWeight // Propiedad para especificar el peso de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.input.KeyboardCapitalization // Opciones de capitalización del teclado (Words, Sentences, Characters)
import androidx.compose.ui.text.input.KeyboardType // Opciones del tipo de teclado virtual (Email, Phone, Text, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación del texto
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida
import com.duocuc.corpalivapp.data.model.DatosEmpresa // Modelo de datos para guardar la información del formulario de empresa
import com.duocuc.corpalivapp.data.model.DatosPersona // Modelo de datos para guardar la información del formulario de persona
import com.duocuc.corpalivapp.data.model.ErroresEmpresa // Modelo que contiene los mensajes de validación para empresas
import com.duocuc.corpalivapp.data.model.ErroresPersona // Modelo que contiene los mensajes de validación para personas
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR // Constante que identifica el motivo de donación/aporte
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum con los tipos de formulario (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.data.model.frecuenciasDonacion // Lista de frecuencias disponibles para las donaciones
import com.duocuc.corpalivapp.data.model.motivosRegistro // Lista de opciones de motivos de registro
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul // Función de estilo de color personalizado para chips
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel // ViewModel que gestiona la lógica del formulario de registro

/**
- G11: PANTALLA DE REGISTRO

 *
-  Si el formulario es válido, avisa mediante [onRegistrado] (el MainViewModel navega al resumen).
-  Se abre al elegir una oportunidad. Arriba se elige el tipo de formulario (persona o empresa)

-  y los datos escritos en cada uno se conservan al cambiar de tipo.

-  G12: al registrar se muestra una barra de carga y, al terminar, se pasa a la pantalla de agradecimiento.

 */
@OptIn(ExperimentalMaterial3Api::class) // Consiente el uso de APIs experimentales requeridas por Material 3
@Composable
fun RegistroScreen( // Componente principal de la pantalla de registro de aportes e inscripciones
    viewModel: UsuarioViewModel, // ViewModel inyectado que maneja el estado del formulario y su validación
    onBack: () -> Unit = {}, // Callback para regresar a la pantalla anterior
    onRegistrado: () -> Unit = {} // Callback ejecutado al completar el registro para navegar al resumen
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle() // Observa el estado del registro respetando el ciclo de vida

    // Al terminar la barra de carga se navega una sola vez
    LaunchedEffect(estado.completado) { // Dispara un efecto cuando el flag completado cambia a true
        if (estado.completado) {
            viewModel.confirmarNavegacion() // Notifica al ViewModel que se ha procesado la navegación
            onRegistrado() // Invoca la navegación a la pantalla de resumen
        }
    }

    // Mientras se envía no se puede volver atrás
    BackHandler(enabled = estado.enviando) {} // Deshabilita el botón/gesto de volver si hay un envío en curso

    Scaffold( // Estructura contenedor base
        topBar = {
            AppTopBar(
                titulo = "Registro", // Título de la barra superior
                onBack = { if (!estado.enviando) onBack() } // Permite volver solo si no se está enviando el formulario
            )
        }
    ) { innerPadding -> // Recibe el espacio de reserva superior
        // Aportes: mientras se procesan se muestra una pantalla de carga completa
        if (estado.enviando && estado.esAporte) { // Si es una donación y está enviando, muestra pantalla de carga completa
            PantallaCarga(
                progreso = estado.progreso, // Valor de progreso entre 0.0f y 1.0f
                modifier = Modifier
                    .fillMaxSize() // Expande a la pantalla completa
                    .padding(innerPadding) // Respeta los insets del Scaffold
            )
        } else {
            Column( // Contenedor vertical desplazable del formulario
                modifier = Modifier
                    .fillMaxSize() // Expande a toda la superficie
                    .padding(innerPadding) // Aplica el padding del Scaffold
                    .consumeWindowInsets(innerPadding) // Consume insets para evitar superposiciones
                    .imePadding() // Añade espacio dinámico por el teclado virtual
                    .verticalScroll(rememberScrollState()) // Habilita el desplazamiento vertical
                    .padding(16.dp), // Margen general de 16dp
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado de 12dp entre elementos
            ) {
                // Oportunidad a la que se quiere aportar o postular
                if (estado.oportunidad.isNotBlank()) { // Muestra el título de la oportunidad si fue seleccionada una previamente
                    Column {
                        Text(
                            text = "Oportunidad elegida", // Etiqueta superior
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = estado.oportunidad, // Nombre de la oportunidad
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Selector del tipo de formulario: cambia los campos sin borrar lo ya escrito
                Text(
                    text = "Tipo de registro", // Etiqueta descriptiva
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Row( // Fila contenedora para alternar entre formulario Persona o Empresa
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio de 8dp entre chips
                ) {
                    TipoRegistro.entries.forEach { tipo -> // Recorre las opciones de tipo de registro (PERSONA, EMPRESA)
                        val elegido = tipo == estado.tipo // Evalúa cuál opción está seleccionada

                        FilterChip( // Componente Chip para alternar el tipo
                            selected = elegido, // Marca el estado activo
                            onClick = { viewModel.onTipoChange(tipo) }, // Cambia el tipo en el ViewModel sin limpiar los campos
                            enabled = !estado.enviando, // Se deshabilita durante el envío
                            label = {
                                Text(
                                    text = tipo.etiqueta, // Muestra "Persona" o "Empresa"
                                    fontWeight = if (elegido) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            // El tipo elegido se ve en azul oscuro con texto blanco
                            colors = coloresChipAzul(), // Aplica paleta de color azul personalizada
                            modifier = Modifier.weight(1f) // Cada chip toma la mitad del ancho
                        )
                    }
                }

                when (estado.tipo) { // Renderiza dinámicamente el formulario según el tipo seleccionado
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
                Button( // Botón para validar e iniciar el proceso de envío
                    onClick = {
                        if (viewModel.validarFormulario()) { // Dispara la validación de campos
                            viewModel.enviarRegistro() // Procesa el envío si es válido
                        }
                    },
                    enabled = !estado.enviando, // Deshabilitado si hay un proceso activo
                    modifier = Modifier.fillMaxWidth() // Ancho completo
                ) {
                    Text("Registrar") // Etiqueta del botón
                }

                // G12: barra de carga del envío (progreso de 0 a 100 %)
                if (estado.enviando) { // Muestra el indicador lineal de avance cuando no es aporte o durante el envío
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Enviando tu registro… ${(estado.progreso * 100).toInt()}%", // Muestra el porcentaje calculado multiplicando por 100
                            style = MaterialTheme.typography.bodyMedium
                        )
                        LinearProgressIndicator( // Barra de progreso lineal
                            progress = { estado.progreso }, // Expresión lambda con el avance actual
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
private fun FormularioPersona( // Sub-composable privado para los campos del formulario de Persona
    persona: DatosPersona, // Estado con los valores introducidos
    errores: ErroresPersona, // Estado con los mensajes de error
    enabled: Boolean, // Control de habilitación de controles
    viewModel: UsuarioViewModel // ViewModel para enlazar los eventos de cambio
) {
    CampoFormulario( // Campo Nombre
        valor = persona.nombre,
        onValorChange = viewModel::onNombreChange,
        etiqueta = "Nombre",
        error = errores.nombre,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario( // Campo Apellido
        valor = persona.apellido,
        onValorChange = viewModel::onApellidoChange,
        etiqueta = "Apellido",
        error = errores.apellido,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario( // Campo RUN
        valor = persona.run,
        onValorChange = viewModel::onRunChange,
        etiqueta = "RUN",
        error = errores.run,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario( // Campo Correo electrónico
        valor = persona.correo,
        onValorChange = viewModel::onCorreoChange,
        etiqueta = "Correo electrónico",
        error = errores.correo,
        enabled = enabled,
        tipoTeclado = KeyboardType.Email
    )

    CampoFormulario( // Campo Teléfono
        valor = persona.telefono,
        onValorChange = viewModel::onTelefonoChange,
        etiqueta = "Teléfono",
        error = errores.telefono,
        enabled = enabled,
        ayuda = "Formato: +569 XXXXXXXX",
        prefijo = "+569 ",
        tipoTeclado = KeyboardType.Phone
    )

    SelectorMotivo( // Campo desplegable para elegir el motivo de participación
        motivo = persona.motivo,
        error = errores.motivo,
        enabled = enabled,
        onSeleccion = viewModel::onMotivoChange
    )

    // Explica qué significa el motivo elegido
    motivosRegistro.find { it.etiqueta == persona.motivo }?.let { motivo -> // Busca la descripción del motivo seleccionado
        Card( // Muestra una tarjeta informativa sobre el motivo
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = motivo.descripcion, // Muestra la descripción detallada
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    // La frecuencia aparece solo si el motivo es "Aportar"
    AnimatedVisibility(visible = persona.motivo == MOTIVO_APORTAR) { // Muestra condicionalmente con animación si el motivo es Aportar
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
private fun FormularioEmpresa( // Sub-composable privado para los campos del formulario de Empresa
    empresa: DatosEmpresa, // Estado con los valores de la empresa
    errores: ErroresEmpresa, // Estado con los errores de validación de la empresa
    enabled: Boolean, // Control de habilitación de inputs
    viewModel: UsuarioViewModel // ViewModel para enlazar eventos
) {
    CampoFormulario( // Campo Razón Social
        valor = empresa.razonSocial,
        onValorChange = viewModel::onRazonSocialChange,
        etiqueta = "Razón social",
        error = errores.razonSocial,
        enabled = enabled,
        ayuda = "Nombre legal completo de la empresa",
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario( // Campo Dirección comercial
        valor = empresa.direccion,
        onValorChange = viewModel::onDireccionEmpresaChange,
        etiqueta = "Dirección comercial",
        error = errores.direccion,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario( // Campo RUT Empresa
        valor = empresa.rut,
        onValorChange = viewModel::onRutEmpresaChange,
        etiqueta = "RUT de la empresa",
        error = errores.rut,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario( // Campo Giro o Actividad económica
        valor = empresa.giro,
        onValorChange = viewModel::onGiroChange,
        etiqueta = "Giro o actividad económica",
        error = errores.giro,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Sentences
    )

    CampoFormulario( // Campo Representante Legal
        valor = empresa.representante,
        onValorChange = viewModel::onRepresentanteChange,
        etiqueta = "Representante legal (nombre)",
        error = errores.representante,
        enabled = enabled,
        capitalizacion = KeyboardCapitalization.Words
    )

    CampoFormulario( // Campo RUT Representante Legal
        valor = empresa.rutRepresentante,
        onValorChange = viewModel::onRutRepresentanteChange,
        etiqueta = "RUT del representante legal",
        error = errores.rutRepresentante,
        enabled = enabled,
        ayuda = "Formato: XXXXXXXX-X",
        capitalizacion = KeyboardCapitalization.Characters
    )

    CampoFormulario( // Campo Correo electrónico corporativo
        valor = empresa.correo,
        onValorChange = viewModel::onCorreoEmpresaChange,
        etiqueta = "Correo electrónico corporativo",
        error = errores.correo,
        enabled = enabled,
        tipoTeclado = KeyboardType.Email
    )

    CampoFormulario( // Campo Teléfono
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
private fun PantallaCarga( // Sub-composable privado para mostrar el diálogo/pantalla de carga completa en aportes
    progreso: Float, // Porcentaje de progreso de 0.0f a 1.0f
    modifier: Modifier = Modifier
) {
    Column( // Contenedor centrado
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator() // Rueda animada de carga
        Spacer(modifier = Modifier.height(24.dp))
        Text( // Muestra el porcentaje entero procesado
            text = "Procesando tu aporte… ${(progreso * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator( // Barra de progreso lineal animada
            progress = { progreso },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Campo de texto del formulario: muestra el mensaje de error debajo si existe (o una ayuda). */
@Composable
private fun CampoFormulario( // Componente auxiliar genérico para construir campos OutlinedTextField estandarizados
    valor: String, // Valor actual del texto
    onValorChange: (String) -> Unit, // Callback para cambios de texto
    etiqueta: String, // Nombre o label del campo
    error: String?, // Mensaje de error de validación (null si es válido)
    enabled: Boolean = true, // Control de habilitación
    ayuda: String? = null, // Texto auxiliar de ayuda
    prefijo: String? = null, // Texto prefijo estático (ej: +569)
    tipoTeclado: KeyboardType = KeyboardType.Text, // Tipo de teclado virtual
    capitalizacion: KeyboardCapitalization = KeyboardCapitalization.None // Regla de mayúsculas automáticas
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null, // Aplica el estilo de error visual si el texto de error no es nulo
        supportingText = { (error ?: ayuda)?.let { Text(it) } }, // Muestra el error con prioridad sobre el texto de ayuda
        // Texto fijo antes de lo que escribe la persona (ej: +569)
        prefix = prefijo?.let { { Text(it) } }, // Renderiza el prefijo si está presente
        singleLine = true, // Restringe la entrada a una sola línea
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            capitalization = capitalizacion
        ),
        modifier = Modifier.fillMaxWidth() // Ancho completo
    )
}

/** Campo desplegable de motivo: al tocarlo abre un menú con las opciones (aportar o ingresar a programa). */
@Composable
private fun SelectorMotivo( // Componente desplegable personalizado para el motivo
    motivo: String, // Valor del motivo actualmente seleccionado
    error: String?, // Error de validación
    enabled: Boolean, // Habilitación
    onSeleccion: (String) -> Unit // Callback de selección
) {
    var expandido by remember { mutableStateOf(false) } // Estado local para abrir o cerrar el menú flotante

    Box { // Contenedor para posicionar el menú desplegable sobre el campo
        OutlinedTextField(
            value = motivo,
            onValueChange = {},
            readOnly = true, // Hace que el campo sea de solo lectura para evitar edición directa por teclado
            enabled = enabled,
            label = { Text("Motivo") },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }, // Ícono de flecha desplegable
            isError = error != null,
            supportingText = { Text(error ?: "Elige cómo quieres participar") }, // Muestra el error o la instrucción por defecto
            modifier = Modifier.fillMaxWidth()
        )

        // Capa transparente: abre el menú al tocar cualquier parte del campo
        Box(
            modifier = Modifier
                .matchParentSize() // Se acopla exactamente a las dimensiones del OutlinedTextField
                .clickable(
                    enabled = enabled,
                    onClickLabel = "Elegir motivo",
                    role = Role.DropdownList
                ) { expandido = true } // Abre el menú flotante al pulsar cualquier punto
        )

        DropdownMenu( // Menú flotante con la lista de opciones
            expanded = expandido,
            onDismissRequest = { expandido = false } // Cierra el menú al tocar fuera
        ) {
            motivosRegistro.forEach { opcion -> // Itera sobre las opciones de motivos disponibles
                DropdownMenuItem(
                    text = { Text(opcion.etiqueta) }, // Texto de cada opción
                    onClick = {
                        onSeleccion(opcion.etiqueta) // Notifica la selección
                        expandido = false // Cierra el menú desplegable
                    }
                )
            }
        }
    }
}

/** Lista de opciones con RadioButton: toda la fila es clicable (área táctil mínima de 48 dp). */
@Composable
private fun GrupoOpciones( // Componente para seleccionar una opción mediante RadioButtons
    titulo: String, // Título de la sección de opciones
    opciones: List<String>, // Lista de opciones disponibles
    seleccionada: String, // Opción activa
    onSeleccion: (String) -> Unit, // Callback para Notificar el cambio
    error: String?, // Error de validación
    enabled: Boolean // Control de habilitación
) {
    Column(modifier = Modifier.selectableGroup()) { // Agrupa semánticamente las opciones seleccionables
        Text(
            text = titulo, // Título del grupo
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        opciones.forEach { opcion -> // Itera sobre cada opción de la lista
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp) // Asigna un área táctil mínima recomendada de 48dp para accesibilidad
                    .selectable(
                        selected = opcion == seleccionada,
                        enabled = enabled,
                        onClick = { onSeleccion(opcion) }, // Permite seleccionar al pulsar cualquier lugar de la fila
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically // Alineación vertical al centro
            ) {
                // onClick = null: el clic lo maneja la fila completa
                RadioButton(selected = opcion == seleccionada, onClick = null, enabled = enabled) // Botón de radio decorativo que hereda la acción de la fila
                Spacer(modifier = Modifier.width(8.dp)) // Espaciado de 8dp entre el botón y el texto
                Text(opcion) // Texto de la opción
            }
        }

        error?.let { mensaje -> // Renderiza el mensaje de error si está presente
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}