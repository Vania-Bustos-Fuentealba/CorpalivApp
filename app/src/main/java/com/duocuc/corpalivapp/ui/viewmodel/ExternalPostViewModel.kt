
package com.duocuc.corpalivapp.ui.viewmodel // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import androidx.lifecycle.ViewModel // Importa ViewModel: conserva la lógica y el estado de una pantalla durante cambios de configuración.
import androidx.lifecycle.viewModelScope // Importa viewModelScope: limita las corrutinas al ciclo de vida del ViewModel.
import com.duocuc.corpalivapp.data.model.ExternalPost // Importa ExternalPost: modela los campos de una publicación recibida desde JSONPlaceholder.
import com.duocuc.corpalivapp.data.repository.ExternalPostRepository // Importa ExternalPostRepository: centraliza la consulta de publicaciones al servicio.
import kotlinx.coroutines.CancellationException // Importa CancellationException: distingue la cancelación de una corrutina de un error de red.
import kotlinx.coroutines.Job // Importa Job: permite saber si una carga de publicaciones sigue ejecutándose.
import kotlinx.coroutines.flow.MutableStateFlow // Importa MutableStateFlow: almacena un estado modificable y observable.
import kotlinx.coroutines.flow.StateFlow // Importa StateFlow: expone el estado observable sin permitir modificarlo desde la pantalla.
import kotlinx.coroutines.flow.asStateFlow // Importa asStateFlow: publica una vista de solo lectura del estado mutable.
import kotlinx.coroutines.launch // Importa launch: inicia una corrutina sin bloquear el hilo que la llama.

data class ExternalPostUiState( // Reúne los datos, la carga y el error que necesita dibujar la pantalla.
    val publicaciones: List<ExternalPost> = emptyList(), // La lista comienza vacía hasta recibir una respuesta.
    val cargando: Boolean = false, // Indica si debe mostrarse el indicador de carga.
    val error: String? = null // Guarda un mensaje de error; null significa que no hay error.
)

class ExternalPostViewModel( // Coordina la consulta al Repository y actualiza el estado de la pantalla.
    private val repository: ExternalPostRepository = // Recibe el Repository; se puede reemplazar por un mock durante las pruebas.
        ExternalPostRepository() // Construye el Repository real cuando la pantalla crea el ViewModel.
) : ViewModel() { // Hereda el ciclo de vida de ViewModel y su soporte para corrutinas.

    private val _uiState = MutableStateFlow(ExternalPostUiState()) // Mantiene el estado mutable privado dentro del ViewModel.
    private var cargaActual: Job? = null // Conserva la tarea activa para impedir solicitudes superpuestas.

    val uiState: StateFlow<ExternalPostUiState> = // Expone el estado para que la pantalla pueda observarlo.
        _uiState.asStateFlow() // Impide que la pantalla modifique directamente el flujo interno.

    init { // Ejecuta este bloque una sola vez al crear el ViewModel.
        cargarPublicaciones() // Inicia automáticamente la primera carga de publicaciones.
    }

    fun cargarPublicaciones() { // Permite cargar o actualizar los datos desde el botón de la pantalla.
        // Evita solicitudes simultáneas si se pulsa Actualizar varias veces.
        if (cargaActual?.isActive == true) return // Ignora otro clic si todavía hay una petición en curso.
        cargaActual = viewModelScope.launch { // Inicia y registra la corrutina; se cancela cuando se destruye el ViewModel.
            _uiState.value = ExternalPostUiState( // Publica un nuevo estado completo para que Compose redibuje la pantalla.
                cargando = true // Activa la carga y, al crear el nuevo estado, limpia los datos y errores anteriores.
            )

            try { // Intenta consultar los datos para tratar los fallos de forma controlada.
                val publicaciones = // Prepara la variable que recibirá la lista del Repository.
                    repository.obtenerPublicaciones() // Espera la respuesta de la función suspendida del Repository.

                _uiState.value = ExternalPostUiState( // Publica un nuevo estado completo para que Compose redibuje la pantalla.
                    publicaciones = publicaciones, // Copia la lista recibida al estado que observará la interfaz.
                    cargando = false // Indica que la petición ya no está en curso.
                )
            } catch (e: CancellationException) { // Separa una cancelación del ciclo de vida de un fallo de conexión.
                throw e // Propaga la cancelación para respetar el funcionamiento de las corrutinas.
            } catch (e: Exception) { // Captura los demás errores para mostrarlos en la pantalla.
                _uiState.value = ExternalPostUiState( // Publica un nuevo estado completo para que Compose redibuje la pantalla.
                    cargando = false, // Desactiva el indicador de carga para el resultado final.
                    error = e.message ?: "Error al cargar publicaciones" // Usa el mensaje recibido o uno predeterminado si no existe.
                )
            } finally { // Ejecuta la limpieza tanto en éxito como en error o cancelación.
                _uiState.value = _uiState.value.copy(cargando = false) // Asegura que el indicador no quede activo al terminar la tarea.
            }
        }
    }
}
