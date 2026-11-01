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
 * G10: secciones de "Quiénes somos". Cada una lee su texto desde un .md en assets/institucion.
 */
enum class SeccionInstitucion(val etiqueta: String, val archivo: String) {
    ESCUELA("Escuela Jan Van Dijk", "institucion/escuela_jan_van_dijk.md"),
    ALBA_LAB("Alba Lab", "institucion/alba_lab.md")
}

/** Sección elegida y su texto; contenido null = cargando, error = no se pudo leer el archivo. */
data class InstitucionUiState(
    val seccion: SeccionInstitucion = SeccionInstitucion.ESCUELA,
    val contenido: String? = null,
    val error: Boolean = false
)

/**
 * Extiende AndroidViewModel porque necesita el contexto para abrir los archivos de assets.
 */
class InstitucionViewModel(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(InstitucionUiState())
    val estado: StateFlow<InstitucionUiState> = _estado.asStateFlow()

    // Textos ya leídos, para no abrir el archivo cada vez que se cambia de sección
    private val cache = mutableMapOf<SeccionInstitucion, String>()

    init {
        seleccionar(SeccionInstitucion.ESCUELA)
    }

    /** Muestra la sección elegida, leyendo su archivo fuera del hilo principal. */
    fun seleccionar(seccion: SeccionInstitucion) {
        val guardado = cache[seccion]
        if (guardado != null) {
            _estado.value = InstitucionUiState(seccion = seccion, contenido = guardado)
            return
        }

        _estado.value = InstitucionUiState(seccion = seccion)
        viewModelScope.launch {
            val texto = withContext(Dispatchers.IO) {
                runCatching {
                    getApplication<Application>().assets.open(seccion.archivo)
                        .bufferedReader().use { it.readText() }
                }.getOrNull()
            }

            if (texto != null) cache[seccion] = texto

            // Solo se actualiza si el usuario sigue en esa sección
            if (_estado.value.seccion == seccion) {
                _estado.value = InstitucionUiState(
                    seccion = seccion,
                    contenido = texto,
                    error = texto == null
                )
            }
        }
    }
}
