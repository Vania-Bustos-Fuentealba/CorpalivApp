package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro de los modelos de datos de la app

/**
- G13: roles de la cuenta con sesión iniciada. El rol se muestra como badge bajo el nombre.

 */
enum class RolUsuario(val etiqueta: String) { // Enum que define los roles de usuario disponibles en el sistema con su etiqueta legible
    ADMINISTRADOR("Administrador") // Rol de administrador con la etiqueta de texto "Administrador"
}

/**
- G13: datos generales de la cuenta que inició sesión.

- [inicio] es la fecha y hora en que se inició la sesión (en milisegundos).
 */
data class UsuarioSesion( // Data class que contiene la información de la sesión activa del usuario
    val nombre: String, // Nombre completo o identificador del usuario autenticado
    val correo: String, // Dirección de correo electrónico asociada a la cuenta activa
    val rol: RolUsuario, // Rol asignado a la sesión mediante la enumeración RolUsuario
    val inicio: Long // Marca de tiempo (timestamp en milisegundos) que registra el momento exacto del inicio de sesión
)
