package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece la clase dentro de los modelos de datos de la app

/**
- MODELO DE DATOS: OportunidadModel

 *
- IMPLEMENTACIÓN GUÍA 2 (DSY1105):

- Variables Numéricas y Operadores Aritméticos (Double, Int, +, -, *, /)

- 2. Seguridad ante Nulos / Null Safety (String?, operador de llamada segura ?.)

- 3. Control de Flujo con Expresión 'when'

 *
- G4:

- Integración POO: Relación de composición con la clase 'Empleado' (que hereda de 'Persona').

 */
data class OportunidadModel( // Declaración de la data class que modela una oportunidad o programa de financiamiento de la ONG
    val id: Int, // Identificador único numérico de la oportunidad
    val titulo: String, // Título descriptivo del programa o proyecto
    val categoria: String, // Categoría a la que pertenece (ej. "Educación", "Inclusión Laboral")
    val descripcion: String, // Detalle explicativo sobre los objetivos del proyecto
    val impactoSocial: String, // Descripción del impacto cuantitativo o cualitativo generado en la comunidad

    // ---------------------------------------------------------------------
    // G2:
    // Definición de atributos de tipo String para manejar datos opcionales
    // provistos por el backend o el origen de datos sin romper la aplicación.
    // ---------------------------------------------------------------------
    val imagenUrl: String?, // URL opcional (anulable) de la imagen representativa del proyecto
    val contactoEmail: String?, // Dirección de correo electrónico de contacto opcional (anulable)

    // ---------------------------------------------------------------------
    // GUÍA 2 - VARIABLES NUMÉRICAS (Double):
    // Declaración explícita de tipos numéricos de punto flotante para métricas financieras.
    // ---------------------------------------------------------------------
    val metaFinanciamiento: Int, // Monto objetivo total en dinero para financiar el proyecto
    val montoRecaudado: Int, // Monto actual recolectado mediante aportes hasta la fecha

    // ---------------------------------------------------------------------
    // G4:
    // Asigna al profesional a cargo del proyecto (instancia de Empleado que hereda de Persona).
    // ---------------------------------------------------------------------
    val responsable: Empleado? = null // Objeto opcional Empleado que representa al encargado institucional del proyecto
) {

    // ---------------------------------------------------------------------
    // G2:
    // Calcula la brecha o saldo faltante para alcanzar la meta financiera del programa.
    // ---------------------------------------------------------------------
    fun calcularMontoFaltante(): Int { // Función que calcula el déficit financiero restante del proyecto
        val faltante = metaFinanciamiento - montoRecaudado // Operación aritmética de resta entre la meta y lo recaudado
        return if (faltante > 0) faltante else 0 // Retorna el monto faltante o 0 si la meta ya fue alcanzada o superada
    }

    // ---------------------------------------------------------------------
    // G2:
    // Calcula la relación porcentual de avance del financiamiento: (montoRecaudado / meta) * 100
    // y convierte a entero.
    // ---------------------------------------------------------------------
    fun calcularPorcentajeAvance(): Int { // Función que calcula el porcentaje de progreso de la recaudación
        if (metaFinanciamiento <= 0.0) return 0 // Validación de seguridad para evitar división por cero o metas inválidas
        // G2: se pasa a Double para que la división no se trunque a 0 (ej: 1200000 / 5000000).
        val porcentaje = (montoRecaudado.toDouble() / metaFinanciamiento) * 100 // Convierte a Double para mantener precisión decimal antes de multiplicar por 100
        return porcentaje.toInt() // Trunca el valor decimal resultante a un entero (Int) y lo retorna
    }


    // G2 - NULL SAFETY:
    // Utiliza 'contactoEmail?.length' para consultar la propiedad únicamente si la variable
    // no es nula
    fun obtenerContactoSeguro(): String { // Función que resuelve de forma segura una dirección de correo no nula
        val longitudEmail = contactoEmail?.length // Llamada segura ?. que obtiene la longitud del correo o null si contactoEmail es null

        return if (longitudEmail != null && longitudEmail > 0) { // Evalúa que el correo no sea nulo ni esté vacío
            contactoEmail // Retorna el correo específico del proyecto
        } else {
            "contacto@corpaliv-demo.cl" // Correo institucional por defecto en caso de valor nulo o inválido
        }
    }

    // ---------------------------------------------------------------------
    // G2 - CONTROL DE FLUJO:
    // Evalúa rangos numéricos del porcentaje de avance para asignar dinámicamente
    // la etiqueta de prioridad de vinculación institucional.
    // ---------------------------------------------------------------------
    fun obtenerPrioridadVinculacion(): String { // Función que clasifica el nivel de prioridad usando lógica condicional
        val avance = calcularPorcentajeAvance() // Obtiene el valor entero del porcentaje de avance

        return when { // Expresión 'when' evaluada como una serie de condiciones booleanas
            avance < 30 -> "Urgencia Alta (Requiere RSE)" // Rango menor al 30%: asigna máxima urgencia de financiamiento
            avance in 30..75 -> "Prioridad Media (En Avance)" // Rango entre 30% y 75%: asigna prioridad intermedia
            avance in 76..99 -> "Fase Final de Financiamiento" // Rango entre 76% y 99%: asigna estado de etapa final
            else -> "Proyecto Completo" // Caso por defecto (100% o superior): indica proyecto totalmente financiado
        }
    }
}