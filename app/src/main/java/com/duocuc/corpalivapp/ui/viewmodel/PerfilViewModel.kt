package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import android.net.Uri // Importación de la clase Uri para manipular direcciones/ubicaciones de recursos multimedia
import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto a la interfaz de usuario
import kotlinx.coroutines.flow.asStateFlow // Convierte un MutableStateFlow en un StateFlow inmutable

/**
- GUÍA 13 - PARTE 2: VIEWMODEL DE PERFIL

 *
- Guarda el Uri de la imagen elegida (galería) o capturada (cámara).

- Se comparte a nivel de la Activity: la foto se mantiene al rotar la pantalla

- y también se puede mostrar en otras pantallas (por ejemplo, el menú lateral).

 */
class PerfilViewModel : ViewModel() { // ViewModel que gestiona la selección y almacenamiento de la Uri de la imagen de perfil de usuario

    // null = todavía no hay imagen
    private val _imagenUri = MutableStateFlow<Uri?>(null) // Estado mutable interno que almacena la Uri de la imagen actual o null si no se ha asignado
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow() // Flujo de estado público de solo lectura para observar la Uri de la foto desde la UI

    /** Actualiza la imagen con la que se seleccionó desde la galería. */
    fun actualizarDesdeGaleria(uri: Uri) { // Función para establecer la Uri proveniente de la galería del dispositivo
        _imagenUri.value = uri // Asigna la nueva Uri al flujo de estado
    }

    /** Actualiza la imagen con la que se capturó con la cámara. */
    fun actualizarDesdeCamara(uri: Uri) { // Función para establecer la Uri proveniente de la captura de la cámara
        _imagenUri.value = uri // Asigna la Uri del archivo generado por la cámara al flujo de estado
    }

    /** Quita la imagen actual y vuelve al ícono por defecto. */
    fun quitarImagen() { // Función para eliminar la referencia de la foto de perfil activa
        _imagenUri.value = null // Restablece el valor a null para volver al estado/avatar predeterminado
    }
}