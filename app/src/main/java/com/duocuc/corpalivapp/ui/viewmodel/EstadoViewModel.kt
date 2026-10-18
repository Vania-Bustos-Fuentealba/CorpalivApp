package com.duocuc.corpalivapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.local.EstadoDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * G12: estado del "modo especial" con persistencia en DataStore.
 */
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    // La UI muestra el CircularProgressIndicator
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    // Controla el mensaje animado de "estado guardado"
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()

    private var jobMensaje: Job? = null

    init {
        cargarEstado()
    }

    /** Carga el estado guardado; el delay simula una carga lenta para ver el loader. */
    private fun cargarEstado() {
        viewModelScope.launch {
            delay(1500)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    /** Se Invierte el estado, lo guarda en DataStore y muestra el mensaje de confirmación. */
    fun alternarEstado() {
        viewModelScope.launch {
            // Si aún está cargando no se hace nada
            val actual = _activo.value ?: return@launch
            val nuevoValor = !actual

            estadoDataStore.guardarEstado(nuevoValor)
            _activo.value = nuevoValor

            mostrarConfirmacion()
        }
    }

    // Si se presiona varias veces seguidas, se reinicia el temporizador del mensaje.
    private fun mostrarConfirmacion() {
        jobMensaje?.cancel()
        jobMensaje = viewModelScope.launch {
            _mostrarMensaje.value = true
            delay(2000)
            _mostrarMensaje.value = false
        }
    }
}
