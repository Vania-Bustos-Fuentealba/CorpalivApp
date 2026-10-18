package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GUÍA 11 - Pruebas unitarias de las validaciones del formulario (JVM, sin emulador).
 */
class UsuarioViewModelTest {

    private fun viewModelCompleto() = UsuarioViewModel().apply {
        onNombreChange("Benjamín")
        onCorreoChange("correo@ejemplo.cl")
        onClaveChange("123456")
        onDireccionChange("Av. Antonio Varas 666")
        onAceptarTerminosChange(true)
    }

    @Test
    fun formularioVacio_noEsValido_yMarcaCadaError() {
        val vm = UsuarioViewModel()

        assertFalse(vm.validarFormulario())

        val errores = vm.estado.value.errores
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Correo inválido", errores.correo)
        assertEquals("Debe tener al menos 6 caracteres", errores.clave)
        assertEquals("Campo obligatorio", errores.direccion)
        assertEquals("Debes aceptar los términos y condiciones", errores.terminos)
    }

    @Test
    fun formularioCompleto_esValido() {
        val vm = viewModelCompleto()

        assertTrue(vm.validarFormulario())
        assertNull(vm.estado.value.errores.correo)
    }

    @Test
    fun correoSinDominio_esInvalido() {
        val vm = viewModelCompleto()
        vm.onCorreoChange("correo@")

        assertFalse(vm.validarFormulario())
        assertEquals("Correo inválido", vm.estado.value.errores.correo)
    }

    @Test
    fun errorSeLimpiaEnTiempoReal_cuandoElCampoQuedaCorrecto() {
        val vm = UsuarioViewModel()
        vm.validarFormulario()

        vm.onClaveChange("123456")

        assertNull(vm.estado.value.errores.clave)
    }

    @Test
    fun reiniciar_dejaElFormularioVacio() {
        val vm = viewModelCompleto()

        vm.reiniciar()

        assertEquals("", vm.estado.value.nombre)
        assertFalse(vm.estado.value.aceptaTerminos)
    }

    @Test
    fun interesVacio_noEsValido_yMarcaCadaError() {
        val vm = UsuarioViewModel()

        assertFalse(vm.validarInteres())

        val errores = vm.estado.value.errores
        assertEquals("Selecciona una modalidad", errores.modalidad)
        assertEquals("Selecciona un tipo de entidad", errores.tipoEntidad)
        assertEquals("Debe tener al menos 10 caracteres", errores.mensaje)
    }

    @Test
    fun interesCompleto_esValido() {
        val vm = UsuarioViewModel()
        vm.onModalidadChange("Donación")
        vm.onTipoEntidadChange("Empresa")
        vm.onMensajeChange("Queremos apoyar el taller")

        assertTrue(vm.validarInteres())
    }

    @Test
    fun validarInteres_noBorraLosErroresDelRegistro() {
        val vm = UsuarioViewModel()
        vm.validarFormulario()

        vm.validarInteres()

        assertEquals("Campo obligatorio", vm.estado.value.errores.nombre)
    }

    @Test
    fun enviar_marcaElEnvio_yReiniciarLoLimpia() {
        val vm = UsuarioViewModel()

        vm.enviar()
        assertTrue(vm.estado.value.enviado)

        vm.reiniciar()
        assertFalse(vm.estado.value.enviado)
    }
}
