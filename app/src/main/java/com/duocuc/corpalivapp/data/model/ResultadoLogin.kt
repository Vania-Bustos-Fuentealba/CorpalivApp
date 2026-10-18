package com.duocuc.corpalivapp.data.model

/**
 * G5 - SEALED CLASS:
 * Define el conjunto cerrado de estados finitos posibles durante la autenticación.
 */
sealed class ResultadoLogin {

    // Estado 1: Proceso en curso
    object Autenticando : ResultadoLogin()

    // Estado 2: Éxito que transporta el objeto PerfilUsuario
    data class Exito(val perfil: PerfilUsuario) : ResultadoLogin()

    // Estado 3: Error con mensaje descriptivo
    data class Error(val mensaje: String) : ResultadoLogin()
}