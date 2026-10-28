package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.util.esRutValido
import com.duocuc.corpalivapp.util.normalizarRut
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GUÍA 11 - Pruebas de la validación de RUT (módulo 11). Se usan RUT de ejemplo, no de personas reales.
 */
class RutUtilsTest {

    @Test
    fun rutValido_conPuntosYGuion() {
        assertTrue(esRutValido("11.111.111-1"))
        assertTrue(esRutValido("12.345.678-5"))
    }

    @Test
    fun rutValido_sinFormato() {
        assertTrue(esRutValido("123456785"))
    }

    @Test
    fun rutValido_conDigitoK_enMayusculaOMinuscula() {
        assertTrue(esRutValido("6.000.000-K"))
        assertTrue(esRutValido("6000000-k"))
    }

    @Test
    fun rutValido_conDigitoCero() {
        assertTrue(esRutValido("5.000.006-0"))
    }

    @Test
    fun rutInvalido_conDigitoVerificadorIncorrecto() {
        assertFalse(esRutValido("11.111.111-2"))
    }

    @Test
    fun rutInvalido_conLargoOCaracteresIncorrectos() {
        assertFalse(esRutValido(""))
        assertFalse(esRutValido("1-9"))
        assertFalse(esRutValido("ABCDEFGH-1"))
    }

    @Test
    fun normalizarRut_quitaPuntosGuionYEspacios() {
        assertEquals("12345678K", normalizarRut("12.345.678-k "))
    }
}
