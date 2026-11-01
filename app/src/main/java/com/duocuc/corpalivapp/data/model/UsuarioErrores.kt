package com.duocuc.corpalivapp.data.model

/**
 * G11: Errores de validación por campo, separados por tipo de formulario.
 * Cada propiedad es null cuando el campo es válido, o el mensaje a mostrar si no lo es.
 */
data class UsuarioErrores(
    val persona: ErroresPersona = ErroresPersona(),
    val empresa: ErroresEmpresa = ErroresEmpresa()
)

data class ErroresPersona(
    val nombre: String? = null,
    val apellido: String? = null,
    val run: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val motivo: String? = null,
    val frecuencia: String? = null
)

data class ErroresEmpresa(
    val razonSocial: String? = null,
    val rut: String? = null,
    val giro: String? = null,
    val representante: String? = null,
    val rutRepresentante: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val direccion: String? = null
)
