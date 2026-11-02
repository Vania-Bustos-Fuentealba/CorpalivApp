package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de negocio (ViewModel)

import android.app.Application // Importación de la clase Application de Android para proveer el contexto global
import androidx.lifecycle.AndroidViewModel // Clase base de ViewModel con acceso al contexto de la aplicación
import androidx.lifecycle.viewModelScope // Alcance de corrutinas vinculado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.local.EstadoDataStore // Gestor de almacenamiento en DataStore para las preferencias de estado
import kotlinx.coroutines.Job // Clase que representa una tarea asíncrona cancelable o manejable en corrutinas
import kotlinx.coroutines.delay // Función de suspensión para pausar la ejecución de la corrutina durante un tiempo definido
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto hacia la UI
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow de solo lectura
import kotlinx.coroutines.flow.first // Operador para obtener el primer elemento emitido por un Flow de forma suspendida
import kotlinx.coroutines.launch // Extensión para iniciar la ejecución de una corrutina

/**
- G12: estado del "modo especial" con persistencia en DataStore.

 */
class EstadoViewModel(application: Application) : AndroidViewModel(application) { // ViewModel que gestiona la preferencia persistente del Modo Especial

    private val estadoDataStore = EstadoDataStore(application) // Instancia del gestor DataStore inicializada con el contexto de la aplicación

    // La UI muestra el CircularProgressIndicator
    private val _activo = MutableStateFlow<Boolean?>(null) // Estado mutable interno que representa el valor activo (true, false o null durante la carga)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow() // Flujo de estado público de solo lectura para observar la activación del modo especial

    // Controla el mensaje animado de "estado guardado"
    private val _mostrarMensaje = MutableStateFlow(false) // Estado mutable interno para activar o desactivar la visibilidad del mensaje flotante de confirmación
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow() // Flujo de estado público de solo lectura para observar la visibilidad del mensaje

    private var jobMensaje: Job? = null // Referencia a la corrutina del temporizador del mensaje para poder cancelarla si hay clics consecutivos

    init {
        cargarEstado() // Llama a la carga inicial de datos desde DataStore al instanciar el ViewModel
    }

    /** Carga el estado guardado; el delay simula una carga lenta para ver el loader. */
    private fun cargarEstado() { // Función privada que lee la preferencia guardada de forma asíncrona
        viewModelScope.launch { // Inicia la corrutina dentro del ciclo de vida del ViewModel
            delay(1500) // Introduce un retardo de 1.5 segundos para simular tiempo de lectura y permitir ver la animación de carga
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false // Obtiene el valor guardado en DataStore o asigna false por defecto
        }
    }

    /** Se Invierte el estado, lo guarda en DataStore y muestra el mensaje de confirmación. */
    fun alternarEstado() { // Método invocado por la UI para conmutar el estado del Modo Especial
        viewModelScope.launch { // Ejecuta la persistencia y actualización en una corrutina
            // Si aún está cargando no se hace nada
            val actual = _activo.value ?: return@launch // Cancela la operación si el estado actual es null (aún cargando)
            val nuevoValor = !actual // Invierte el valor booleano actual

            estadoDataStore.guardarEstado(nuevoValor) // Guarda el nuevo estado de forma persistente en DataStore
            _activo.value = nuevoValor // Actualiza el estado observable de la UI

            mostrarConfirmacion() // Llama al temporizador para desplegar el aviso visual de confirmación
        }
    }

    // Si se presiona varias veces seguidas, se reinicia el temporizador del mensaje.
    private fun mostrarConfirmacion() { // Gestiona el despliegue del mensaje temporal de éxito
        jobMensaje?.cancel() // Cancela cualquier temporizador previo en ejecución para reiniciar el conteo
        jobMensaje = viewModelScope.launch { // Asigna e inicia una nueva corrutina de conteo
            _mostrarMensaje.value = true // Activa la visibilidad del mensaje
            delay(2000) // Mantiene el mensaje visible durante 2 segundos
            _mostrarMensaje.value = false // Oculta el mensaje
        }
    }
}