package com.duocuc.corpalivapp.data.model

/**
 * G11: motivos del registro de una persona (aportar o ingresar a un programa).
 * Se usan en el desplegable de RegistroScreen y en los textos de agradecimiento de ResumenScreen.
 * En [confirmacion], %s se reemplaza por el título de la oportunidad elegida.
 */
data class MotivoRegistro(
    val etiqueta: String,
    val descripcion: String,
    val agradecimiento: String,
    val confirmacion: String
)

// Nombres de los motivos: "Aportar" pide además la frecuencia de la donación
const val MOTIVO_APORTAR = "Aportar"
const val MOTIVO_INGRESAR = "Ingresar a Programa"

val motivosRegistro = listOf(
    MotivoRegistro(
        etiqueta = MOTIVO_APORTAR,
        descripcion = "Serás contactado/a por la organización para coordinar tu aporte " +
            "a la oportunidad elegida.",
        agradecimiento = "¡Gracias por tu apoyo!",
        confirmacion = "Registramos tu interés en aportar a «%s»."
    ),
    MotivoRegistro(
        etiqueta = MOTIVO_INGRESAR,
        descripcion = "Podrás ser parte de la oportunidad como usuario/a de la Escuela Jan Van Dijk " +
            "o del servicio de la Red Socio-Laboral Alba Lab.",
        agradecimiento = "¡Gracias por tu postulación!",
        confirmacion = "Registramos tu postulación a «%s»."
    )
)

val frecuenciasDonacion = listOf("Donación única", "Mensual", "Anual")
