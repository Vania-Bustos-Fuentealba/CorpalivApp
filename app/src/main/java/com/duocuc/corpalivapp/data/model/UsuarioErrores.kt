package com.duocuc.corpalivapp.data.model

/**
 * G11: Errores de validación por campo.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val terminos: String? = null,
    val modalidad: String? = null,
    val tipoEntidad: String? = null,
    val mensaje: String? = null
)
