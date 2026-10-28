package com.duocuc.corpalivapp.data.model

/**
 * G11: Errores de validación por campo.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val apellido: String? = null,
    val rut: String? = null,
    val correo: String? = null,
    val direccion: String? = null,
    val motivo: String? = null
)
