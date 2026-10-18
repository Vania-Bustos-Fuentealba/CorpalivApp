package com.duocuc.corpalivapp.data.model

/**
 * G4:
 * Representa la abstracción general de un integrante en la red CORPALIV.
 * Se utiliza la palabra clave 'open' para permitir que otras clases hereden de ella.
 */
open class Persona(
    val nombre: String,
    val edad: Int
) {
    /**
     * GUÍA 4 (POLIMORFISMO - METODO BASE):
     * Marcado como 'open' para permitir su sobrescritura en las clases hijas.
     */
    open fun presentarse(): String {
        val saludo = "Hola, mi nombre es $nombre y tengo $edad años."
        println(saludo) // Salida por consola para la guía
        return saludo
    }
}