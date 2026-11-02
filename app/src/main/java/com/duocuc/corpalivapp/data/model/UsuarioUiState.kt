package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece este archivo dentro de los modelos de datos de la app

/**
- G11: modelo principal que representa el estado del formulario de registro.

- Guarda a la vez los datos de "Persona" y de "Empresa": al cambiar de tipo no se pierde lo escrito.

 */
data class UsuarioUiState( // Data class que encapsula todo el estado de la UI del formulario de registro de usuario
    // Tipo de formulario que se está mostrando
    val tipo: TipoRegistro = TipoRegistro.PERSONA, // Tipo de registro activo (PERSONA o EMPRESA), por defecto PERSONA
    val persona: DatosPersona = DatosPersona(), // Instancia que contiene los campos ingresados para persona natural
    val empresa: DatosEmpresa = DatosEmpresa(), // Instancia que contiene los campos ingresados para persona jurídica/empresa

    // Título de la oportunidad elegida antes de abrir el formulario
    val oportunidad: String = "", // Guarda el título del programa u oportunidad seleccionada previamente

    // G12: estado del envío simulado (barra de carga) y aviso de que terminó
    val enviando: Boolean = false, // Indica si la operación de envío está en ejecución (muestra indicador de carga)
    val progreso: Float = 0f, // Representa el porcentaje de progreso de 0.0f a 1.0f para la barra de avance
    val completado: Boolean = false, // Marca si el proceso de registro se completó exitosamente

    val errores: UsuarioErrores = UsuarioErrores() // Contenedor con todos los errores de validación de los campos
) {
    // Es un aporte si la persona eligió "Aportar" o si el registro es de una empresa
    val esAporte: Boolean // Propiedad calculada que determina si el flujo corresponde a un aporte económico
        get() = tipo == TipoRegistro.EMPRESA || persona.motivo == MOTIVO_APORTAR // Retorna true si es una empresa o si la persona seleccionó el motivo Aportar
}

/** G11: tipos de formulario que se pueden elegir. */
enum class TipoRegistro(val etiqueta: String) { // Enum que define la categoría del solicitante
    PERSONA("Persona"), // Opción para persona natural
    EMPRESA("Empresa") // Opción para persona jurídica o empresa
}

/** G11: datos de una persona. El teléfono guarda solo los 8 dígitos que van después del +569. */
data class DatosPersona( // Data class que almacena las entradas de texto del formulario de persona natural
    val nombre: String = "", // Nombres de la persona
    val apellido: String = "", // Apellidos de la persona
    val run: String = "", // Rol Único Nacional (RUN/RUT)
    val correo: String = "", // Dirección de correo electrónico
    val telefono: String = "", // Número telefónico (8 dígitos tras el prefijo)
    val motivo: String = "", // Motivo de registro ("Aportar" o "Ingresar a Programa")
    // Solo se usa cuando el motivo es "Aportar"
    val frecuenciaDonacion: String = "" // Periodicidad de la donación ("Donación única", "Mensual", "Anual")
)

/** G11: datos de una empresa. El teléfono guarda solo los 9 dígitos que van después del +56. */
data class DatosEmpresa( // Data class que almacena las entradas de texto del formulario empresarial
    val razonSocial: String = "", // Razón social o nombre legal de la empresa
    val rut: String = "", // Rol Único Tributario (RUT) de la empresa
    val giro: String = "", // Giro o actividad comercial de la organización
    val representante: String = "", // Nombre completo del representante legal
    val rutRepresentante: String = "", // RUT personal del representante legal
    val correo: String = "", // Correo de contacto institucional
    val telefono: String = "", // Teléfono comercial de contacto
    val direccion: String = "" // Dirección física o domicilio tributario
)
