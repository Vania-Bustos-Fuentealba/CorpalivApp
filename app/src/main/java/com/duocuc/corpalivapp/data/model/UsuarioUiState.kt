package com.duocuc.corpalivapp.data.model

/**
 * G11: modelo principal que representa el estado del formulario de registro.
 */
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,

    // G11: datos del interés de colaboración y estado del envío
    val modalidad: String = "",
    val tipoEntidad: String = "",
    val mensaje: String = "",
    val enviado: Boolean = false,

    val errores: UsuarioErrores = UsuarioErrores()
)
