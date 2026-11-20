package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenecen los objetos de transferencia de datos (DTO) de oportunidades

/**
- PARCIAL 4: cuerpo que la app envía a POST y PUT /api/oportunidades (oportunidades-service).

- Solo los administradores pueden crear, modificar o eliminar oportunidades.

- Los campos opcionales viajan solo si tienen valor (Gson no envía los null).

 */
data class OportunidadRequestDto( // Cuerpo de la petición para crear o actualizar una oportunidad
    val titulo: String, // Título del programa o proyecto
    val categoria: String, // Categoría (ej. "RSE Empresarial", "Donación Directa")
    val descripcion: String, // Detalle de los objetivos del proyecto
    val impactoSocial: String, // Impacto generado en la comunidad
    val imagenUrl: String? = null, // URL opcional de la imagen
    val contactoEmail: String? = null, // Correo de contacto opcional
    val metaFinanciamiento: Int, // Monto objetivo en pesos
    val montoRecaudado: Int // Monto recaudado hasta la fecha
)
