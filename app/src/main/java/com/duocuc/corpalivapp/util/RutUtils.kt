package com.duocuc.corpalivapp.util // Declaración del paquete al que pertenece este archivo dentro de la capa de utilidades del proyecto

/**
- G11: Utilidades para validar un RUT chileno (sin conexión y sin guardar el dato).

 */

/** Quita puntos, guion y espacios, y deja la K en mayúscula. */
fun normalizarRut(rut: String): String = rut.filter { it.isLetterOrDigit() }.uppercase() // Filtra la cadena conservando solo letras y dígitos y convierte el resultado a mayúsculas

/**
- Da formato XXXXXXXX-X mientras se escribe: deja solo dígitos y K (máximo 9 caracteres)

- y separa el último con un guion.

 */
fun formatearRut(entrada: String): String { // Función para formatear dinámicamente la entrada del RUT con un guion antes del dígito verificador
    val limpio = entrada.filter { it.isDigit() || it == 'k' || it == 'K' }.uppercase().take(9) // Conserva solo dígitos o caracteres 'K', convierte a mayúsculas y limita a 9 caracteres
    return if (limpio.length <= 1) limpio else limpio.dropLast(1) + "-" + limpio.last() // Si la longitud es mayor a 1, inserta un guion previo al último carácter (dígito verificador)
}

// Por ahora el RUT solo se valida por formato (ej: 45678987-3 se acepta aunque su dígito
// verificador no coincida). Cambiar a true para volver a exigir el módulo 11.
const val VERIFICAR_DIGITO_VERIFICADOR = false // Constante global que habilita o inhabilita la validación matemática estricta por Módulo 11

/**
- Valida un RUT: 7 u 8 dígitos más un dígito o K al final.

- Si
[verificarDigito] es true, además comprueba el dígito verificador (módulo 11).
 */
fun esRutValido(rut: String, verificarDigito: Boolean = VERIFICAR_DIGITO_VERIFICADOR): Boolean { // Valida si una cadena de texto corresponde a la estructura de un RUT chileno válido
    val limpio = normalizarRut(rut) // Limpia y normaliza el RUT eliminando puntos, guiones y espacios
    if (limpio.length !in 8..9) return false // Retorna false si la longitud total del cuerpo + DV no está entre 8 y 9 caracteres

    val cuerpo = limpio.dropLast(1) // Extrae la parte numérica principal del RUT (removiendo el último carácter)
    val digitoIngresado = limpio.last() // Extrae el último carácter correspondiente al dígito verificador
    if (!cuerpo.all { it.isDigit() }) return false // Verifica que todos los caracteres de la parte numérica sean dígitos del 0 al 9
    if (!digitoIngresado.isDigit() && digitoIngresado != 'K') return false // Verifica que el dígito verificador sea un número o la letra 'K'

    return !verificarDigito || calcularDigitoVerificador(cuerpo) == digitoIngresado // Si verificarDigito es true, compara el DV ingresado con el resultado calculado por Módulo 11
}

// Multiplica los dígitos (de derecha a izquierda) por la serie 2..7 y obtiene el verificador
private fun calcularDigitoVerificador(cuerpo: String): Char { // Calcula el dígito verificador esperado mediante la fórmula Módulo 11
    var suma = 0 // Variable acumuladora para la suma ponderada
    var factor = 2 // Serie de multiplicación que alterna de 2 a 7
    for (digito in cuerpo.reversed()) { // Recorre los números del cuerpo de derecha a izquierda
        suma += digito.digitToInt() * factor // Multiplica el dígito actual por el factor de la serie y lo acumula en la suma
        factor = if (factor == 7) 2 else factor + 1 // Incrementa el factor o lo reinicia a 2 si llega a 7
    }
    return when (val resultado = 11 - (suma % 11)) { // Corrige la sintaxis calculando la diferencia del Módulo 11
        11 -> '0' // Si el resultado final del residuo es 11, el dígito verificador corresponde al carácter '0'
        10 -> 'K' // Si el resultado final del residuo es 10, el dígito verificador corresponde al carácter 'K'
        else -> resultado.digitToChar() // Para cualquier otro valor numérico entre 1 y 9, lo convierte directamente a carácter
    }
}