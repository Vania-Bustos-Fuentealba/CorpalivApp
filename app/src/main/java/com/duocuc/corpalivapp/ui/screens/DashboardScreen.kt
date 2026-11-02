package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución de los elementos en la pantalla
import androidx.compose.foundation.layout.Box // Importación del contenedor Box para apilar o alinear elementos al centro
import androidx.compose.foundation.layout.PaddingValues // Importación para definir valores de márgenes internos o paddings de listas
import androidx.compose.foundation.layout.Row // Importación del contenedor horizontal Row
import androidx.compose.foundation.layout.Spacer // Importación del componente Spacer para añadir espacios vacíos
import androidx.compose.foundation.layout.Column // Importación del contenedor vertical Column
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.padding // Modificador para añadir márgenes o relleno interno
import androidx.compose.foundation.layout.width // Modificador para fijar un ancho específico a un componente
import androidx.compose.foundation.lazy.LazyColumn // Importación de LazyColumn para renderizar listas verticales optimizadas
import androidx.compose.foundation.lazy.items // Importación de la función de extensión items para iterar sobre colecciones en LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape // Importación de la forma con esquinas redondeadas para contenedores y superficies
import androidx.compose.material3.Button // Importación del botón de acción estándar de Material 3
import androidx.compose.material3.CircularProgressIndicator // Importación del indicador de carga circular
import androidx.compose.material3.ExperimentalMaterial3Api // Importación de la anotación para habilitar APIs experimentales de Material 3
import androidx.compose.material3.FilterChip // Componente FilterChip de Material 3 para representar filtros seleccionables
import androidx.compose.material3.MaterialTheme // Acceso al esquema de colores y tipografía del tema de Material Design
import androidx.compose.material3.OutlinedCard // Componente de tarjeta con borde dibujado para enmarcar contenido
import androidx.compose.material3.Scaffold // Estructura visual básica de pantalla que soporta topBar y snackbarHost
import androidx.compose.material3.SnackbarHost // Contenedor visual para mostrar mensajes emergentes (Snackbars)
import androidx.compose.material3.SnackbarHostState // Estado que gestiona la cola y visualización de los Snackbars
import androidx.compose.material3.Surface // Superficie plana que aplica color de fondo y forma a sus elementos hijos
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.runtime.Composable // Anotación que indica que la función construye la UI con Jetpack Compose
import androidx.compose.runtime.getValue // Delegado para obtener el valor de un estado mediante la palabra clave 'by'
import androidx.compose.runtime.remember // Función para memorizar objetos a través de las recomposiciones
import androidx.compose.runtime.rememberCoroutineScope // Crea y recuerda un alcance de corrutinas vinculado a la composición
import androidx.compose.ui.Alignment // Importación para definir la alineación de elementos en contenedores
import androidx.compose.ui.Modifier // Modificador de UI base de Jetpack Compose
import androidx.compose.ui.platform.LocalContext // Proporciona acceso al Contexto actual de Android
import androidx.compose.ui.text.font.FontWeight // Propiedad para controlar el grosor de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación del texto (Center, Start, etc.)
import androidx.compose.ui.unit.dp // Unidad de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta el estado de un Flow de forma consciente del ciclo de vida
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Modelo de datos que representa un registro de aporte guardado
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum con los tipos de registro (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de la barra superior reutilizable con botón de retorno
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul // Función de colores personalizados para los chips seleccionados
import com.duocuc.corpalivapp.ui.viewmodel.DashboardViewModel // ViewModel que maneja la lógica de negocio del Dashboard
import com.duocuc.corpalivapp.ui.viewmodel.FiltroDashboard // Enum con los tipos de filtros disponibles (TODOS, PERSONAS, EMPRESAS)
import com.duocuc.corpalivapp.util.abrirWhatsApp // Función de utilidad para abrir la aplicación WhatsApp con un mensaje prefijado
import kotlinx.coroutines.launch // Extensión para iniciar corrutinas de manera asíncrona
import java.text.SimpleDateFormat // Clase para formatear marcas de tiempo (Long) a fechas legibles
import java.util.Date // Clase Date para la conversión de milisegundos a objeto fecha
import java.util.Locale // Especificación de localización para el formato de fechas en español de Chile

/**
- G12: DASHBOARD (administradores)

- Muestra los aportes registrados (personas que aportan y empresas) con todos los datos que entregaron

- y un botón para contactarlos por WhatsApp. Los datos se leen del DashboardViewModel.

 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación para permitir el uso de componentes experimentales de Material 3
@Composable
fun DashboardScreen( // Pantalla principal del panel de administración/Dashboard
    viewModel: DashboardViewModel, // Instancia del ViewModel que provee el estado del Dashboard
    onBack: () -> Unit = {} // Lambda para regresar a la pantalla anterior
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle() // Recolecta el estado del Dashboard observando el ciclo de vida
    val contexto = LocalContext.current // Obtiene la referencia al Contexto de la aplicación Android
    val snackbarHostState = remember { SnackbarHostState() } // Instancia memorizada para la gestión de mensajería emergente
    val scope = rememberCoroutineScope() // CoroutineScope asociado al ciclo de vida del composable

    Scaffold( // Estructura base de la pantalla
        topBar = { AppTopBar(titulo = "Dashboard", onBack = onBack) }, // Barra superior con título "Dashboard" y acción para volver
        snackbarHost = { SnackbarHost(snackbarHostState) } // Host configurado para mostrar notificaciones de Snackbar
    ) { innerPadding -> // Recibe las dimensiones del margen de la barra superior
        LazyColumn( // Lista de desplazamiento vertical eficiente para los elementos del panel
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio de la pantalla
                .padding(innerPadding), // Aplica el padding asignado por el Scaffold
            contentPadding = PaddingValues(16.dp), // Define los márgenes internos de la lista en 16dp
            verticalArrangement = Arrangement.spacedBy(12.dp) // Asigna 12dp de separación entre cada elemento de la lista
        ) {
            item { // Elemento estático: Título principal
                Text(
                    text = "Aportes registrados", // Encabezado de la lista
                    style = MaterialTheme.typography.headlineSmall, // Estilo tipográfico para título mediano
                    fontWeight = FontWeight.Bold, // Grosor de letra en negrita
                    color = MaterialTheme.colorScheme.primary // Color primario del tema
                )
            }

            // Totales por tipo de aporte
            item { // Elemento estático: Fila con el resumen de métricas numéricas
                Row(
                    modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado de 8dp entre cada tarjeta
                ) {
                    TarjetaTotal("Total", estado.totalPersonas + estado.totalEmpresas, Modifier.weight(1f)) // Tarjeta para la suma global
                    TarjetaTotal("Personas", estado.totalPersonas, Modifier.weight(1f)) // Tarjeta para el conteo de personas
                    TarjetaTotal("Empresas", estado.totalEmpresas, Modifier.weight(1f)) // Tarjeta para el conteo de empresas
                }
            }

            // Filtros: el elegido se resalta en azul
            item { // Elemento estático: Fila de chips para filtrar la lista
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Contenedor horizontal con separación de 8dp
                    FiltroDashboard.entries.forEach { filtro -> // Itera sobre las opciones del enum FiltroDashboard
                        val elegido = filtro == estado.filtro // Evalúa si la opción es la seleccionada actualmente

                        FilterChip( // Componente Chip interactivo para filtrado
                            selected = elegido, // Marca el estado de selección
                            onClick = { viewModel.cambiarFiltro(filtro) }, // Actualiza el filtro en el ViewModel al presionar
                            label = { // Contenido del texto dentro del chip
                                Text(
                                    text = filtro.etiqueta, // Texto descriptivo del filtro
                                    fontWeight = if (elegido) FontWeight.Bold else FontWeight.Normal // Resalta con negrita si está activo
                                )
                            },
                            colors = coloresChipAzul() // Aplica la paleta de colores azul personalizada
                        )
                    }
                }
            }

            when { // Evalúa las condiciones para el contenido principal de la lista
                estado.cargando -> item { // Si el estado está cargando datos
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp), // Contenedor centrado con margen de 32dp
                        contentAlignment = Alignment.Center // Alinea el contenido estrictamente al centro
                    ) {
                        CircularProgressIndicator() // Muestra la rueda animada de progreso
                    }
                }

                estado.registros.isEmpty() -> item { // Si no existen aportes que coincidan con el filtro
                    Text(
                        text = "Aún no hay aportes registrados.", // Texto descriptivo cuando la lista está vacía
                        style = MaterialTheme.typography.bodyLarge, // Tipografía de cuerpo grande
                        textAlign = TextAlign.Center, // Texto centrado horizontalmente
                        modifier = Modifier.fillMaxWidth().padding(32.dp) // Ocupa el ancho total con espacio interno
                    )
                }

                else -> items(estado.registros, key = { it.id }) { registro -> // Genera la lista dinámica de tarjetas optimizada por clave única
                    TarjetaAporte( // Dibuja la tarjeta con la información detallada del aporte
                        registro = registro, // Objeto con la información guardada
                        onContactar = { // Acción para iniciar contacto con la persona o empresa
                            val mensaje = "Hola ${registro.nombre}, te escribimos desde CORPALIV por tu " + // Redacción del mensaje automático
                                    "registro en «${registro.oportunidad}». ¡Gracias por querer colaborar!"

                            if (!abrirWhatsApp(contexto, registro.telefono, mensaje)) { // Intenta abrir WhatsApp con el número y mensaje
                                scope.launch { // Si falla la apertura, dispara una corrutina para mostrar la advertencia
                                    snackbarHostState.showSnackbar("No se pudo abrir WhatsApp") // Notifica que no se pudo iniciar la app de mensajería
                                }
                            }
                        }
                    )
                }
            }

            item { // Elemento estático: Nota aclaratoria sobre la persistencia local de datos
                Text(
                    text = "Los aportes se guardan solo en este dispositivo.", // Mensaje informativo
                    style = MaterialTheme.typography.bodySmall, // Estilo tipográfico pequeño
                    color = MaterialTheme.colorScheme.outline // Color atenuado (Outline)
                )
            }
        }
    }
}

/** Número grande con su etiqueta (total, personas o empresas). */
@Composable
private fun TarjetaTotal( // Componente privado para renderizar una tarjeta con una cifra contable
    etiqueta: String, // Título descriptivo de la métrica (ej: "Total")
    valor: Int, // Cantidad numérica a mostrar
    modifier: Modifier = Modifier // Modificador de diseño
) {
    OutlinedCard(modifier = modifier) { // Tarjeta con contorno
        Column(
            modifier = Modifier
                .fillMaxWidth() // Ocupa el ancho total asignado por el peso (weight)
                .padding(vertical = 12.dp), // Margen interno de 12dp arriba y abajo
            horizontalAlignment = Alignment.CenterHorizontally // Centra los textos horizontalmente
        ) {
            Text(
                text = valor.toString(), // Convierte el número entero a String
                style = MaterialTheme.typography.headlineMedium, // Estilo tipográfico para la cifra grande
                fontWeight = FontWeight.Bold // Fuente en negrita
            )
            Text(text = etiqueta, style = MaterialTheme.typography.labelMedium) // Muestra la etiqueta identificadora abajo
        }
    }
}

/** Un aporte con todos sus datos; las empresas muestran además los datos legales y comerciales. */
@Composable
private fun TarjetaAporte( // Componente privado para renderizar los detalles completos de un aporte guardado
    registro: RegistroGuardado, // Instancia con los datos del registro
    onContactar: () -> Unit // Callback al hacer clic en el botón de WhatsApp
) {
    val esEmpresa = registro.tipo == TipoRegistro.EMPRESA.name // Evalúa si el registro corresponde a una empresa
    val fecha = remember(registro.id) { // Formatea y memoriza la fecha calculada desde el ID (timestamp)
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(registro.id)) // Convierte los milisegundos a formato dd/MM/yyyy HH:mm
    }

    OutlinedCard(modifier = Modifier.fillMaxWidth()) { // Tarjeta contenedora principal con borde
        Column(
            modifier = Modifier.padding(16.dp), // Padding interno de 16dp
            verticalArrangement = Arrangement.spacedBy(6.dp) // Espaciado constante de 6dp entre filas
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
                verticalAlignment = Alignment.Top // Alinea los elementos al borde superior
            ) {
                Text(
                    text = registro.nombre, // Nombre de la persona o Razón Social
                    style = MaterialTheme.typography.titleMedium, // Estilo de título mediano
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    modifier = Modifier.weight(1f) // Expande el nombre usando el peso disponible
                )
                Spacer(modifier = Modifier.width(8.dp)) // Separación de 8dp
                Surface(
                    shape = RoundedCornerShape(8.dp), // Esquinas redondeadas de 8dp
                    color = MaterialTheme.colorScheme.secondaryContainer // Color de fondo para destacar la categoría
                ) {
                    Text(
                        text = if (esEmpresa) "Empresa" else "Persona", // Badge que diferencia el tipo de aportante
                        style = MaterialTheme.typography.labelMedium, // Estilo de etiqueta mediana
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp) // Padding interno del badge
                    )
                }
            }

            Dato("Oportunidad", registro.oportunidad) // Muestra la oportunidad o programa seleccionado
            Dato("Tipo de aporte", registro.tipoAporte) // Muestra la modalidad o motivo de la donación/aporte
            Dato(if (esEmpresa) "RUT de la empresa" else "RUN", registro.rut) // Identificación tributaria o personal

            if (esEmpresa) { // Despliega datos adicionales requeridos exclusivamente para empresas
                Dato("Giro", registro.giro) // Giro comercial
                Dato("Dirección comercial", registro.direccion) // Dirección tributaria/comercial
                Dato("Representante legal", registro.representante) // Nombre del representante legal
                Dato("RUT del representante", registro.rutRepresentante) // RUT del representante legal
            }

            Dato("Correo", registro.correo) // Correo de contacto
            Dato("Teléfono", registro.telefono) // Teléfono registrado
            Dato("Registrado", fecha) // Fecha y hora exacta de realización del registro

            Button(
                onClick = onContactar, // Invoca el callback de contacto al ser presionado
                modifier = Modifier
                    .fillMaxWidth() // Ancho completo
                    .padding(top = 8.dp) // Margen de 8dp arriba
            ) {
                Text("Contactar Whatsapp") // Texto descriptivo del botón
            }
        }
    }
}

/** Fila "Etiqueta: valor". */
@Composable
private fun Dato(etiqueta: String, valor: String) { // Componente auxiliar para estructurar pares clave-valor en filas
    Row { // Contenedor horizontal
        Text(
            text = "$etiqueta: ", // Muestra la clave en negrita seguida de dos puntos
            style = MaterialTheme.typography.bodyMedium, // Estilo de texto de cuerpo medio
            fontWeight = FontWeight.Bold // Fuente en negrita para la etiqueta
        )
        Text(
            text = valor, // Muestra el valor asignado
            style = MaterialTheme.typography.bodyMedium, // Estilo de texto de cuerpo medio
            modifier = Modifier.weight(1f) // Expande el texto para que ocupe el resto de la fila sin desbordarse
        )
    }
}