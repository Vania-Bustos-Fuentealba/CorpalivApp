package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas unitarias locales

import com.duocuc.corpalivapp.util.esRutValido // Importación de la función utilitaria para validar el RUT chileno
import com.duocuc.corpalivapp.util.formatearRut // Importación de la función utilitaria para dar formato visual al RUT (guion)
import com.duocuc.corpalivapp.util.normalizarRut // Importación de la función utilitaria para limpiar y normalizar la cadena del RUT
import org.junit.Assert.assertEquals // Importación de aserción para comprobar la igualdad de dos valores
import org.junit.Assert.assertFalse // Importación de aserción para verificar que una condición sea falsa
import org.junit.Assert.assertTrue // Importación de aserción para verificar que una condición sea verdadera
import org.junit.Test // Anotación de JUnit para identificar métodos que corresponden a casos de prueba

/**
- GUÍA 11 - Pruebas de la validación de RUT. Se usan RUT de ejemplo, no de personas reales.

- Por ahora la app solo revisa el formato; el dígito verificador se prueba con verificarDigito = true.

 */
class RutUtilsTest { // Clase contenedor para las pruebas unitarias de las funciones de utilidad relativas al RUT

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun rutConFormatoCorrecto_seAcepta_aunqueElDigitoNoCoincida() { // Valida que se acepten RUT con formato estructural válido cuando verificarDigito es false
        assertTrue(esRutValido("45678987-3")) // Verifica que un RUT con guion y formato de largo válido retorne true
        assertTrue(esRutValido("45.678.987-3")) // Verifica que se acepte un RUT que incluya puntos y guion
        assertTrue(esRutValido("456789873")) // Verifica que se acepte un RUT continuo de solo dígitos numéricos
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun rutConDigitoK_seAcepta() { // Comprueba la correcta aceptación del dígito verificador 'K' tanto en mayúscula como en minúscula
        assertTrue(esRutValido("6.000.000-K")) // Afirma verdadero para el dígito 'K' en mayúscula
        assertTrue(esRutValido("6000000-k")) // Afirma verdadero para el dígito 'k' en minúscula
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun rutConFormatoIncorrecto_noSeAcepta() { // Prueba que cadenas vacías, muy cortas o con caracteres no permitidos sean rechazadas
        assertFalse(esRutValido("")) // Afirma falso para una cadena vacía
        assertFalse(esRutValido("1-9")) // Afirma falso para un RUT demasiado corto
        assertFalse(esRutValido("ABCDEFGH-1")) // Afirma falso para un cuerpo que contiene letras no numéricas
        assertFalse(esRutValido("12345678-Z")) // Afirma falso para un dígito verificador distinto de número o 'K'
        assertFalse(esRutValido("1234-5")) // Afirma falso para una longitud de cuerpo insuficiente
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun conVerificacionDeDigito_seRechazaUnDigitoQueNoCoincida() { // Prueba el cálculo estricto por Módulo 11 activando verificarDigito = true
        assertFalse(esRutValido("45678987-3", verificarDigito = true)) // Rechaza el RUT si el dígito verificador calculado difiere del ingresado ('3')
        assertTrue(esRutValido("45678987-0", verificarDigito = true)) // Acepta el RUT cuando el dígito verificador coincide con el Módulo 11 ('0')
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun conVerificacionDeDigito_validaUnoNumericoKYCero() { // Evalúa casos bordes de validación matemática (dígitos 1..9, 'K' y '0')
        assertTrue(esRutValido("11.111.111-1", verificarDigito = true)) // Valida un RUT cuyo DV esperado es '1'
        assertTrue(esRutValido("12.345.678-5", verificarDigito = true)) // Valida un RUT cuyo DV esperado es '5'
        assertTrue(esRutValido("6.000.000-K", verificarDigito = true)) // Valida un RUT cuyo DV esperado equivale a 'K' (residuo 10)
        assertTrue(esRutValido("5.000.006-0", verificarDigito = true)) // Valida un RUT cuyo DV esperado equivale a '0' (residuo 11)
        assertFalse(esRutValido("11.111.111-2", verificarDigito = true)) // Rechaza un RUT cuando el DV provisto es erróneo
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun formatearRut_poneElGuionAntesDelUltimoCaracter() { // Comprueba la inserción dinámica del guion de separación
        assertEquals("12345678-5", formatearRut("123456785")) // Verifica que inserte un guion antes del último dígito
        assertEquals("12345678-5", formatearRut("12.345.678-5")) // Verifica que limpie puntos y reformaté correctamente
        assertEquals("6000000-K", formatearRut("6000000k")) // Verifica que inserte guion y transforme el DV 'k' a mayúscula
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun formatearRut_limitaElLargo_yNoFormateaUnSoloCaracter() { // Prueba límites de longitud y comportamiento con entradas mínimas
        assertEquals("1", formatearRut("1")) // Comprueba que un único carácter no sufra formateo con guion
        assertEquals("12345678-5", formatearRut("123456785999")) // Comprueba el recorte automático al máximo de 9 caracteres
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun normalizarRut_quitaPuntosGuionYEspacios() { // Prueba la limpieza total de caracteres de formato
        assertEquals("12345678K", normalizarRut("12.345.678-k ")) // Verifica la eliminación de puntos, guiones, espacios y la conversión a mayúscula
    }
}