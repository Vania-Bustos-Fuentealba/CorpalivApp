package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import android.app.Application // Importación de la clase Application de Android para acceder al contexto global y recursos de assets
import androidx.lifecycle.AndroidViewModel // Clase base de ViewModel especializada en mantener referencia al contexto de la aplicación
import androidx.lifecycle.viewModelScope // Alcance de corrutinas acotado al ciclo de vida del ViewModel
import kotlinx.coroutines.Dispatchers // Objeto que provee los despachadores de hilos (IO, Main, Default) para corrutinas
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable en Jetpack Compose
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto hacia la vista
import kotlinx.coroutines.flow.asStateFlow // Convierte un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.launch // Extensión de corrutina para iniciar operaciones asíncronas
import kotlinx.coroutines.withContext // Cambia el contexto de ejecución del hilo dentro de una corrutina en bloque suspendido

/**
- G10: texto de la pantalla de inicio. Lee la información de Corpaliv desde assets/institucion/corpaliv.md.

- Se crea a nivel de la Activity, así el archivo se lee una sola vez.

 */
class InicioViewModel(application: Application) : AndroidViewModel(application) { // ViewModel que administra la lectura asíncrona de la información institucional inicial

    // null = todavía cargando
    private val _contenido = MutableStateFlow<String?>(null) // Estado mutable interno que almacena el texto en formato Markdown o null mientras carga
    val contenido: StateFlow<String?> = _contenido.asStateFlow() // Flujo de estado público de solo lectura para ser observado desde la interfaz de usuario

    init { // Bloque de inicialización que se ejecuta al instanciar el ViewModel
        viewModelScope.launch { // Inicia una corrutina vinculada al ciclo de vida del ViewModel
            val texto = withContext(Dispatchers.IO) { // Cambia la ejecución al hilo I/O especializado en lectura e ingreso de datos/archivos
                runCatching { // Envuelve la lectura dentro de un bloque seguro para capturar excepciones de IO
                    getApplication<Application>().assets.open(ARCHIVO) // Abre el archivo de texto almacenado en la carpeta de assets
                        .bufferedReader().use { it.readText() } // Lee todo el contenido del buffer y asegura el cierre automático de la transmisión
                }.getOrNull() // Retorna el texto leído o null si ocurrió alguna falla en la apertura o lectura del archivo
            }

            // Si el archivo no se pudo leer se muestra un aviso en lugar de dejar el cargando
            _contenido.value = texto ?: "# CORPALIV\n\nNo se pudo cargar la información institucional." // Asigna el texto recuperado o un mensaje de error por defecto
        }
    }

    private companion object { // Objeto compañero privado para definir constantes estáticas
        const val ARCHIVO = "institucion/corpaliv.md" // Ruta relativa dentro de la carpeta assets donde reside la información Markdown
    }
}