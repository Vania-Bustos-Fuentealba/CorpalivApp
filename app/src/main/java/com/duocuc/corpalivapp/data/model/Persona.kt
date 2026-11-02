package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro del modelo de datos de la app

/**
- G4:

- Representa la abstracción general de un integrante en la red CORPALIV.

- Se utiliza la palabra clave 'open' para permitir que otras clases hereden de ella.

 */
open class Persona( // Declaración de la clase base con el modificador 'open' para habilitar la herencia en Kotlin
    val nombre: String, // Propiedad pública de solo lectura (val) para almacenar el nombre de la persona
    val edad: Int // Propiedad pública de solo lectura (val) para almacenar la edad en años
) {
    /**
    - GUÍA 4 (POLIMORFISMO - METODO BASE):

    - Marcado como 'open' para permitir su sobrescritura en las clases hijas.

     */
    open fun presentarse(): String { // Función marcada como 'open' para permitir su polimorfismo mediante sobrescritura (override) en clases hijas como Empleado
        val saludo = "Hola, mi nombre es $nombre y tengo $edad años." // Construye la cadena de saludo base utilizando interpolación de cadenas
        println(saludo) // Salida por consola para la guía // Imprime el saludo resultante en la consola del sistema para depuración
        return saludo // Retorna la cadena de texto construida
    }
}