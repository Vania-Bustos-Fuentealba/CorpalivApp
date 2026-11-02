package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro del modelo de datos de la app

/**
- G5 - SEALED CLASS:

- Define el conjunto cerrado de estados finitos posibles durante la autenticación.

 */
sealed class ResultadoLogin { // Declaración de la clase sellada que restringe la jerarquía de tipos para representar los estados de inicio de sesión

    // Estado 1: Proceso en curso
    object Autenticando : ResultadoLogin() // Objeto singleton que representa el estado transitorio de carga o verificación de credenciales

    // Estado 2: Éxito que transporta el objeto PerfilUsuario
    data class Exito(val perfil: PerfilUsuario) : ResultadoLogin() // Subclase de datos que representa una autenticación exitosa, encapsulando los datos del usuario

    // Estado 3: Error con mensaje descriptivo
    data class Error(val mensaje: String) : ResultadoLogin() // Subclase de datos que representa un fallo en el proceso de login, conteniendo la causa en texto
}