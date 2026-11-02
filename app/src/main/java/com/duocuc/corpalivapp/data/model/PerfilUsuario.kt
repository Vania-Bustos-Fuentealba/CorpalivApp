package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro de los modelos de datos de la app

/**
- G5:

- Almacena las propiedades del perfil del usuario tras autenticarse.

 */
data class PerfilUsuario( // Data class inmutable que sirve como contenedor de los datos del usuario autenticado
    val nombreUsuario: String, // Propiedad pública de solo lectura que almacena el nombre del usuario
    val email: String // Propiedad pública de solo lectura que almacena la dirección de correo electrónico institucional/personal
)