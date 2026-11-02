package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece este archivo dentro de la capa de interfaz de usuario (UI)

import android.net.Uri // Importación de la clase Uri para manejar referencias a imágenes de perfil
import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado de los elementos en pantalla
import androidx.compose.foundation.layout.Box // Importación del contenedor Box para alineación y superposición de elementos
import androidx.compose.foundation.layout.Column // Importación del contenedor de diseño vertical Column
import androidx.compose.foundation.layout.fillMaxSize // Modificador para que el contenedor ocupe el 100% del tamaño disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o padding interno/externo
import androidx.compose.foundation.layout.widthIn // Modificador para restringir el ancho mínimo y máximo de un composable
import androidx.compose.foundation.layout.width // Modificador para definir un ancho específico a un componente
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado del desplazamiento de una lista o columna
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical en un contenedor
import androidx.compose.material.icons.Icons // Importación del contenedor global de íconos de Material Design
import androidx.compose.material.icons.filled.Menu // Importación del ícono de menú de hamburguesa
import androidx.compose.material3.CircularProgressIndicator // Componente indicador de carga circular de Material 3
import androidx.compose.material3.DrawerValue // Enumeración que representa los estados del menú lateral (Open o Closed)
import androidx.compose.material3.ExperimentalMaterial3Api // Anotación para permitir el uso de APIs experimentales de Material 3
import androidx.compose.material3.Icon // Componente Icon de Material 3 para desplegar vectores
import androidx.compose.material3.IconButton // Componente de botón con ícono para acciones de la barra superior
import androidx.compose.material3.MaterialTheme // Acceso a la paleta de colores, tipografía y formas del tema Material
import androidx.compose.material3.ModalDrawerSheet // Hoja contenedora para el contenido del menú lateral modal
import androidx.compose.material3.ModalNavigationDrawer // Menú lateral deslizable modal para pantallas pequeñas/medianas
import androidx.compose.material3.PermanentDrawerSheet // Hoja contenedora fija para el menú lateral en pantallas grandes
import androidx.compose.material3.PermanentNavigationDrawer // Menú lateral permanente siempre visible para pantallas expandidas
import androidx.compose.material3.Scaffold // Estructura base de pantalla que gestiona ranuras para barras superiores y contenido
import androidx.compose.material3.Text // Componente Text para renderizar cadenas de texto
import androidx.compose.material3.TopAppBar // Componente de la barra de navegación superior de Material 3
import androidx.compose.material3.TopAppBarDefaults // Configuración de colores predeterminados para la barra superior
import androidx.compose.material3.rememberDrawerState // Función para recordar el estado (abierto/cerrado) del NavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass // Clase que clasifica el ancho de la pantalla (Compact, Medium, Expanded)
import androidx.compose.runtime.Composable // Anotación Composable para identificar funciones de UI en Jetpack Compose
import androidx.compose.runtime.rememberCoroutineScope // Crea un alcance de corrutinas vinculado al ciclo de vida del composable
import androidx.compose.ui.Alignment // Importación para alinear elementos dentro de sus contenedores
import androidx.compose.ui.Modifier // Modificador de UI base para configurar el aspecto y comportamiento de composables
import androidx.compose.ui.text.font.FontWeight // Propiedad para definir el grosor de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.tooling.preview.Preview // Anotación para generar previsualizaciones en Android Studio
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import com.duocuc.corpalivapp.navigation.Screen // Importación de la sealed class Screen para manejar destinos de navegación
import com.duocuc.corpalivapp.ui.components.AppDrawerContent // Componente con la lista de opciones y cabecera del menú lateral
import com.duocuc.corpalivapp.ui.components.MarkdownEnBloques // Componente personalizado para renderizar texto Markdown agrupado en tarjetas
import com.duocuc.corpalivapp.util.obtenerWindowSizeClass // Función utilitaria para calcular la categoría de tamaño de la ventana actual
import kotlinx.coroutines.launch // Extensión para ejecutar corrutinas de manera asíncrona
import kotlin.OptIn // Anotación de Kotlin para permitir APIs experimentales

// 1. VISTA COMPACTA
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de APIs experimentales requeridas por TopAppBar
@Composable
fun HomeScreenCompacta( // Composable para la vista de la pantalla principal optimizada para teléfonos móviles (Compact)
    contenido: String? = TEXTO_MUESTRA, // Texto Markdown a renderizar; por defecto asigna el texto de muestra
    onMenuClick: (() -> Unit)? = null // Callback opcional para abrir el menú lateral modal
) {
    Scaffold( // Contenedor estructural básico
        topBar = { // Barra de navegación superior para pantallas móviles
            TopAppBar(
                title = { Text("CORPALIV Mobile", fontWeight = FontWeight.Bold) }, // Título de la barra en negrita
                navigationIcon = { BotonMenu(onMenuClick) }, // Ícono de hamburguesa en el extremo izquierdo
                colors = TopAppBarDefaults.topAppBarColors( // Asigna colores específicos para el contenedor primario
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding -> // Recibe el padding de margen superior e inferior del Scaffold
        Column( // Contenedor vertical desplazable
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el tamaño disponible
                .padding(innerPadding) // Respeta el espacio reservado para la barra superior
                .verticalScroll(rememberScrollState()) // Activa el desplazamiento vertical
                .padding(16.dp), // Aplica un margen general de 16dp
            verticalArrangement = Arrangement.spacedBy(16.dp), // Espaciado de 16dp entre elementos
            horizontalAlignment = Alignment.CenterHorizontally // Centra los componentes horizontalmente
        ) {
            InformacionCorpaliv(contenido) // Llama a la función que renderiza el Markdown o la barra de carga
        }
    }
}

// ======================================================================================
// 2. VISTA MEDIANA (Tablets pequeñas / Dispositivos plegables)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de APIs experimentales requeridas por TopAppBar
@Composable
fun HomeScreenMediana( // Composable optimizado para dispositivos plegables y tablets en posición vertical (Medium)
    contenido: String? = TEXTO_MUESTRA, // Texto Markdown de contenido institucional
    onMenuClick: (() -> Unit)? = null // Callback opcional para invocar la apertura del menú lateral
) {
    Scaffold( // Contenedor estructural de la pantalla
        topBar = { // Barra de navegación superior
            TopAppBar(
                title = { Text("CORPALIV - Vista Tablet / Plegable", fontWeight = FontWeight.Bold) }, // Título identificador de la vista
                navigationIcon = { BotonMenu(onMenuClick) }, // Botón para desplegar el menú modal
                colors = TopAppBarDefaults.topAppBarColors( // Asigna colores de la variante secundaria del tema
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding -> // Recibe los márgenes del Scaffold
        // El contenido se centra y no se estira más de 720 dp para que el texto sea legible
        Column( // Contenedor externo desplazable y centrado
            modifier = Modifier
                .fillMaxSize() // Expande a la pantalla completa
                .padding(innerPadding) // Aplica el padding del Scaffold
                .verticalScroll(rememberScrollState()), // Permite desplazamiento vertical
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenedor interno
        ) {
            Column( // Contenedor interno que limita el ancho máximo a 720dp para mejorar la lectura
                modifier = Modifier
                    .widthIn(max = 720.dp) // Limita el ancho horizontal a un máximo de 720dp
                    .padding(24.dp), // Padding interno de 24dp
                verticalArrangement = Arrangement.spacedBy(24.dp), // Espaciado de 24dp entre bloques
                horizontalAlignment = Alignment.CenterHorizontally // Alinea el contenido al centro
            ) {
                InformacionCorpaliv(contenido) // Renderiza el contenido institucional
            }
        }
    }
}

// ======================================================================================
// 3. VISTA EXPANDIDA (Tablets en horizontal / Pantallas grandes / Escritorio)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de APIs experimentales requeridas por TopAppBar
@Composable
fun HomeScreenExpandida(contenido: String? = TEXTO_MUESTRA) { // Composable para pantallas grandes, escritorio y tablets horizontales (Expanded)
    Scaffold( // Contenedor base
        topBar = { // Barra superior del panel expandido
            TopAppBar(
                title = { Text("CORPALIV - Panel de Administración Expandido", fontWeight = FontWeight.Bold) }, // Título de vista expandida
                colors = TopAppBarDefaults.topAppBarColors( // Utiliza el contenedor terciario para el estilo visual
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    ) { innerPadding -> // Recibe el espacio de reserva superior
        // Contenido centrado, con un ancho máximo para que el texto sea legible
        Column( // Contenedor principal desplazable
            modifier = Modifier
                .fillMaxSize() // Ocupa el espacio completo asignado al NavDrawer permanente
                .padding(innerPadding) // Aplica el padding del Scaffold
                .verticalScroll(rememberScrollState()), // Habilita el desplazamiento vertical
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            Column(modifier = Modifier.widthIn(max = 960.dp).padding(32.dp)) { // Limita el ancho máximo a 960dp para óptima lectura con padding de 32dp
                InformacionCorpaliv(contenido) // Renderiza la información institucional
            }
        }
    }
}

// Información de Corpaliv en bloques; mientras el texto carga se muestra un indicador
@Composable
private fun InformacionCorpaliv(contenido: String?) { // Composable privado encargado de evaluar la presencia del texto
    if (contenido == null) { // Si la cadena es nula (aún está cargando desde assets)
        Box( // Muestra un cuadro contenedor centrado
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho
                .padding(32.dp), // Aplica padding de 32dp
            contentAlignment = Alignment.Center // Centra el indicador
        ) {
            CircularProgressIndicator() // Despliega el indicador de progreso circular
        }
    } else {
        MarkdownEnBloques(markdown = contenido) // Renderiza el contenido estructurado en bloques y tarjetas
    }
}

// Texto de ejemplo para los @Preview (en la app el texto sale de assets/institucion/corpaliv.md)
private const val TEXTO_MUESTRA = // Constante privada con texto de formato Markdown para previsualizaciones en el IDE
    "# CORPALIV\n\nCorporación de Padres y Amigos por el Limitado Visual.\n\n" +
            "## Misión\n\nAcompañar el desarrollo de niños, niñas, jóvenes y adultos con discapacidad múltiple.\n\n" +
            "## Visión\n\nUn país sin barreras para la discapacidad múltiple.\n\n" +
            "## Valores\n\nCompromiso\n\nRespeto\n\nEmpatía\n\nSolidaridad" // Se corrige el formato de viñeta o saltos para alinearse con el parser


// ======================================================================================
// 4. CONTENEDOR ADAPTATIVO PRINCIPAL (Guía 9 + Guía 10)
// ======================================================================================

/**
- GUÍA 10 - Botón de menú (ícono de hamburguesa) de la TopAppBar.

- Si [onMenuClick] es null (menú permanente en pantallas grandes) no se muestra nada.
 */
@Composable
private fun BotonMenu(onMenuClick: (() -> Unit)?) { // Composable auxiliar para renderizar el botón de menú
    if (onMenuClick != null) { // Solo dibuja el botón si existe la función callback para abrir el Drawer
        IconButton(onClick = onMenuClick) { // Crea un botón con el ícono
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Abrir menú") // Muestra el ícono de menú de hamburguesa
        }
    }
}

/**
- GUÍA 10 - NavigationDrawer MODAL (Compact y Medium): el menú se abre desde

- el ícono de la TopAppBar o deslizando desde el borde izquierdo.

 */
@Composable
private fun HomeConMenuModal( // Encapsula la estructura del menú deslizante modal para vistas móviles y tablets
    onNavigate: (Screen) -> Unit, // Callback para ejecutar la navegación
    fotoPerfil: Uri?, // Foto de perfil del usuario activa
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit // Composable lambda de contenido que recibe la acción para abrir el menú
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Recuerda el estado del Drawer (inicia cerrado)
    val scope = rememberCoroutineScope() // Crea el alcance de corrutinas para animar la apertura/cierre del menú

    ModalNavigationDrawer( // Componente Material 3 para menús desplegables modales
        drawerState = drawerState, // Vincula el estado del Drawer
        drawerContent = { // Define el panel con las opciones del menú
            ModalDrawerSheet { // Hoja contenedora del menú modal
                AppDrawerContent( // Dibuja la cabecera e ítems de navegación del menú
                    selected = Screen.Home, // Marca la pantalla actual como Home
                    fotoPerfil = fotoPerfil, // Transmite la URI de la foto de perfil
                    onItemClick = { destino -> // Al hacer clic en un ítem
                        scope.launch { drawerState.close() } // Cierra el menú de manera asíncrona mediante corrutina
                        onNavigate(destino) // Notifica la navegación hacia la pantalla seleccionada
                    }
                )
            }
        }
    ) {
        contenido { scope.launch { drawerState.open() } } // Pasa la lambda que ejecuta la apertura del menú modal
    }
}

/**
- Contenedor adaptativo de la pantalla de inicio.

- Compact / Medium: menú lateral modal con botón de hamburguesa.

- Expanded: menú lateral permanente siempre visible.

- La navegación se delega en [onNavigate] (MainViewModel -> NavController).
 */
@Composable
fun HomeScreenAdaptive( // Composable orquestador adaptativo de la pantalla de inicio
    onNavigate: (Screen) -> Unit = {}, // Callback de navegación
    fotoPerfil: Uri? = null, // URI de la foto de perfil cargada
    contenido: String? = null // Contenido en texto Markdown
) {
    val windowSizeClass = obtenerWindowSizeClass() // Obtiene la métrica de tamaño de ventana activa del dispositivo

    // Evaluador del tamaño de ancho de ventana
    when (windowSizeClass.widthSizeClass) { // Determina la disposición visual según la clasificación del ancho
        WindowWidthSizeClass.Compact -> HomeConMenuModal(onNavigate, fotoPerfil) { abrirMenu -> // Para pantallas pequeñas/móviles
            HomeScreenCompacta(contenido = contenido, onMenuClick = abrirMenu) // Renderiza la vista compacta con menú modal
        }

        WindowWidthSizeClass.Medium -> HomeConMenuModal(onNavigate, fotoPerfil) { abrirMenu -> // Para pantallas medianas/foldables
            HomeScreenMediana(contenido = contenido, onMenuClick = abrirMenu) // Renderiza la vista mediana con menú modal
        }

        WindowWidthSizeClass.Expanded -> PermanentNavigationDrawer( // Para pantallas expandidas/escritorio
            drawerContent = { // Menú lateral permanente
                PermanentDrawerSheet(modifier = Modifier.width(280.dp)) { // Define un menú lateral fijo de 280dp de ancho
                    AppDrawerContent( // Dibuja el contenido del menú lateral
                        selected = Screen.Home,
                        fotoPerfil = fotoPerfil,
                        onItemClick = onNavigate
                    )
                }
            }
        ) {
            HomeScreenExpandida(contenido = contenido) // Renderiza la vista expandida sin botón de hamburguesa
        }
    }
}

// ======================================================================================
// PREVIEWS PARA CADA TAMAÑO DE PANTALLA (Guía 9 - Requerimiento 5)
// ======================================================================================
@Preview(name = "Pantalla Compacta (Smartphone)", widthDp = 360, heightDp = 800, showBackground = true) // Previsualización en formato Smartphone
@Composable
fun PreviewCompact() {
    HomeScreenCompacta() // Invoca la vista compacta
}

@Preview(name = "Pantalla Mediana (Tablet / Plegable)", widthDp = 700, heightDp = 800, showBackground = true) // Previsualización en formato Tablet vertical
@Composable
fun PreviewMedium() {
    HomeScreenMediana() // Invoca la vista mediana
}

@Preview(name = "Pantalla Expandida (Desktop / Tablet Horizontal)", widthDp = 1000, heightDp = 800, showBackground = true) // Previsualización en formato Desktop
@Composable
fun PreviewExpanded() {
    HomeScreenExpandida() // Invoca la vista expandida
}