package com.duocuc.corpalivapp.util

/**
 * G11: Utilidades para validar un RUT chileno (sin conexión y sin guardar el dato).
 */

/** Quita puntos, guion y espacios, y deja la K en mayúscula. */
fun normalizarRut(rut: String): String = rut.filter { it.isLetterOrDigit() }.uppercase()

/**
 * Da formato XXXXXXXX-X mientras se escribe: deja solo dígitos y K (máximo 9 caracteres)
 * y separa el último con un guion.
 */
fun formatearRut(entrada: String): String {
    val limpio = entrada.filter { it.isDigit() || it == 'k' || it == 'K' }.uppercase().take(9)
    return if (limpio.length <= 1) limpio else limpio.dropLast(1) + "-" + limpio.last()
}

// Por ahora el RUT solo se valida por formato (ej: 45678987-3 se acepta aunque su dígito
// verificador no coincida). Cambiar a true para volver a exigir el módulo 11.
const val VERIFICAR_DIGITO_VERIFICADOR = false

/**
 * Valida un RUT: 7 u 8 dígitos más un dígito o K al final.
 * Si [verificarDigito] es true, además comprueba el dígito verificador (módulo 11).
 */
fun esRutValido(rut: String, verificarDigito: Boolean = VERIFICAR_DIGITO_VERIFICADOR): Boolean {
    val limpio = normalizarRut(rut)
    if (limpio.length !in 8..9) return false

    val cuerpo = limpio.dropLast(1)
    val digitoIngresado = limpio.last()
    if (!cuerpo.all { it.isDigit() }) return false
    if (!digitoIngresado.isDigit() && digitoIngresado != 'K') return false

    return !verificarDigito || calcularDigitoVerificador(cuerpo) == digitoIngresado
}

// Multiplica los dígitos (de derecha a izquierda) por la serie 2..7 y obtiene el verificador
private fun calcularDigitoVerificador(cuerpo: String): Char {
    var suma = 0
    var factor = 2
    for (digito in cuerpo.reversed()) {
        suma += digito.digitToInt() * factor
        factor = if (factor == 7) 2 else factor + 1
    }
    return when (val resultado = 11 - suma % 11) {
        11 -> '0'
        10 -> 'K'
        else -> resultado.digitToChar()
    }
}
