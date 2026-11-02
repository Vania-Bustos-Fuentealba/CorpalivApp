package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece la clase dentro del modelo de datos de la app

/**
- G4:

- La clase 'Empleado' hereda de 'Persona' extendiendo sus atributos

- para los equipos de la ONG.

 */
class Empleado( // Declaración de la clase Empleado que representa a un trabajador o colaborador de la ONG
    nombre: String, // Parámetro del constructor primario para el nombre, que se transmitirá a la clase padre
    edad: Int, // Parámetro del constructor primario para la edad, que se transmitirá a la clase padre
    val puesto: String, // Propiedad pública de solo lectura (val) que almacena el cargo o rol del empleado

    // G4:
    // La propiedad 'salario' es 'private' para que solo sea accesible dentro de esta clase,
    private val salario: Double // Propiedad privada para asegurar el encapsulamiento de la remuneración/presupuesto
) : Persona(nombre, edad) { // Hereda (: Persona) e invoca al constructor de la clase base pasando 'nombre' y 'edad'

    /**
    - G4 (METODO ESPECIFICO):

    - Imprime y retorna la función o rol dentro de la corporación.

     */
    fun mostrarPuesto(): String { // Función pública propia de Empleado para exponer el puesto ocupado
        val mensaje = "$nombre trabaja como $puesto en la red de inclusión." // Construye el mensaje formateado usando interpolación de strings
        println(mensaje) // Imprime el mensaje descriptivo en la consola del sistema para depuración/log
        return mensaje // Retorna la cadena de texto construida
    }

    /**
    - G4

    - Sobrescribe (override) el comportamiento del metodo 'presentarse()'

    - entregando un saludo específico de un colaborador institucional.

     */
    override fun presentarse(): String { // Sobrescribe (override) el método abstracto/abierto heredado de la clase base 'Persona'
        val saludoEspecifico = "Hola, soy $nombre, tengo $edad años y desempeño el cargo de $puesto en CORPALIV." // Construye la presentación institucional
        println(saludoEspecifico) // Imprime la presentación específica en la consola
        return saludoEspecifico // Retorna el saludo formateado como String
    }

    // Getter controlado opcional para validar encapsulamiento si se requiere auditar el presupuesto
    fun obtenerFichaResumida(): String { // Método de apoyo que permite consultar información sin exponer directamente el atributo 'salario'
        return "Empleado: $nombre | Cargo: $puesto | Renta reservada: $${salario}" // Retorna una ficha técnica en texto con los datos formateados
    }
}