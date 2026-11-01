package com.duocuc.corpalivapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.local.RegistroDataStore
import com.duocuc.corpalivapp.data.model.RegistroGuardado
import com.duocuc.corpalivapp.data.model.TipoRegistro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** G12: filtros del Dashboard. */
enum class FiltroDashboard(val etiqueta: String) {
    TODOS("Todos"),
    PERSONAS("Personas"),
    EMPRESAS("Empresas")
}

/** Estado del Dashboard: lista ya filtrada y totales de cada tipo de aporte. */
data class DashboardUiState(
    val cargando: Boolean = true,
    val filtro: FiltroDashboard = FiltroDashboard.TODOS,
    val registros: List<RegistroGuardado> = emptyList(),
    val totalPersonas: Int = 0,
    val totalEmpresas: Int = 0
)

/**
 * G12: lógica del Dashboard. Lee los aportes guardados, aplica el filtro y guarda los nuevos.
 * Se crea a nivel de la Activity para poder guardar un aporte aunque el Dashboard no esté abierto.
 */
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val almacen = RegistroDataStore(application)
    private val _filtro = MutableStateFlow(FiltroDashboard.TODOS)

    // Combina la lista guardada con el filtro elegido; se actualiza sola al guardar un aporte
    val estado: StateFlow<DashboardUiState> = combine(almacen.obtenerTodos(), _filtro) { todos, filtro ->
        DashboardUiState(
            cargando = false,
            filtro = filtro,
            registros = when (filtro) {
                FiltroDashboard.TODOS -> todos
                FiltroDashboard.PERSONAS -> todos.filter { it.tipo == TipoRegistro.PERSONA.name }
                FiltroDashboard.EMPRESAS -> todos.filter { it.tipo == TipoRegistro.EMPRESA.name }
            },
            totalPersonas = todos.count { it.tipo == TipoRegistro.PERSONA.name },
            totalEmpresas = todos.count { it.tipo == TipoRegistro.EMPRESA.name }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    fun cambiarFiltro(filtro: FiltroDashboard) {
        _filtro.value = filtro
    }

    /** Guarda un aporte nuevo (se escribe en DataStore fuera del hilo principal). */
    fun guardar(registro: RegistroGuardado) {
        viewModelScope.launch { almacen.guardar(registro) }
    }
}
