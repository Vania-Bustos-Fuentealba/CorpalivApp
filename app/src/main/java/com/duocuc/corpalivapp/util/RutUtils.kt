package com.duocuc.corpalivapp.util

/**
 * G11: Utilidades para validar un RUT chileno (sin conexión y sin guardar el dato).
 */

/** Quita puntos, guion y espacios, y deja la K en mayúscula. */
fun normalizarRut(rut: String): String = rut.filter { it.isLetterOrDigit() }.uppercase()

/** Valida un RUT: 7 u 8 dígitos más el dígito verificador (módulo 11). */
fun esRutValido(rut: String): Boolean {
    val limpio = normalizarRut(rut)
    if (limpio.length !in 8..9) return false

    val cuerpo = limpio.dropLast(1)
    val digitoIngresado = limpio.last()
    if (!cuerpo.all { it.isDigit() }) return false

    return calcularDigitoVerificador(cuerpo) == digitoIngresado
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
