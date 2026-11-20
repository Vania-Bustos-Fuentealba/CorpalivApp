package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de negocio y presentación (ViewModel)

import android.app.Application // Importación de la clase Application de Android para proporcionar el contexto global de la app
import androidx.lifecycle.AndroidViewModel // ViewModel especializado con acceso al contexto de la aplicación
import androidx.lifecycle.viewModelScope // Alcance (scope) de corrutinas vinculado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.local.RegistroDataStore // Gestor de almacenamiento local DataStore para persistir aportes y registros
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Modelo de datos que representa un registro enviado y guardado
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum para distinguir entre tipos de registro (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.data.repository.AportesRepository // Repositorio que consulta aportes-service (Parcial 4)
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.SharingStarted // Estrategias de inicio para la conversión de flujos con stateIn
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura que emite el estado actual
import kotlinx.coroutines.flow.combine // Operador de corrutinas para combinar múltiples flujos en uno solo
import kotlinx.coroutines.flow.stateIn // Convierte un Flow frío en un StateFlow caliente accesible por la UI
import kotlinx.coroutines.launch // Extensión de corrutina para ejecutar operaciones asíncronas

/** G12: filtros del Dashboard. */
enum class FiltroDashboard(val etiqueta: String) { // Enumeración para los criterios de filtrado en el Dashboard
    TODOS("Todos"), // Opción para mostrar la totalidad de los registros guardados
    PERSONAS("Personas"), // Opción para filtrar y mostrar solo los registros de personas
    EMPRESAS("Empresas"), // Opción para filtrar y mostrar solo los registros de empresas
    POSTULACIONES("Postulaciones") // Parcial 4: opción para mostrar solo las personas que postulan a un programa
}

/** Estado del Dashboard: lista ya filtrada y totales de cada tipo de aporte. */
data class DashboardUiState( // Clase de datos que encapsula el estado visual de la pantalla Dashboard
    val cargando: Boolean = true, // Flag para indicar si la información aún se está cargando
    val filtro: FiltroDashboard = FiltroDashboard.TODOS, // Criterio de filtro actualmente seleccionado
    val registros: List<RegistroGuardado> = emptyList(), // Lista de registros filtrados a mostrar en la UI
    val totalPersonas: Int = 0, // Contador total de registros de personas
    val totalEmpresas: Int = 0, // Contador total de registros de empresas
    val totalPostulaciones: Int = 0, // Parcial 4: contador de personas que postulan a un programa
    val error: String? = null // Parcial 4: mensaje cuando falló la conexión con el servidor (null si no hay error)
)

/**
- G12: lógica del Dashboard. Lee los aportes guardados, aplica el filtro y guarda los nuevos.

- Se crea a nivel de la Activity para poder guardar un aporte aunque el Dashboard no esté abierto.

 */
class DashboardViewModel @JvmOverloads constructor(application: Application, private val repositorio: AportesRepository = AportesRepository()) : AndroidViewModel(application) { // ViewModel que administra el panel de control y la persistencia de registros

    private val almacen = RegistroDataStore(application) // Instancia del gestor DataStore pasando el contexto de la aplicación
    private val _filtro = MutableStateFlow(FiltroDashboard.TODOS) // Estado privado que contiene el filtro seleccionado
    private val _sincronizando = MutableStateFlow(false) // Parcial 4: true mientras se descargan los registros del servidor
    private val _error = MutableStateFlow<String?>(null) // Parcial 4: mensaje de error de la última sincronización (null si salió bien)

    // Combina la lista guardada con el filtro elegido; se actualiza sola al guardar un aporte
    val estado: StateFlow<DashboardUiState> = combine(almacen.obtenerTodos(), _filtro, _sincronizando, _error) { todos, filtro, sincronizando, error -> // Combina el flujo de DataStore y el flujo de filtro
        DashboardUiState(
            cargando = sincronizando && todos.isEmpty(), // Marca la carga como completada al emitir
            filtro = filtro, // Asigna el filtro actual
            registros = when (filtro) { // Filtra la lista de registros en función del filtro activo
                FiltroDashboard.TODOS -> todos // Muestra la lista completa sin filtrar
                FiltroDashboard.PERSONAS -> todos.filter { it.tipo == TipoRegistro.PERSONA.name && it.motivo == "APORTE" } // Muestra solo los registros cuyo tipo sea PERSONA
                FiltroDashboard.EMPRESAS -> todos.filter { it.tipo == TipoRegistro.EMPRESA.name && it.motivo == "APORTE" } // Muestra solo los registros cuyo tipo sea EMPRESA
                FiltroDashboard.POSTULACIONES -> todos.filter { it.motivo == "POSTULACION" } // Muestra solo las postulaciones a programas
            },
            totalPersonas = todos.count { it.tipo == TipoRegistro.PERSONA.name && it.motivo == "APORTE" }, // Calcula la cantidad total de registros de tipo PERSONA
            totalEmpresas = todos.count { it.tipo == TipoRegistro.EMPRESA.name && it.motivo == "APORTE" }, // Calcula la cantidad total de registros de tipo EMPRESA
            totalPostulaciones = todos.count { it.motivo == "POSTULACION" }, // Parcial 4: cantidad de personas que postulan a un programa
            error = error // Parcial 4: aviso de falla de conexión para mostrar en pantalla
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState()) // Transforma a StateFlow activo durante la subscripción de la UI

    fun cambiarFiltro(filtro: FiltroDashboard) { // Función para actualizar el filtro desde la interfaz de usuario
        _filtro.value = filtro // Asigna el nuevo valor al estado interno del filtro
    }

    /** Guarda un aporte nuevo (se escribe en DataStore fuera del hilo principal). */
    fun guardar(registro: RegistroGuardado) { // Función para registrar un nuevo aporte
        viewModelScope.launch { almacen.guardar(registro) } // Inicia una corrutina en el scope del ViewModel para persistir el registro en DataStore
    }

    /**
     * PARCIAL 4: descarga los aportes y postulaciones desde aportes-service (requiere la sesión de
     * administrador) y los deja en el DataStore como copia local. Si falla, se muestra el aviso y
     * se conservan los últimos datos descargados.
     */
    fun sincronizar() { // Actualiza el Dashboard con lo que hay en el servidor
        if (_sincronizando.value) return // Evita descargas duplicadas si ya hay una en curso
        _sincronizando.value = true // Marca el inicio de la descarga
        _error.value = null // Limpia el aviso de error anterior

        viewModelScope.launch { // Corrutina: la petición de red no bloquea la interfaz
            repositorio.listar() // Consulta a aportes-service
                .onSuccess { lista -> almacen.reemplazarTodos(lista) } // Reemplaza la copia local por la lista del servidor
                .onFailure { error -> _error.value = error.message } // Informa el motivo de la falla
            _sincronizando.value = false // Marca el fin de la descarga
        }
    }

    /** PARCIAL 4: elimina el registro en el servidor y vuelve a cargar la lista. */
    fun eliminar(id: Long) { // Se usa tras confirmar la eliminación en el diálogo
        viewModelScope.launch { // Corrutina para no bloquear la interfaz
            repositorio.eliminar(id) // Elimina en aportes-service
                .onSuccess { sincronizar() } // Refresca la lista para que desaparezca el registro
                .onFailure { error -> _error.value = error.message } // Informa el motivo de la falla
        }
    }

    /** PARCIAL 4: marca el registro como contactado en el servidor y vuelve a cargar la lista. */
    fun marcarContactado(id: Long) { // Se usa al abrir WhatsApp para contactar a la persona o empresa
        viewModelScope.launch { // Corrutina para no bloquear la interfaz
            repositorio.marcarContactado(id) // Cambia el estado en aportes-service
                .onSuccess { sincronizar() } // Refresca la lista para mostrar el nuevo estado
                .onFailure { error -> _error.value = error.message } // Informa el motivo de la falla
        }
    }
}
