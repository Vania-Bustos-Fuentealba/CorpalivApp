package com.duocuc.corpalivapp.data.model

/**
 * MODELO DE DATOS: OportunidadModel
 *
 * IMPLEMENTACIÓN GUÍA 2 (DSY1105):
 * 1. Variables Numéricas y Operadores Aritméticos (Double, Int, +, -, *, /)
 * 2. Seguridad ante Nulos / Null Safety (String?, operador de llamada segura ?.)
 * 3. Control de Flujo con Expresión 'when'
 *
 * IMPLEMENTACIÓN GUÍA 4 (DSY1105):
 * 4. Integración POO: Relación de composición con la clase 'Empleado' (que hereda de 'Persona').
 */
data class OportunidadModel(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val impactoSocial: String,

    // ---------------------------------------------------------------------
    // GUÍA 2 - NULL SAFETY:
    // Definición de atributos de tipo String? (nulables) para manejar datos opcionales
    // provistos por el backend o el origen de datos sin romper la aplicación.
    // ---------------------------------------------------------------------
    val imagenUrl: String?,
    val contactoEmail: String?,

    // ---------------------------------------------------------------------
    // GUÍA 2 - VARIABLES NUMÉRICAS (Double):
    // Declaración explícita de tipos numéricos de punto flotante para métricas financieras.
    // ---------------------------------------------------------------------
    val metaFinanciamiento: Double,
    val montoRecaudado: Double,

    // ---------------------------------------------------------------------
    // GUÍA 4 - INTEGRACIÓN POO (Relación de Composición / Herencia):
    // Asigna al profesional a cargo del proyecto (instancia de Empleado que hereda de Persona).
    // Opcional (= null) para resguardar la compatibilidad con endpoints existentes.
    // ---------------------------------------------------------------------
    val responsable: Empleado? = null
) {

    // ---------------------------------------------------------------------
    // GUÍA 2 - OPERADORES ARITMÉTICOS (Resta '-'):
    // Calcula la brecha o saldo faltante para alcanzar la meta financiera del programa.
    // ---------------------------------------------------------------------
    fun calcularMontoFaltante(): Double {
        val faltante = metaFinanciamiento - montoRecaudado
        return if (faltante > 0) faltante else 0.0
    }

    // ---------------------------------------------------------------------
    // GUÍA 2 - OPERADORES ARITMÉTICOS (División '/' y Multiplicación '*'):
    // Calcula la relación porcentual de avance del financiamiento: (montoRecaudado / meta) * 100
    // y realiza la conversión explícita a entero (.toInt()).
    // ---------------------------------------------------------------------
    fun calcularPorcentajeAvance(): Int {
        if (metaFinanciamiento <= 0.0) return 0
        val porcentaje = (montoRecaudado / metaFinanciamiento) * 100
        return porcentaje.toInt()
    }

    // ---------------------------------------------------------------------
    // GUÍA 2 - NULL SAFETY (Llamada Segura '?.'):
    // Utiliza 'contactoEmail?.length' para consultar la propiedad únicamente si la variable
    // no es nula. Garantiza un retorno seguro de email o una dirección por defecto (fallback).
    // ---------------------------------------------------------------------
    fun obtenerContactoSeguro(): String {
        val longitudEmail = contactoEmail?.length // Llamada segura ?.

        return if (longitudEmail != null && longitudEmail > 0) {
            contactoEmail
        } else {
            "contacto@corpaliv-demo.cl" // Correo institucional por defecto
        }
    }

    // ---------------------------------------------------------------------
    // GUÍA 2 - CONTROL DE FLUJO ('when' como expresión):
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