package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro de los modelos de datos de la app

/**
- G11: motivos del registro de una persona (aportar o ingresar a un programa).

- Se usan en el desplegable de RegistroScreen y en los textos de agradecimiento de ResumenScreen.

- En [confirmacion], %s se reemplaza por el título de la oportunidad elegida.
 */
data class MotivoRegistro( // Data class que define la estructura del modelo para cada motivo de registro disponible
    val etiqueta: String, // Texto breve que identifica el motivo (ej. "Aportar") usado en el menú desplegable (Dropdown)
    val descripcion: String, // Texto explicativo de lo que implica seleccionar este motivo específico
    val agradecimiento: String, // Mensaje principal de agradecimiento mostrado tras completar el registro exitosamente
    val confirmacion: String // Plantilla de texto con formato (contiene %s) para personalizar el resumen según la oportunidad seleccionada
)

// Nombres de los motivos: "Aportar" pide además la frecuencia de la donación
const val MOTIVO_APORTAR = "Aportar" // Constante pública que define el identificador de texto para la opción de realizar aportes/donaciones
const val MOTIVO_INGRESAR = "Ingresar a Programa" // Constante pública que define el identificador para la opción de postular a un programa

val motivosRegistro = listOf( // Lista inmutable con las instancias predefinidas de motivos de registro que consume la interfaz de usuario
    MotivoRegistro( // Primera opción disponible: Aportar/Donar
        etiqueta = MOTIVO_APORTAR, // Asigna la etiqueta fija "Aportar"
        descripcion = "Serás contactado/a por la organización para coordinar tu aporte " + // Asigna la descripción informativa del flujo de aportes
                "a la oportunidad elegida.",
        agradecimiento = "¡Gracias por tu apoyo!", // Asigna el encabezado de agradecimiento para aportes
        confirmacion = "Registramos tu interés en aportar a «%s»." // Asigna la plantilla de confirmación con comodín %s para interpolar la oportunidad
    ),
    MotivoRegistro( // Segunda opción disponible: Postular/Ingresar a un programa
        etiqueta = MOTIVO_INGRESAR, // Asigna la etiqueta fija "Ingresar a Programa"
        descripcion = "Podrás ser parte de la oportunidad como usuario/a de la Escuela Jan Van Dijk " + // Asigna la descripción orientada a participantes
                "o del servicio de la Red Socio-Laboral Alba Lab.",
        agradecimiento = "¡Gracias por tu postulación!", // Asigna el encabezado de agradecimiento para postulaciones
        confirmacion = "Registramos tu postulación a «%s»." // Asigna la plantilla de confirmación con comodín %s para interpolar el programa
    )
)

val frecuenciasDonacion = listOf("Donación única", "Mensual", "Anual") // Lista inmutable con las opciones de periodicidad desplegadas cuando el motivo es "Aportar"
