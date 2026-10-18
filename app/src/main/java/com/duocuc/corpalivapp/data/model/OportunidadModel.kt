package com.duocuc.corpalivapp.data.model

/**
 * MODELO DE DATOS: OportunidadModel
 *
 * IMPLEMENTACIÓN GUÍA 2 (DSY1105):
 * 1. Variables Numéricas y Operadores Aritméticos (Double, Int, +, -, *, /)
 * 2. Seguridad ante Nulos / Null Safety (String?, operador de llamada segura ?.)
 * 3. Control de Flujo con Expresión 'when'
 *
 * G4:
 * Integración POO: Relación de composición con la clase 'Empleado' (que hereda de 'Persona').
 */
data class OportunidadModel(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val impactoSocial: String,

    // ---------------------------------------------------------------------
    // G2:
    // Definición de atributos de tipo String para manejar datos opcionales
    // provistos por el backend o el origen de datos sin romper la aplicación.
    // ---------------------------------------------------------------------
    val imagenUrl: String?,
    val contactoEmail: String?,

    // ---------------------------------------------------------------------
    // GUÍA 2 - VARIABLES NUMÉRICAS (Double):
    // Declaración explícita de tipos numéricos de punto flotante para métricas financieras.
    // ---------------------------------------------------------------------
    val metaFinanciamiento: Int,
    val montoRecaudado: Int,

    // ---------------------------------------------------------------------
    // G4:
    // Asigna al profesional a cargo del proyecto (instancia de Empleado que hereda de Persona).
    // ---------------------------------------------------------------------
    val responsable: Empleado? = null
) {

    // ---------------------------------------------------------------------
    // G2:
    // Calcula la brecha o saldo faltante para alcanzar la meta financiera del programa.
    // ---------------------------------------------------------------------
    fun calcularMontoFaltante(): Int {
        val faltante = metaFinanciamiento - montoRecaudado
        return if (faltante > 0) faltante else 0
    }

    // ---------------------------------------------------------------------
    // G2:
    // Calcula la relación porcentual de avance del financiamiento: (montoRecaudado / meta) * 100
    // y convierte a entero.
    // ---------------------------------------------------------------------
    fun calcularPorcentajeAvance(): Int {
        if (metaFinanciamiento <= 0.0) return 0
        // G2: se pasa a Double para que la división no se trunque a 0 (ej: 1200000 / 5000000).
        val porcentaje = (montoRecaudado.toDouble() / metaFinanciamiento) * 100
        return porcentaje.toInt()
    }


    // G2 - NULL SAFETY:
    // Utiliza 'contactoEmail?.length' para consultar la propiedad únicamente si la variable
    // no es nula
    fun obtenerContactoSeguro(): String {
        val longitudEmail = contactoEmail?.length // Llamada segura ?.

        return if (longitudEmail != null && longitudEmail > 0) {
            contactoEmail
        } else {
            "contacto@corpaliv-demo.cl" // Correo institucional por defecto
        }
    }

    // ---------------------------------------------------------------------
    // G2 - CONTROL DE FLUJO:
    // Evalúa rangos numéricos del porcentaje de avance para asignar dinámicamente
    // la etiqueta de prioridad de vinculación institucional.
    // ---------------------------------------------------------------------
    fun obtenerPrioridadVinculacion(): String {
        val avance = calcularPorcentajeAvance()

        return when {
            avance < 30 -> "Urgencia Alta (Requiere RSE)"
            avance in 30..75 -> "Prioridad Media (En Avance)"
            avance in 76..99 -> "Fase Final de Financiamiento"
            else -> "Proyecto Completo"
        }
    }
}