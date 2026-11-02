package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import android.Manifest // Importación de la clase de permisos del sistema Android
import android.content.pm.PackageManager // Importación de PackageManager para verificar si un permiso ha sido otorgado
import android.net.Uri // Importación de Uri para representar ubicaciones de archivos e imágenes
import androidx.activity.compose.rememberLauncherForActivityResult // Contrato de Compose para gestionar resultados de actividades (galería, cámara, permisos)
import androidx.activity.result.contract.ActivityResultContracts // Contratos estándar de Android para solicitar permisos o tomar contenidos
import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado de elementos
import androidx.compose.foundation.layout.Column // Contenedor de diseño vertical Column
import androidx.compose.foundation.layout.Row // Contenedor de diseño horizontal Row
import androidx.compose.foundation.layout.Spacer // Componente Spacer para añadir espacios vacíos
import androidx.compose.foundation.layout.fillMaxSize // Modificador para ocupar el 100% de la pantalla
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.height // Modificador para definir una altura fija
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios internos (padding)
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado de desplazamiento
import androidx.compose.foundation.shape.RoundedCornerShape // Forma con esquinas redondeadas para etiquetas y contenedores
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical
import androidx.compose.material3.Button // Componente de botón de acción principal de Material 3
import androidx.compose.material3.Card // Componente de tarjeta de Material 3 para agrupar información
import androidx.compose.material3.MaterialTheme // Acceso a colores, tipografía y formas del tema de Material Design
import androidx.compose.material3.OutlinedButton // Componente de botón delineado para acciones secundarias
import androidx.compose.material3.Scaffold // Estructura base de pantalla que soporta la barra superior y notificaciones
import androidx.compose.material3.SnackbarHost // Contenedor para mostrar mensajes emergentes (Snackbars)
import androidx.compose.material3.SnackbarHostState // Estado que gestiona los Snackbars
import androidx.compose.material3.Surface // Superficie de fondo plano que aplica color y forma
import androidx.compose.material3.Text // Componente Text de Material 3 para mostrar cadenas
import androidx.compose.material3.TextButton // Botón plano de solo texto para acciones de bajo énfasis
import androidx.compose.runtime.Composable // Anotación Composable que identifica la función como UI en Compose
import androidx.compose.runtime.getValue // Delegado de propiedad para lectura directa de estados
import androidx.compose.runtime.mutableStateOf // Función para crear estados mutables observables
import androidx.compose.runtime.remember // Función para memorizar objetos entre recomposiciones
import androidx.compose.runtime.rememberCoroutineScope // Crea un alcance de corrutinas vinculado a la composición
import androidx.compose.runtime.saveable.rememberSaveable // Memoriza valores resistiendo recreaciones de la actividad/rotación
import androidx.compose.runtime.setValue // Delegado de propiedad para escritura directa en estados
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador de UI base para configurar el aspecto y comportamiento de composables
import androidx.compose.ui.graphics.Color // Clase para definir y manipular colores
import androidx.compose.ui.platform.LocalContext // Proporciona acceso al Contexto de la aplicación Android
import androidx.compose.ui.text.font.FontWeight // Propiedad para definir el grosor de la fuente (Bold, Normal, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación del texto
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import androidx.core.content.ContextCompat // Clase de compatibilidad para consultar permisos del sistema
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un Flow de forma consciente del ciclo de vida
import com.duocuc.corpalivapp.data.model.RolUsuario // Enum que representa los roles de usuario (ADMINISTRADOR, etc.)
import com.duocuc.corpalivapp.data.model.UsuarioSesion // Modelo con los datos del usuario en la sesión activa
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable
import com.duocuc.corpalivapp.ui.components.ImagenInteligente // Componente personalizado para renderizar la foto de perfil circular o ícono por defecto
import com.duocuc.corpalivapp.ui.theme.AzulSeleccion // Color personalizado definido en el tema
import com.duocuc.corpalivapp.ui.viewmodel.PerfilViewModel // ViewModel que gestiona la foto de perfil y la persitencia de la imagen
import com.duocuc.corpalivapp.util.crearUriFoto // Función utilitaria para generar una Uri temporal mediante FileProvider
import kotlinx.coroutines.launch // Extensión para iniciar corrutinas asíncronas
import java.text.SimpleDateFormat // Formateador de fechas
import java.util.Date // Clase Date de Java
import java.util.Locale // Especificación de localización en español de Chile

/**
- GUÍA 13 - PARTE 4: PANTALLA DE PERFIL

 *
- Integra dos recursos nativos con la API:

- Galería: GetContent()

- Cámara: TakePicture() + FileProvider (requiere el permiso CAMERA, que se solicita).

- Si el usuario cancela cualquiera de las dos acciones, la app no se cae.

- G13: es el perfil de la cuenta con sesión iniciada: muestra su información general, el badge con su rol,

- la opción Dashboard (solo administradores) y el botón para cerrar sesión.

 */
@Composable
fun PerfilScreen( // Pantalla del perfil de usuario con capacidades para cambiar foto mediante cámara o galería
    viewModel: PerfilViewModel, // ViewModel para gestionar la imagen de perfil
    sesion: UsuarioSesion, // Objeto con la información de la sesión actual
    onDashboard: () -> Unit = {}, // Callback para navegar al Dashboard
    onCerrarSesion: () -> Unit = {}, // Callback para ejecutar el cierre de sesión
    onBack: () -> Unit = {} // Callback para volver a la pantalla anterior
) {
    val context = LocalContext.current // Obtiene la referencia al contexto actual de Android
    val imagenUri by viewModel.imagenUri.collectAsStateWithLifecycle() // Observa la Uri de la imagen de perfil guardada

    val snackbarHostState = remember { SnackbarHostState() } // Estado para controlar mensajes emergentes
    val scope = rememberCoroutineScope() // Alcance de corrutina para operaciones asíncronas de UI

    // Fecha y hora en que se inició la sesión
    val inicioSesion = remember(sesion.inicio) { // Formatea y memoriza la fecha de inicio de sesión
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(sesion.inicio)) // Convierte el timestamp a formato legible
    }

    // Uri donde la cámara guardará la foto; se conserva si se rota la pantalla.
    var uriCamaraPendiente by rememberSaveable { mutableStateOf<Uri?>(null) } // Guarda la Uri temporal creada para la foto capturada por la cámara

    fun avisar(mensaje: String) { // Función helper interna para desplegar Snackbars
        scope.launch { snackbarHostState.showSnackbar(mensaje) } // Lanza una corrutina para mostrar la notificación
    }

    // Lanzador de galería: devuelve null si el usuario cancela
    val galeriaLauncher = rememberLauncherForActivityResult( // Registra el contrato de actividad para la selección de archivos multimedia de la galería
        contract = ActivityResultContracts.GetContent() // Contrato estándar para elegir contenido
    ) { uri ->
        if (uri != null) viewModel.actualizarDesdeGaleria(uri) // Si se seleccionó una imagen, actualiza la Uri en el ViewModel
    }

    // Lanzador de cámara: devuelve true solo si el usuario aceptó la foto
    val camaraLauncher = rememberLauncherForActivityResult( // Registra el contrato de actividad para captura de fotos con la app de cámara
        contract = ActivityResultContracts.TakePicture() // Contrato para tomar una imagen y guardarla en la Uri provista
    ) { exito ->
        val uri = uriCamaraPendiente // Obtiene la Uri temporal previamente guardada
        if (exito && uri != null) viewModel.actualizarDesdeCamara(uri) // Si la captura fue exitosa, actualiza la Uri en el ViewModel
        uriCamaraPendiente = null // Limpia la variable temporal
    }

    fun abrirCamara() { // Función interna para preparar el archivo y lanzar la cámara del sistema
        try {
            val uri = crearUriFoto(context) // Genera una Uri mediante FileProvider en el almacenamiento de la app
            uriCamaraPendiente = uri // Guarda la Uri en la variable de estado
            camaraLauncher.launch(uri) // Lanza el intent de la cámara pasando la Uri de destino
        } catch (e: Exception) {
            avisar("No se pudo abrir la cámara en este dispositivo") // Captura excepciones si no hay cámara disponible o falla FileProvider
        }
    }

    // Lanzador del permiso de la cámara
    val permisoCamaraLauncher = rememberLauncherForActivityResult( // Registra la solicitud del permiso de sistema para usar la cámara
        contract = ActivityResultContracts.RequestPermission() // Contrato estándar para solicitar un único permiso
    ) { concedido ->
        if (concedido) abrirCamara() else avisar("Permiso de cámara denegado") // Abre la cámara si fue concedido o muestra un aviso si fue rechazado
    }

    Scaffold( // Contenedor estructural de la pantalla
        topBar = { AppTopBar(titulo = "Mi perfil", onBack = onBack) }, // Barra superior con título y botón de regreso
        snackbarHost = { SnackbarHost(snackbarHostState) } // Registra el host para mensajes de aviso
    ) { innerPadding -> // Recibe el espacio de reserva superior
        Column( // Contenedor vertical principal
            modifier = Modifier
                .fillMaxSize() // Ocupa toda la superficie
                .padding(innerPadding) // Aplica el padding del Scaffold
                .verticalScroll(rememberScrollState()) // Activa el desplazamiento vertical
                .padding(24.dp), // Margen general de 24dp
            verticalArrangement = Arrangement.Top, // Alinea el contenido desde la parte superior
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
        ) {
            ImagenInteligente(uri = imagenUri) // Renderiza la foto de perfil en forma circular o el ícono por defecto

            Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical de 16dp

            // Nombre de la cuenta y, justo debajo, el badge con su rol
            Text( // Nombre del usuario
                text = sesion.nombre, // Muestra el nombre provisto por la sesión
                style = MaterialTheme.typography.titleLarge, // Estilo tipográfico para título grande
                fontWeight = FontWeight.Bold, // Texto en negrita
                textAlign = TextAlign.Center // Centrado
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical de 8dp

            Surface( // Badge visual para mostrar el rol del usuario
                shape = RoundedCornerShape(50), // Esquinas completamente redondeadas
                color = AzulSeleccion // Fondo en azul de selección
            ) {
                Text( // Texto del rol de usuario
                    text = sesion.rol.etiqueta, // Muestra la etiqueta textual del rol (ej: "Administrador")
                    style = MaterialTheme.typography.labelLarge, // Estilo de etiqueta grande
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    color = Color.White, // Color del texto en blanco
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp) // Padding interno del badge
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical de 24dp

            // Información general de la cuenta
            Card(modifier = Modifier.fillMaxWidth()) { // Tarjeta informativa con los datos de la cuenta
                Column(
                    modifier = Modifier.padding(16.dp), // Margen interno de 16dp
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado de 8dp entre líneas
                ) {
                    Text( // Título de la tarjeta
                        text = "Información general",
                        style = MaterialTheme.typography.titleMedium, // Estilo de título mediano
                        fontWeight = FontWeight.Bold // Negrita
                    )
                    DatoCuenta("Correo", sesion.correo) // Muestra la fila del correo
                    DatoCuenta("Rol", sesion.rol.etiqueta) // Muestra la fila del rol
                    DatoCuenta("Organización", "CORPALIV") // Muestra la organización
                    DatoCuenta("Sesión iniciada", inicioSesion) // Muestra la fecha y hora de inicio de sesión
                }
            }

            // Solo los administradores ven la opción para revisar los datos de los usuarios
            if (sesion.rol == RolUsuario.ADMINISTRADOR) { // Renderiza condicionalmente las funciones de administrador
                Spacer(modifier = Modifier.height(16.dp)) // Espacio de 16dp

                Button( // Botón para acceder al panel de Dashboard
                    onClick = onDashboard, // Invoca el callback de navegación al Dashboard
                    modifier = Modifier.fillMaxWidth() // Expande el botón a todo el ancho
                ) {
                    Text("Dashboard") // Etiqueta del botón
                }
                Text( // Bajada explicativa del botón Dashboard
                    text = "Revisa los datos de los usuarios que aportaron.",
                    style = MaterialTheme.typography.bodySmall, // Estilo pequeño
                    color = MaterialTheme.colorScheme.outline, // Color atenuado (Outline)
                    modifier = Modifier.padding(top = 4.dp) // Espacio de 4dp arriba
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical de 16dp

            Text( // Texto del estado de la imagen de perfil
                text = if (imagenUri == null) "Aún no has elegido una foto" else "Tu foto de perfil", // Informa si el usuario ya cuenta con foto elegida
                style = MaterialTheme.typography.titleMedium, // Estilo título mediano
                textAlign = TextAlign.Center // Centrado
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espacio vertical de 32dp

            Button( // Botón para elegir una foto existente desde la galería
                onClick = { galeriaLauncher.launch("image/*") }, // Lanza la búsqueda de archivos tipo imagen
                modifier = Modifier.fillMaxWidth() // Ancho completo
            ) {
                Text("Elegir de la galería") // Texto del botón
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espacio vertical de 12dp

            Button( // Botón para capturar una foto utilizando la cámara del dispositivo
                onClick = {
                    val permisoConcedido = ContextCompat.checkSelfPermission( // Verifica si la app cuenta con el permiso CAMERA otorgado
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (permisoConcedido) {
                        abrirCamara() // Abre la cámara directamente si el permiso ya está concedido
                    } else {
                        permisoCamaraLauncher.launch(Manifest.permission.CAMERA) // Solicita el permiso CAMERA al usuario en caso contrario
                    }
                },
                modifier = Modifier.fillMaxWidth() // Ancho completo
            ) {
                Text("Tomar foto con la cámara") // Texto del botón
            }

            if (imagenUri != null) { // Si ya hay una foto guardada, muestra la opción para eliminarla
                Spacer(modifier = Modifier.height(12.dp)) // Espacio vertical de 12dp
                OutlinedButton( // Botón delineado para eliminar la imagen activa
                    onClick = { viewModel.quitarImagen() }, // Llama al método del ViewModel para eliminar la preferencia de la foto
                    modifier = Modifier.fillMaxWidth() // Ancho completo
                ) {
                    Text("Quitar foto") // Texto del botón
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical de 24dp

            TextButton(onClick = onCerrarSesion) { // Botón de solo texto para cerrar la sesión
                Text("Cerrar sesión") // Texto de acción de cierre de sesión
            }
        }
    }
}

/** Fila "Etiqueta: valor" de la información de la cuenta. */
@Composable
private fun DatoCuenta(etiqueta: String, valor: String) { // Componente auxiliar privado para formatear los datos clave-valor de la cuenta
    Row { // Contenedor horizontal
        Text(
            text = "$etiqueta: ", // Clave del dato en negrita
            style = MaterialTheme.typography.bodyMedium, // Estilo de cuerpo medio
            fontWeight = FontWeight.Bold // Fuente en negrita
        )
        Text(
            text = valor, // Valor del dato
            style = MaterialTheme.typography.bodyMedium, // Estilo de cuerpo medio
            modifier = Modifier.weight(1f) // Expande el texto para tomar el resto de la fila
        )
    }
}