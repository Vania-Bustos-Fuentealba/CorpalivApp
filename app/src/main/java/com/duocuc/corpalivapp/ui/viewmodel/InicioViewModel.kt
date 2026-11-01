package com.duocuc.corpalivapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * G10: texto de la pantalla de inicio. Lee la información de Corpaliv desde assets/institucion/corpaliv.md.
 * Se crea a nivel de la Activity, así el archivo se lee una sola vez.
 */
class InicioViewModel(application: Application) : AndroidViewModel(application) {

    // null = todavía cargando
    private val _contenido = MutableStateFlow<String?>(null)
    val contenido: StateFlow<String?> = _contenido.asStateFlow()

    init {
        viewModelScope.launch {
            val texto = withContext(Dispatchers.IO) {
                runCatching {
                    getApplication<Application>().assets.open(ARCHIVO)
                        .bufferedReader().use { it.readText() }
                }.getOrNull()
            }

            // Si el archivo no se pudo leer se muestra un aviso en lugar de dejar el cargando
            _contenido.value = texto ?: "# CORPALIV\n\nNo se pudo cargar la información institucional."
        }
    }

    private companion object {
        const val ARCHIVO = "institucion/corpaliv.md"
    }
}
