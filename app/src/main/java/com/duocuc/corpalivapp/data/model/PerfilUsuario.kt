package com.duocuc.corpalivapp.data.model

/**
 * GUÍA 5 - CLASE DE DATOS (DATA CLASS):
 * Almacena las propiedades del perfil del usuario tras una autenticación exitosa.
 */
data class PerfilUsuario(
    val nombreUsuario: String,
    val email: String
)