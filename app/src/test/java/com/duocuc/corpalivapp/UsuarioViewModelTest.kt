package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GUÍA 11 - Pruebas unitarias de las validaciones del formulario (JVM, sin emulador).
 * Los datos son ficticios (RUT de ejemplo de uso común en pruebas).
 */
class UsuarioViewModelTest {

    private fun viewModelCompleto() = UsuarioViewModel().apply {
        onNombreChange("Benjamín")
        onApellidoChange("Pérez")
        onRutChange("11.111.111-1")
        onCorreoChange("correo@ejemplo.cl")
        onDireccionChange("Av. Antonio Varas 666")
        onMotivoChange("Aportar a la oportunidad")
    }

    @Test
    fun formularioVacio_noEsValido_yMarcaCadaError() {
        val vm = UsuarioViewModel()

        assertFalse(vm.validarFormulario())

        val errores = vm.estado.value.errores
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Campo obligatorio", errores.apellido)
        assertEquals("Campo obligatorio", errores.rut)
        assertEquals("Correo inválido", errores.correo)
        assertEquals("Campo obligatorio", errores.direccion)
        assertEquals("Selecciona un motivo", errores.motivo)
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
    fun rutConDigitoVerificadorIncorrecto_esInvalido() {
        val vm = viewModelCompleto()
        vm.onRutChange("11.111.111-2")

        assertFalse(vm.validarFormulario())
        assertEquals("RUT inválido", vm.estado.value.errores.rut)
    }

    @Test
    fun nombreMuyCorto_esInvalido() {
        val vm = viewModelCompleto()
        vm.onNombreChange("Al")

        assertFalse(vm.validarFormulario())
        assertEquals("Debe tener al menos 3 caracteres", vm.estado.value.errores.nombre)
    }

    @Test
    fun errorSeLimpiaEnTiempoReal_cuandoElCampoQuedaCorrecto() {
        val vm = UsuarioViewModel()
        vm.validarFormulario()

        vm.onRutChange("12345678-5")

        assertNull(vm.estado.value.errores.rut)
    }

    @Test
    fun elegirMotivo_limpiaSuError() {
        val vm = UsuarioViewModel()
        vm.validarFormulario()

        vm.onMotivoChange("Postular a la oportunidad")

        assertNull(vm.estado.value.errores.motivo)
    }

    @Test
    fun seleccionarOportunidad_guardaElTituloYDejaElFormularioVacio() {
        val vm = viewModelCompleto()

        vm.seleccionarOportunidad("Taller de carpintería")

        assertEquals("Taller de carpintería", vm.estado.value.oportunidad)
        assertEquals("", vm.estado.value.nombre)
        assertEquals("", vm.estado.value.motivo)
    }

    @Test
    fun reiniciar_dejaElFormularioVacio() {
        val vm = viewModelCompleto()

        vm.reiniciar()

        assertEquals("", vm.estado.value.nombre)
        assertEquals("", vm.estado.value.oportunidad)
        assertFalse(vm.estado.value.completado)
    }
}
