package com.duocuc.corpalivapp.data.model

/**
 * G4:
 * La clase 'Empleado' hereda de 'Persona' extendiendo sus atributos
 * para los equipos de la ONG.
 */
class Empleado(
    nombre: String,
    edad: Int,
    val puesto: String,

    // G4:
    // La propiedad 'salario' es 'private' para que solo sea accesible dentro de esta clase,
    private val salario: Double
) : Persona(nombre, edad) {

    /**
     * G4 (METODO ESPECIFICO):
     * Imprime y retorna la función o rol dentro de la corporación.
     */
    fun mostrarPuesto(): String {
        val mensaje = "$nombre trabaja como $puesto en la red de inclusión."
        println(mensaje)
        return mensaje
    }

    /**
     * G4
     * Sobrescribe (override) el comportamiento del metodo 'presentarse()'
     * entregando un saludo específico de un colaborador institucional.
     */
    override fun presentarse(): String {
        val saludoEspecifico = "Hola, soy $nombre, tengo $edad años y desempeño el cargo de $puesto en CORPALIV."
        println(saludoEspecifico)
        return saludoEspecifico
    }

    // Getter controlado opcional para validar encapsulamiento si se requiere auditar el presupuesto
    fun obtenerFichaResumida(): String {
        return "Empleado: $nombre | Cargo: $puesto | Renta reservada: $${salario}"
    }
}