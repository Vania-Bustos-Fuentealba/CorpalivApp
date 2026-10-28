package com.duocuc.corpalivapp.data.model

/**
 * G11: motivos del registro (aportar o postular).
 * Se usan en el desplegable de RegistroScreen y en los textos de agradecimiento de ResumenScreen.
 * En [confirmacion], %s se reemplaza por el título de la oportunidad elegida.
 */
data class MotivoRegistro(
    val etiqueta: String,
    val descripcion: String,
    val agradecimiento: String,
    val confirmacion: String
)

val motivosRegistro = listOf(
    MotivoRegistro(
        etiqueta = "Aportar a la oportunidad",
        descripcion = "Serás contactado/a por la organización para coordinar tu aporte económico, " +
            "mensual o anual, a la oportunidad elegida.",
        agradecimiento = "¡Gracias por tu apoyo!",
        confirmacion = "Registramos tu interés en aportar a «%s»."
    ),
    MotivoRegistro(
        etiqueta = "Postular a la oportunidad",
        descripcion = "Podrás ser parte de la oportunidad como usuario/a de la Escuela Jan Van Dijk " +
            "o del servicio de la Red Socio-Laboral Alba Lab.",
        agradecimiento = "¡Gracias por tu postulación!",
        confirmacion = "Registramos tu postulación a «%s»."
    )
)
