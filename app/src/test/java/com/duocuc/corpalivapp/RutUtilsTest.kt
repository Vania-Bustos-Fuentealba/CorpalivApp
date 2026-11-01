package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.util.esRutValido
import com.duocuc.corpalivapp.util.formatearRut
import com.duocuc.corpalivapp.util.normalizarRut
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GUÍA 11 - Pruebas de la validación de RUT. Se usan RUT de ejemplo, no de personas reales.
 * Por ahora la app solo revisa el formato; el dígito verificador se prueba con verificarDigito = true.
 */
class RutUtilsTest {

    @Test
    fun rutConFormatoCorrecto_seAcepta_aunqueElDigitoNoCoincida() {
        assertTrue(esRutValido("45678987-3"))
        assertTrue(esRutValido("45.678.987-3"))
        assertTrue(esRutValido("456789873"))
    }

    @Test
    fun rutConDigitoK_seAcepta() {
        assertTrue(esRutValido("6.000.000-K"))
        assertTrue(esRutValido("6000000-k"))
    }

    @Test
    fun rutConFormatoIncorrecto_noSeAcepta() {
        assertFalse(esRutValido(""))
        assertFalse(esRutValido("1-9"))
        assertFalse(esRutValido("ABCDEFGH-1"))
        assertFalse(esRutValido("12345678-Z"))
        assertFalse(esRutValido("1234-5"))
    }

    @Test
    fun conVerificacionDeDigito_seRechazaUnDigitoQueNoCoincide() {
        assertFalse(esRutValido("45678987-3", verificarDigito = true))
        assertTrue(esRutValido("45678987-0", verificarDigito = true))
    }

    @Test
    fun conVerificacionDeDigito_validaUnoNumericoKYCero() {
        assertTrue(esRutValido("11.111.111-1", verificarDigito = true))
        assertTrue(esRutValido("12.345.678-5", verificarDigito = true))
        assertTrue(esRutValido("6.000.000-K", verificarDigito = true))
        assertTrue(esRutValido("5.000.006-0", verificarDigito = true))
        assertFalse(esRutValido("11.111.111-2", verificarDigito = true))
    }

    @Test
    fun formatearRut_poneElGuionAntesDelUltimoCaracter() {
        assertEquals("12345678-5", formatearRut("123456785"))
        assertEquals("12345678-5", formatearRut("12.345.678-5"))
        assertEquals("6000000-K", formatearRut("6000000k"))
    }

    @Test
    fun formatearRut_limitaElLargo_yNoFormateaUnSoloCaracter() {
        assertEquals("1", formatearRut("1"))
        assertEquals("12345678-5", formatearRut("123456785999"))
    }

    @Test
    fun normalizarRut_quitaPuntosGuionYEspacios() {
        assertEquals("12345678K", normalizarRut("12.345.678-k "))
    }
}
