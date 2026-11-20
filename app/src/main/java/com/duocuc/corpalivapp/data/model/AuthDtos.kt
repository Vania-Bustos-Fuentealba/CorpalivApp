package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenecen los objetos de transferencia de datos (DTO) del login

/**
- PARCIAL 4: objetos que viajan entre la app y auth-service (microservicio Spring Boot).

- Los nombres de los campos coinciden con el JSON del backend para que Gson los convierta solo.

 */
data class LoginRequestDto( // Cuerpo que se envía a POST /api/auth/login
    val correo: String, // Correo de la cuenta de administrador
    val clave: String // Contraseña de la cuenta (viaja solo en esta petición y no se guarda en la app)
)

/** Respuesta del login: el token JWT y los datos que la app muestra en el perfil. */
data class SesionRemotaDto( // Respuesta de POST /api/auth/login
    val token: String, // Token JWT que se envía en la cabecera Authorization de las siguientes peticiones
    val tipoToken: String, // Tipo de token (siempre "Bearer")
    val nombre: String, // Nombre de la cuenta autenticada
    val correo: String, // Correo de la cuenta autenticada
    val rol: String, // Rol en texto, por ejemplo "ADMINISTRADOR" (nombre de RolUsuario)
    val rolEtiqueta: String, // Rol legible, por ejemplo "Administrador"
    val inicio: Long // Fecha y hora de inicio de la sesión en milisegundos
)
