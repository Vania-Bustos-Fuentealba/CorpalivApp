package com.duocuc.corpalivapp.data.model

/**
 * GUÍA 4 (POO - HERENCIA):
 * La clase 'Empleado' hereda de 'Persona' extendiendo sus atributos
 * para el equipo profesional de Alba Lab y Escuela Jean Van Dijk.
 */
class Empleado(
    nombre: String,
    edad: Int,
    val puesto: String,

    // GUÍA 4 (ENCAPSULAMIENTO):
    // La propiedad 'salario' es 'private' para que solo sea accesible dentro de esta clase,
    // resguardando la privacidad presupuestaria de CORPALIV.
    private val salario: Double
) : Persona(nombre, edad) {

    /**
     * GUÍA 4 (MÉTODO ESPECÍFICO):
     * Imprime y retorna la función o rol dentro de la corporación.
     */
    fun mostrarPuesto(): String {
        val mensaje = "$nombre trabaja como $puesto en la red de inclusión."
        println(mensaje)
        return mensaje
    }

    /**
     * GUÍA 4 (POLIMORFISMO):
     * Sobrescribe (override) el comportamiento del método 'presentarse()'
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