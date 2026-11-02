package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado de los elementos
import androidx.compose.foundation.layout.Box // Importación del contenedor Box para superponer o alinear componentes en el centro
import androidx.compose.foundation.layout.Column // Importación del contenedor de diseño vertical Column
import androidx.compose.foundation.layout.Row // Importación del contenedor de diseño horizontal Row
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios de relleno (padding)
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado de desplazamiento
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical en un contenedor
import androidx.compose.material3.CircularProgressIndicator // Componente de rueda de carga animada de Material 3
import androidx.compose.material3.ExperimentalMaterial3Api // Anotación para habilitar el uso de APIs experimentales de Material 3
import androidx.compose.material3.FilterChip // Componente de chip interactivo para selección de filtros
import androidx.compose.material3.MaterialTheme // Acceso a las propiedades del tema (colores, tipografías, formas)
import androidx.compose.material3.Scaffold // Estructura base de pantalla que gestiona barras superiores e inferiores
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.runtime.Composable // Anotación que identifica la función como un componente de UI en Jetpack Compose
import androidx.compose.runtime.getValue // Delegado para la lectura simplificada del estado de Compose
import androidx.compose.runtime.key // Función para forzar la recreación/reinicio de un bloque composable según una clave
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador base para alterar la apariencia y comportamiento de composables
import androidx.compose.ui.text.font.FontWeight // Propiedad para especificar el peso de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para definir la alineación horizontal del texto
import androidx.compose.ui.tooling.preview.Preview // Anotación para generar vistas previas dentro de Android Studio
import androidx.compose.ui.unit.dp // Unidad de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida
import androidx.lifecycle.viewmodel.compose.viewModel // Extensión para inyectar u obtener ViewModels en composables
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable con botón de retorno
import com.duocuc.corpalivapp.ui.components.MarkdownEnBloques // Componente personalizado para renderizar texto Markdown agrupado
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul // Función que retorna el esquema de colores personalizado para los chips
import com.duocuc.corpalivapp.ui.viewmodel.InstitucionUiState // Modelo de estado de UI para la pantalla de institución
import com.duocuc.corpalivapp.ui.viewmodel.InstitucionViewModel // ViewModel encargado de gestionar la lógica de esta pantalla
import com.duocuc.corpalivapp.ui.viewmodel.SeccionInstitucion // Enum con las secciones institucionales (Escuela Jean Van Dijk, Alba Lab)

/**
- G10: INFORMACIÓN INSTITUCIONAL

- Muestra a quién está dirigida la plataforma. Incluye la barra inferior

- Ahora la barra inferior la muestra AppNavigation y el texto de cada sección se lee de un .md en assets/institucion.

- Las dos secciones (Escuela Jan Van Dijk y Alba Lab) van una a cada costado y se muestran en bloques.

- Tiene una barra superior con una flecha para volver al inicio.

 */
@Composable
fun InstitucionScreen( // Componente composable principal de la pantalla Institución vinculado a su ViewModel
    viewModel: InstitucionViewModel = viewModel(), // Instancia del ViewModel inyectada por defecto
    onBack: () -> Unit = {} // Lambda de callback ejecutada al presionar el botón de regresar
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle() // Observa el estado UI en tiempo real respetando el ciclo de vida

    InstitucionContenido( // Invoca la función puramente visual de la pantalla pasando el estado actual
        estado = estado,
        onSeleccion = viewModel::seleccionar, // Pasa la referencia de la función para cambiar de sección activa
        onBack = onBack
    )
}

/** Parte visual sin ViewModel, para poder verla en el @Preview. */
@OptIn(ExperimentalMaterial3Api::class) // Consiente el uso de componentes experimentales de Material 3
@Composable
private fun InstitucionContenido( // Componente privado desacoplado del ViewModel para facilitar previsualizaciones
    estado: InstitucionUiState, // Estado con la sección seleccionada y el contenido Markdown cargado
    onSeleccion: (SeccionInstitucion) -> Unit, // Callback notificador de cambio de sección
    onBack: () -> Unit // Callback para retroceder
) {
    Scaffold( // Estructura base de pantalla
        topBar = { AppTopBar(titulo = "Institución", onBack = onBack) } // Barra superior con título "Institución" y flecha de retorno
    ) { innerPadding -> // Recibe el padding de margen superior provisto por el Scaffold
        Column( // Contenedor vertical principal
            modifier = Modifier
                .fillMaxSize() // Expande la columna a toda la superficie
                .padding(innerPadding) // Aplica el padding de reserva del Scaffold
                .padding(horizontal = 16.dp, vertical = 16.dp) // Aplica márgenes externos de 16dp
        ) {
            Text( // Encabezado de la pantalla
                text = "Quiénes somos", // Título de la sección
                style = MaterialTheme.typography.headlineMedium, // Estilo de titular mediano
                fontWeight = FontWeight.Bold, // Grosor de letra en negrita
                color = MaterialTheme.colorScheme.primary, // Color primario del tema
                textAlign = TextAlign.Center, // Texto centrado
                modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho horizontal
            )

            // Selector de sección: una a cada costado; la elegida se resalta en azul
            Row( // Contenedor horizontal para distribuir los botones/chips de sección
                modifier = Modifier
                    .fillMaxWidth() // Ocupa el ancho completo
                    .padding(vertical = 12.dp), // Margen vertical de 12dp
                horizontalArrangement = Arrangement.SpaceBetween // Separa los elementos al extremo izquierdo y derecho
            ) {
                SeccionInstitucion.entries.forEach { seccion -> // Itera sobre cada opción del enum SeccionInstitucion
                    val elegida = seccion == estado.seccion // Evalúa si la sección es la actualmente seleccionada

                    FilterChip( // Componente chip seleccionable
                        selected = elegida, // Marca si está seleccionado
                        onClick = { onSeleccion(seccion) }, // Ejecuta el callback al presionar el chip
                        label = { // Etiqueta del chip
                            Text(
                                text = seccion.etiqueta, // Texto descriptivo de la sección
                                fontWeight = if (elegida) FontWeight.Bold else FontWeight.Normal // Texto en negrita si está seleccionado
                            )
                        },
                        colors = coloresChipAzul() // Aplica el estilo de color azul personalizado
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) { // Contenedor flexible que toma el resto del espacio disponible
                when {
                    estado.error -> Text( // Muestra mensaje en caso de falla de lectura de archivos assets
                        text = "No se pudo cargar la información. Intenta nuevamente.", // Mensaje de error descriptivo
                        color = MaterialTheme.colorScheme.error, // Color de error del tema
                        style = MaterialTheme.typography.bodyLarge, // Estilo tipográfico grande
                        modifier = Modifier.align(Alignment.Center) // Centra el texto dentro del Box
                    )

                    estado.contenido == null -> CircularProgressIndicator( // Muestra la rueda de carga mientras se lee el archivo
                        modifier = Modifier.align(Alignment.Center) // Centra el indicador dentro del Box
                    )

                    // key: al cambiar de sección el texto vuelve al inicio
                    else -> key(estado.seccion) { // Fuerza el reinicio del ScrollState al cambiar la clave de sección
                        MarkdownEnBloques( // Renderiza el texto Markdown en tarjetas estructuradas
                            markdown = estado.contenido, // Cadena Markdown provista por el estado
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()) // Activa el desplazamiento vertical
                                .padding(bottom = 16.dp) // Aplica margen inferior de 16dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true) // Habilita la previsualización en el entorno de desarrollo
@Composable
fun InstitucionScreenPreview() { // Composable para renderizar la vista previa de InstitucionScreen
    InstitucionContenido( // Invoca la parte visual sin dependencias de ViewModel
        estado = InstitucionUiState(
            contenido = "# Corpaliv\n\nTexto de ejemplo con **negrita**.\n\nPrimer punto\n\nSegundo punto"

        ),
        onSeleccion = {},
        onBack = {}
    )
}