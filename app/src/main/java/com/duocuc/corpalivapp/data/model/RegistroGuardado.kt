package com.duocuc.corpalivapp.data.model // Declaración del paquete al que pertenece esta data class dentro de los modelos de datos de la app

/**
- G12: aporte guardado en el dispositivo para mostrarlo en el Dashboard.

- Sirve para personas que aportan y para empresas; los campos que no aplican quedan vacíos.

- Todos tienen valor por defecto para que Gson pueda leerlos desde el JSON guardado.

 */
data class RegistroGuardado( // Data class que representa un registro de aporte persistido localmente en DataStore
    // Fecha y hora del registro en milisegundos (también identifica el registro)
    val id: Long = 0L, // Marca de tiempo en milisegundos usada como identificador único con valor por defecto 0L
    // "PERSONA" o "EMPRESA" (nombre de TipoRegistro)
    val tipo: String = "", // Tipo de aportante ("PERSONA" o "EMPRESA") inicializado como cadena vacía
    val oportunidad: String = "", // Nombre de la oportunidad o programa al que se destina el aporte
    // Nombre completo de la persona o razón social de la empresa
    val nombre: String = "", // Nombre del aportante individual o razón social de la persona jurídica
    // RUN de la persona o RUT de la empresa
    val rut: String = "", // Identificador tributario (RUT/RUN) del aportante
    val correo: String = "", // Dirección de correo electrónico de contacto
    // Teléfono completo con el prefijo, ej: +569 12345678
    val telefono: String = "", // Número telefónico de contacto con formato de red
    // Frecuencia del aporte (persona) o "Aporte empresarial" (empresa)
    val tipoAporte: String = "", // Modalidad o periodicidad de la contribución
    // Solo empresas
    val direccion: String = "", // Dirección física o domicilio tributario (usado principalmente por empresas)
    val giro: String = "", // Actividad o giro comercial (exclusivo para aportes empresariales)
    val representante: String = "", // Nombre completo del representante legal de la organización
    val rutRepresentante: String = "" // RUT del representante legal de la empresa
)
