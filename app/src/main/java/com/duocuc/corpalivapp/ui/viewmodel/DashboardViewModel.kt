package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de negocio y presentación (ViewModel)

import android.app.Application // Importación de la clase Application de Android para proporcionar el contexto global de la app
import androidx.lifecycle.AndroidViewModel // ViewModel especializado con acceso al contexto de la aplicación
import androidx.lifecycle.viewModelScope // Alcance (scope) de corrutinas vinculado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.local.RegistroDataStore // Gestor de almacenamiento local DataStore para persistir aportes y registros
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Modelo de datos que representa un registro enviado y guardado
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum para distinguir entre tipos de registro (PERSONA o EMPRESA)
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
    EMPRESAS("Empresas") // Opción para filtrar y mostrar solo los registros de empresas
}

/** Estado del Dashboard: lista ya filtrada y totales de cada tipo de aporte. */
data class DashboardUiState( // Clase de datos que encapsula el estado visual de la pantalla Dashboard
    val cargando: Boolean = true, // Flag para indicar si la información aún se está cargando
    val filtro: FiltroDashboard = FiltroDashboard.TODOS, // Criterio de filtro actualmente seleccionado
    val registros: List<RegistroGuardado> = emptyList(), // Lista de registros filtrados a mostrar en la UI
    val totalPersonas: Int = 0, // Contador total de registros de personas
    val totalEmpresas: Int = 0 // Contador total de registros de empresas
)

/**
- G12: lógica del Dashboard. Lee los aportes guardados, aplica el filtro y guarda los nuevos.

- Se crea a nivel de la Activity para poder guardar un aporte aunque el Dashboard no esté abierto.

 */
class DashboardViewModel(application: Application) : AndroidViewModel(application) { // ViewModel que administra el panel de control y la persistencia de registros

    private val almacen = RegistroDataStore(application) // Instancia del gestor DataStore pasando el contexto de la aplicación
    private val _filtro = MutableStateFlow(FiltroDashboard.TODOS) // Estado privado que contiene el filtro seleccionado

    // Combina la lista guardada con el filtro elegido; se actualiza sola al guardar un aporte
    val estado: StateFlow<DashboardUiState> = combine(almacen.obtenerTodos(), _filtro) { todos, filtro -> // Combina el flujo de DataStore y el flujo de filtro
        DashboardUiState(
            cargando = false, // Marca la carga como completada al emitir
            filtro = filtro, // Asigna el filtro actual
            registros = when (filtro) { // Filtra la lista de registros en función del filtro activo
                FiltroDashboard.TODOS -> todos // Muestra la lista completa sin filtrar
                FiltroDashboard.PERSONAS -> todos.filter { it.tipo == TipoRegistro.PERSONA.name } // Muestra solo los registros cuyo tipo sea PERSONA
                FiltroDashboard.EMPRESAS -> todos.filter { it.tipo == TipoRegistro.EMPRESA.name } // Muestra solo los registros cuyo tipo sea EMPRESA
            },
            totalPersonas = todos.count { it.tipo == TipoRegistro.PERSONA.name }, // Calcula la cantidad total de registros de tipo PERSONA
            totalEmpresas = todos.count { it.tipo == TipoRegistro.EMPRESA.name } // Calcula la cantidad total de registros de tipo EMPRESA
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState()) // Transforma a StateFlow activo durante la subscripción de la UI

    fun cambiarFiltro(filtro: FiltroDashboard) { // Función para actualizar el filtro desde la interfaz de usuario
        _filtro.value = filtro // Asigna el nuevo valor al estado interno del filtro
    }

    /** Guarda un aporte nuevo (se escribe en DataStore fuera del hilo principal). */
    fun guardar(registro: RegistroGuardado) { // Función para registrar un nuevo aporte
        viewModelScope.launch { almacen.guardar(registro) } // Inicia una corrutina en el scope del ViewModel para persistir el registro en DataStore
    }
}