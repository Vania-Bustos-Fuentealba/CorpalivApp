package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import android.app.Application // Importación de la clase Application de Android para acceder al contexto global y a los recursos de assets
import androidx.lifecycle.AndroidViewModel // Clase base de ViewModel especializada en mantener una referencia al contexto de la aplicación
import androidx.lifecycle.viewModelScope // Alcance de corrutinas acotado al ciclo de vida del ViewModel
import kotlinx.coroutines.Dispatchers // Objeto que provee los despachadores de hilos (IO, Main, Default) para corrutinas
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable en Jetpack Compose
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto hacia la UI
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.launch // Extensión de corrutina para iniciar operaciones asíncronas
import kotlinx.coroutines.withContext // Cambia el contexto de ejecución del hilo dentro de un bloque suspendido

/**
- G10: secciones de "Quiénes somos". Cada una lee su texto desde un .md en assets/institucion.

 */
enum class SeccionInstitucion(val etiqueta: String, val archivo: String) { // Enumeración para definir las secciones institucionales y sus rutas a archivos Markdown
    ESCUELA("Escuela Jan Van Dijk", "institucion/escuela_jan_van_dijk.md"), // Opción para la Escuela Jan Van Dijk con la ruta a su archivo correspondiente
    ALBA_LAB("Alba Lab", "institucion/alba_lab.md") // Opción para Alba Lab con la ruta a su archivo correspondiente
}

/** Sección elegida y su texto; contenido null = cargando, error = no se pudo leer el archivo. */
data class InstitucionUiState( // Clase de datos que representa el estado de la UI en la pantalla institucional
    val seccion: SeccionInstitucion = SeccionInstitucion.ESCUELA, // Sección activa seleccionada por el usuario
    val contenido: String? = null, // Contenido de texto en Markdown o null mientras se carga
    val error: Boolean = false // Flag booleano para indicar si ocurrió un error durante la lectura del archivo
)

/**
- Extiende AndroidViewModel porque necesita el contexto para abrir los archivos de assets.

 */
class InstitucionViewModel(application: Application) : AndroidViewModel(application) { // ViewModel que administra el contenido informativo de "Quiénes somos"

    private val _estado = MutableStateFlow(InstitucionUiState()) // Estado mutable interno que almacena el estado visual actual
    val estado: StateFlow<InstitucionUiState> = _estado.asStateFlow() // Flujo de estado público de solo lectura expuesto a la vista

    // Textos ya leídos, para no abrir el archivo cada vez que se cambia de sección
    private val cache = mutableMapOf<SeccionInstitucion, String>() // Mapa en memoria para almacenar en caché los textos previamente leídos

    init {
        seleccionar(SeccionInstitucion.ESCUELA) // Carga por defecto la primera sección (Escuela Jan Van Dijk) al instanciar el ViewModel
    }

    /** Muestra la sección elegida, leyendo su archivo fuera del hilo principal. */
    fun seleccionar(seccion: SeccionInstitucion) { // Método para cambiar de sección activa y cargar su contenido
        val guardado = cache[seccion] // Consulta si la sección ya está almacenada en la caché
        if (guardado != null) { // Si la sección se encuentra en memoria caché
            _estado.value = InstitucionUiState(seccion = seccion, contenido = guardado) // Emite el estado directamente usando el texto cacheado
            return // Finaliza la ejecución sin necesidad de volver a leer el archivo desde assets
        }

        _estado.value = InstitucionUiState(seccion = seccion) // Establece la nueva sección y coloca contenido en null (estado de carga)
        viewModelScope.launch { // Inicia una corrutina en el scope del ViewModel para leer el archivo en segundo plano
            val texto = withContext(Dispatchers.IO) { // Ejecuta la lectura de datos en el hilo I/O especializado
                runCatching { // Captura posibles excepciones al acceder a assets
                    getApplication<Application>().assets.open(seccion.archivo) // Abre el archivo Markdown especificado en la ruta de assets
                        .bufferedReader().use { it.readText() } // Lee todo el contenido del buffer y cierra la transmisión
                }.getOrNull() // Retorna el texto leído o null en caso de error
            }

            if (texto != null) cache[seccion] = texto // Si la lectura fue exitosa, guarda el resultado en el mapa de caché

            // Solo se actualiza si el usuario sigue en esa sección
            if (_estado.value.seccion == seccion) { // Verifica si el usuario no ha cambiado a otra sección durante la lectura
                _estado.value = InstitucionUiState( // Emite el estado final a la interfaz de usuario
                    seccion = seccion,
                    contenido = texto,
                    error = texto == null // Define error en true si no se pudo obtener el texto
                )
            }
        }
    }
}