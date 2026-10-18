package com.duocuc.corpalivapp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * GUÍA 13 - PARTE 2: VIEWMODEL DE PERFIL
 *
 * Guarda el Uri de la imagen elegida (galería) o capturada (cámara).
 * Se comparte a nivel de la Activity: la foto se mantiene al rotar la pantalla
 * y también se puede mostrar en otras pantallas (por ejemplo, el menú lateral).
 */
class PerfilViewModel : ViewModel() {

    // null = todavía no hay imagen
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    /** Actualiza la imagen con la que se seleccionó desde la galería. */
    fun actualizarDesdeGaleria(uri: Uri) {
        _imagenUri.value = uri
    }

    /** Actualiza la imagen con la que se capturó con la cámara. */
    fun actualizarDesdeCamara(uri: Uri) {
        _imagenUri.value = uri
    }

    /** Quita la imagen actual y vuelve al ícono por defecto. */
    fun quitarImagen() {
        _imagenUri.value = null
    }
}
