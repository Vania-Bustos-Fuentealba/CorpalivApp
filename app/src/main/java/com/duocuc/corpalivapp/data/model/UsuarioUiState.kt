package com.duocuc.corpalivapp.data.model

/**
 * G11: modelo principal que representa el estado del formulario de registro.
 * Guarda a la vez los datos de "Persona" y de "Empresa": al cambiar de tipo no se pierde lo escrito.
 */
data class UsuarioUiState(
    // Tipo de formulario que se está mostrando
    val tipo: TipoRegistro = TipoRegistro.PERSONA,
    val persona: DatosPersona = DatosPersona(),
    val empresa: DatosEmpresa = DatosEmpresa(),

    // Título de la oportunidad elegida antes de abrir el formulario
    val oportunidad: String = "",

    // G12: estado del envío simulado (barra de carga) y aviso de que terminó
    val enviando: Boolean = false,
    val progreso: Float = 0f,
    val completado: Boolean = false,

    val errores: UsuarioErrores = UsuarioErrores()
) {
    // Es un aporte si la persona eligió "Aportar" o si el registro es de una empresa
    val esAporte: Boolean
        get() = tipo == TipoRegistro.EMPRESA || persona.motivo == MOTIVO_APORTAR
}

/** G11: tipos de formulario que se pueden elegir. */
enum class TipoRegistro(val etiqueta: String) {
    PERSONA("Persona"),
    EMPRESA("Empresa")
}

/** G11: datos de una persona. El teléfono guarda solo los 8 dígitos que van después del +569. */
data class DatosPersona(
    val nombre: String = "",
    val apellido: String = "",
    val run: String = "",
    val correo: String = "",
    val telefono: String = "",
    val motivo: String = "",
    // Solo se usa cuando el motivo es "Aportar"
    val frecuenciaDonacion: String = ""
)

/** G11: datos de una empresa. El teléfono guarda solo los 9 dígitos que van después del +56. */
data class DatosEmpresa(
    val razonSocial: String = "",
    val rut: String = "",
    val giro: String = "",
    val representante: String = "",
    val rutRepresentante: String = "",
    val correo: String = "",
    val telefono: String = "",
    val direccion: String = ""
)
