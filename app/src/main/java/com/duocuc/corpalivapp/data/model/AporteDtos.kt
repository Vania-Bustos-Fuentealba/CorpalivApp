package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenecen los objetos de transferencia de datos (DTO) de aportes

/**
- PARCIAL 4: cuerpo que la app envía a POST /api/aportes (aportes-service).

- Sirve para aportes de personas y empresas y para postulaciones de personas a un programa.

- Los campos opcionales viajan solo si tienen valor (Gson no envía los null).

 */
data class AporteRequestDto( // Cuerpo de la petición de registro
    val tipo: String, // "PERSONA" o "EMPRESA" (nombre de TipoRegistro)
    val motivo: String, // "APORTE" o "POSTULACION" (la postulación es solo para personas)
    val oportunidad: String, // Título de la oportunidad o programa elegido
    val nombre: String, // Nombre completo de la persona o razón social de la empresa
    val rut: String, // RUN o RUT con formato XXXXXXXX-X
    val correo: String, // Correo de contacto
    val telefono: String, // Teléfono con prefijo, por ejemplo +569 12345678
    val tipoAporte: String? = null, // Frecuencia del aporte de una persona (no aplica a postulaciones ni empresas)
    val direccion: String? = null, // Solo empresas
    val giro: String? = null, // Solo empresas
    val representante: String? = null, // Solo empresas
    val rutRepresentante: String? = null // Solo empresas
)

/** Cuerpo de PATCH /api/aportes/{id}/estado: "PENDIENTE" o "CONTACTADO". */
data class EstadoRequestDto( // Cuerpo de la petición de cambio de estado
    val estado: String // Nuevo estado del registro
)
