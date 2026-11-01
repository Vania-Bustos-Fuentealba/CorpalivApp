package com.duocuc.corpalivapp.data.model

/**
 * G12: aporte guardado en el dispositivo para mostrarlo en el Dashboard.
 * Sirve para personas que aportan y para empresas; los campos que no aplican quedan vacíos.
 * Todos tienen valor por defecto para que Gson pueda leerlos desde el JSON guardado.
 */
data class RegistroGuardado(
    // Fecha y hora del registro en milisegundos (también identifica el registro)
    val id: Long = 0L,
    // "PERSONA" o "EMPRESA" (nombre de TipoRegistro)
    val tipo: String = "",
    val oportunidad: String = "",
    // Nombre completo de la persona o razón social de la empresa
    val nombre: String = "",
    // RUN de la persona o RUT de la empresa
    val rut: String = "",
    val correo: String = "",
    // Teléfono completo con el prefijo, ej: +569 12345678
    val telefono: String = "",
    // Frecuencia del aporte (persona) o "Aporte empresarial" (empresa)
    val tipoAporte: String = "",
    // Solo empresas
    val direccion: String = "",
    val giro: String = "",
    val representante: String = "",
    val rutRepresentante: String = ""
)
