package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro del modelo de datos de la app

/**
- G11: Errores de validación por campo, separados por tipo de formulario.

- Cada propiedad es null cuando el campo es válido, o el mensaje a mostrar si no lo es.

 */
data class UsuarioErrores( // Data class contenedora principal que agrupa las estructuras de errores para personas y empresas
    val persona: ErroresPersona = ErroresPersona(), // Instancia con los errores del formulario de persona natural (por defecto vacía/sin errores)
    val empresa: ErroresEmpresa = ErroresEmpresa() // Instancia con los errores del formulario de persona jurídica/empresa (por defecto vacía/sin errores)
)

data class ErroresPersona( // Data class que representa el conjunto de mensajes de error de los campos de persona natural
    val nombre: String? = null, // Mensaje de error para el nombre (null si es válido)
    val apellido: String? = null, // Mensaje de error para el apellido (null si es válido)
    val run: String? = null, // Mensaje de error para el RUN (null si es válido)
    val correo: String? = null, // Mensaje de error para el correo electrónico (null si es válido)
    val telefono: String? = null, // Mensaje de error para el número telefónico (null si es válido)
    val motivo: String? = null, // Mensaje de error para la selección del motivo (null si es válido)
    val frecuencia: String? = null // Mensaje de error para la frecuencia de donación (null si es válido)
)

data class ErroresEmpresa( // Data class que representa el conjunto de mensajes de error de los campos de empresa
    val razonSocial: String? = null, // Mensaje de error para la razón social (null si es válida)
    val rut: String? = null, // Mensaje de error para el RUT de la empresa (null si es válido)
    val giro: String? = null, // Mensaje de error para el giro o actividad comercial (null si es válido)
    val representante: String? = null, // Mensaje de error para el nombre del representante legal (null si es válido)
    val rutRepresentante: String? = null, // Mensaje de error para el RUT del representante legal (null si es válido)
    val correo: String? = null, // Mensaje de error para el correo de la empresa (null si es válido)
    val telefono: String? = null, // Mensaje de error para el teléfono de la empresa (null si es válido)
    val direccion: String? = null // Mensaje de error para la dirección comercial (null si es válida)
)
