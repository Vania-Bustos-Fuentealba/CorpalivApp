package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.data.model.TipoRegistro
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GUÍA 11 - Pruebas unitarias de las validaciones del formulario (JVM, sin emulador).
 * Los datos son ficticios (RUN y RUT de ejemplo de uso común en pruebas).
 */
class UsuarioViewModelTest {

    private fun personaCompleta() = UsuarioViewModel().apply {
        onNombreChange("Benjamín")
        onApellidoChange("Pérez")
        onRunChange("11.111.111-1")
        onCorreoChange("correo@ejemplo.cl")
        onTelefonoChange("12345678")
        onMotivoChange("Ingresar a Programa")
    }

    private fun empresaCompleta() = UsuarioViewModel().apply {
        onTipoChange(TipoRegistro.EMPRESA)
        onRazonSocialChange("Fundación Ejemplo SpA")
        onRutEmpresaChange("76.543.210-3")
        onGiroChange("Servicios de ejemplo")
        onRepresentanteChange("Ana Ejemplo")
        onRutRepresentanteChange("12345678-5")
        onCorreoEmpresaChange("contacto@ejemplo.cl")
        onTelefonoEmpresaChange("212345678")
        onDireccionEmpresaChange("Av. Providencia 1234")
    }

    @Test
    fun personaVacia_noEsValida_yMarcaCadaError() {
        val vm = UsuarioViewModel()

        assertFalse(vm.validarFormulario())

        val errores = vm.estado.value.errores.persona
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Campo obligatorio", errores.apellido)
        assertEquals("Campo obligatorio", errores.run)
        assertEquals("Correo inválido", errores.correo)
        assertEquals("Campo obligatorio", errores.telefono)
        assertEquals("Selecciona un motivo", errores.motivo)
        assertNull(errores.frecuencia)
    }

    @Test
    fun personaCompleta_esValida() {
        val vm = personaCompleta()

        assertTrue(vm.validarFormulario())
    }

    @Test
    fun correoSinDominio_esInvalido() {
        val vm = personaCompleta()
        vm.onCorreoChange("correo@")

        assertFalse(vm.validarFormulario())
        assertEquals("Correo inválido", vm.estado.value.errores.persona.correo)
    }

    @Test
    fun runConFormatoCorrecto_seAcepta_aunqueElDigitoNoCoincida() {
        val vm = personaCompleta()
        vm.onRunChange("45678987-3")

        assertTrue(vm.validarFormulario())
    }

    @Test
    fun runConFormatoIncorrecto_esInvalido() {
        val vm = personaCompleta()
        vm.onRunChange("1234")

        assertFalse(vm.validarFormulario())
        assertEquals("RUN inválido", vm.estado.value.errores.persona.run)
    }

    @Test
    fun runSeFormateaAlEscribir() {
        val vm = UsuarioViewModel()

        vm.onRunChange("123456785")

        assertEquals("12345678-5", vm.estado.value.persona.run)
    }

    @Test
    fun telefono_soloAceptaDigitos_yMaximoOchoEnPersona() {
        val vm = UsuarioViewModel()

        vm.onTelefonoChange("12ab34 5678 99")

        assertEquals("12345678", vm.estado.value.persona.telefono)
    }

    @Test
    fun telefonoIncompleto_esInvalido() {
        val vm = personaCompleta()
        vm.onTelefonoChange("1234")

        assertFalse(vm.validarFormulario())
        assertEquals("Ingresa los 8 dígitos del celular", vm.estado.value.errores.persona.telefono)
    }

    @Test
    fun aportar_exigeFrecuencia() {
        val vm = personaCompleta()
        vm.onMotivoChange("Aportar")

        assertFalse(vm.validarFormulario())
        assertEquals("Selecciona la frecuencia", vm.estado.value.errores.persona.frecuencia)

        vm.onFrecuenciaChange("Mensual")

        assertTrue(vm.validarFormulario())
    }

    @Test
    fun ingresarAPrograma_noExigeFrecuencia() {
        val vm = personaCompleta()

        assertTrue(vm.validarFormulario())
        assertNull(vm.estado.value.errores.persona.frecuencia)
    }

    @Test
    fun alCambiarAIngresarAPrograma_seQuitaElErrorDeFrecuencia() {
        val vm = personaCompleta()
        vm.onMotivoChange("Aportar")
        vm.validarFormulario()

        vm.onMotivoChange("Ingresar a Programa")

        assertNull(vm.estado.value.errores.persona.frecuencia)
        assertTrue(vm.validarFormulario())
    }

    @Test
    fun errorSeLimpiaEnTiempoReal_cuandoElCampoQuedaCorrecto() {
        val vm = UsuarioViewModel()
        vm.validarFormulario()

        vm.onRunChange("12345678-5")

        assertNull(vm.estado.value.errores.persona.run)
    }

    @Test
    fun cambiarDeTipo_conservaLosDatosEscritosEnAmbos() {
        val vm = UsuarioViewModel()
        vm.onNombreChange("Benjamín")

        vm.onTipoChange(TipoRegistro.EMPRESA)
        vm.onRazonSocialChange("Fundación Ejemplo SpA")
        vm.onTipoChange(TipoRegistro.PERSONA)

        assertEquals("Benjamín", vm.estado.value.persona.nombre)
        assertEquals("Fundación Ejemplo SpA", vm.estado.value.empresa.razonSocial)
    }

    @Test
    fun empresaVacia_noEsValida_yMarcaCadaError() {
        val vm = UsuarioViewModel()
        vm.onTipoChange(TipoRegistro.EMPRESA)

        assertFalse(vm.validarFormulario())

        val errores = vm.estado.value.errores.empresa
        assertEquals("Campo obligatorio", errores.razonSocial)
        assertEquals("Campo obligatorio", errores.rut)
        assertEquals("Campo obligatorio", errores.giro)
        assertEquals("Campo obligatorio", errores.representante)
        assertEquals("Campo obligatorio", errores.rutRepresentante)
        assertEquals("Correo inválido", errores.correo)
        assertEquals("Campo obligatorio", errores.telefono)
        assertEquals("Campo obligatorio", errores.direccion)
    }

    @Test
    fun empresaCompleta_esValida_sinCompletarLosCamposDePersona() {
        val vm = empresaCompleta()

        assertTrue(vm.validarFormulario())
    }

    @Test
    fun rutDeEmpresaConFormatoIncorrecto_esInvalido() {
        val vm = empresaCompleta()
        vm.onRutEmpresaChange("7654-4")

        assertFalse(vm.validarFormulario())
        assertEquals("RUT inválido", vm.estado.value.errores.empresa.rut)
    }

    @Test
    fun rutDeEmpresaYDelRepresentante_seAceptanPorFormato() {
        val vm = empresaCompleta()
        vm.onRutEmpresaChange("45678987-3")
        vm.onRutRepresentanteChange("45678987-3")

        assertTrue(vm.validarFormulario())
    }

    @Test
    fun telefonoDeEmpresa_exigeNueveDigitos() {
        val vm = empresaCompleta()
        vm.onTelefonoEmpresaChange("12345678")

        assertFalse(vm.validarFormulario())
        assertEquals("Ingresa los 9 dígitos del teléfono", vm.estado.value.errores.empresa.telefono)
    }

    @Test
    fun personaQueAporta_esUnAporte_yGeneraSuRegistro() {
        val vm = UsuarioViewModel()
        vm.seleccionarOportunidad("Taller de carpintería")
        vm.onNombreChange("Benjamín")
        vm.onApellidoChange("Pérez")
        vm.onRunChange("11.111.111-1")
        vm.onCorreoChange("correo@ejemplo.cl")
        vm.onTelefonoChange("12345678")
        vm.onMotivoChange("Aportar")
        vm.onFrecuenciaChange("Mensual")

        assertTrue(vm.estado.value.esAporte)

        val registro = vm.construirRegistro(ahora = 1000L)

        assertEquals(1000L, registro?.id)
        assertEquals("PERSONA", registro?.tipo)
        assertEquals("Taller de carpintería", registro?.oportunidad)
        assertEquals("Benjamín Pérez", registro?.nombre)
        assertEquals("+569 12345678", registro?.telefono)
        assertEquals("Mensual", registro?.tipoAporte)
    }

    @Test
    fun personaQueIngresaAPrograma_noEsUnAporte_yNoGeneraRegistro() {
        val vm = personaCompleta()

        assertFalse(vm.estado.value.esAporte)
        assertNull(vm.construirRegistro())
    }

    @Test
    fun empresa_esUnAporte_yGeneraSuRegistroConTodosSusDatos() {
        val vm = empresaCompleta()

        assertTrue(vm.estado.value.esAporte)

        val registro = vm.construirRegistro(ahora = 2000L)

        assertEquals("EMPRESA", registro?.tipo)
        assertEquals("Fundación Ejemplo SpA", registro?.nombre)
        assertEquals("+56 212345678", registro?.telefono)
        assertEquals("Aporte empresarial", registro?.tipoAporte)
        assertEquals("Av. Providencia 1234", registro?.direccion)
        assertEquals("Ana Ejemplo", registro?.representante)
    }

    @Test
    fun seleccionarOportunidad_guardaElTituloYDejaElFormularioVacio() {
        val vm = personaCompleta()

        vm.seleccionarOportunidad("Taller de carpintería")

        assertEquals("Taller de carpintería", vm.estado.value.oportunidad)
        assertEquals("", vm.estado.value.persona.nombre)
        assertEquals(TipoRegistro.PERSONA, vm.estado.value.tipo)
    }

    @Test
    fun reiniciar_dejaElFormularioVacio() {
        val vm = personaCompleta()

        vm.reiniciar()

        assertEquals("", vm.estado.value.persona.nombre)
        assertEquals("", vm.estado.value.oportunidad)
        assertFalse(vm.estado.value.completado)
    }
}
