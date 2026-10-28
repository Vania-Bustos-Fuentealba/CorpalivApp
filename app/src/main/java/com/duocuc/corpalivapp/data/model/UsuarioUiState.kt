package com.duocuc.corpalivapp.data.model

/**
 * G11: modelo principal que representa el estado del formulario de registro.
 */
data class UsuarioUiState(
    val nombre: String = "",
    val apellido: String = "",
    val rut: String = "",
    val correo: String = "",
    val direccion: String = "",
    val motivo: String = "",

    // Título de la oportunidad elegida antes de abrir el formulario
    val oportunidad: String = "",

    // G12: estado del envío simulado (barra de carga) y aviso de que terminó
    val enviando: Boolean = false,
    val progreso: Float = 0f,
    val completado: Boolean = false,

    val errores: UsuarioErrores = UsuarioErrores()
)
