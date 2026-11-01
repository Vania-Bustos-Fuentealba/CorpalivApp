package com.duocuc.corpalivapp.data.model

/**
 * G13: roles de la cuenta con sesión iniciada. El rol se muestra como badge bajo el nombre.
 */
enum class RolUsuario(val etiqueta: String) {
    ADMINISTRADOR("Administrador")
}

/**
 * G13: datos generales de la cuenta que inició sesión.
 * [inicio] es la fecha y hora en que se inició la sesión (en milisegundos).
 */
data class UsuarioSesion(
    val nombre: String,
    val correo: String,
    val rol: RolUsuario,
    val inicio: Long
)
