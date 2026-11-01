package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.data.model.RolUsuario
import com.duocuc.corpalivapp.ui.viewmodel.ADMIN_CLAVE
import com.duocuc.corpalivapp.ui.viewmodel.ADMIN_CORREO
import com.duocuc.corpalivapp.ui.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * G13 - Pruebas del inicio de sesión de la cuenta de administrador (JVM, sin emulador).
 */
class SesionViewModelTest {

    private fun intentar(correo: String, clave: String): SesionViewModel {
        val vm = SesionViewModel()
        vm.onCorreoChange(correo)
        vm.onClaveChange(clave)
        return vm
    }

    @Test
    fun credencialesCorrectas_abrenLaSesionDeAdministrador() {
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE)

        assertTrue(vm.iniciarSesion(ahora = 1000L))

        val usuario = vm.usuario.value
        assertNotNull(usuario)
        assertEquals(ADMIN_CORREO, usuario?.correo)
        assertEquals(RolUsuario.ADMINISTRADOR, usuario?.rol)
        assertEquals(1000L, usuario?.inicio)
    }

    @Test
    fun elCorreoNoDistingueMayusculasNiEspacios() {
        val vm = intentar("  ADMIN@Corpaliv.CL ", ADMIN_CLAVE)

        assertTrue(vm.iniciarSesion())
    }

    @Test
    fun laClaveSiDistingueMayusculas() {
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE.uppercase())

        assertFalse(vm.iniciarSesion())
        assertNull(vm.usuario.value)
    }

    @Test
    fun claveIncorrecta_muestraError_yLimpiaLaClave() {
        val vm = intentar(ADMIN_CORREO, "otra")

        assertFalse(vm.iniciarSesion())

        assertEquals("Correo o contraseña incorrectos", vm.login.value.error)
        assertEquals("", vm.login.value.clave)
        assertNull(vm.usuario.value)
    }

    @Test
    fun correoIncorrecto_noAbreLaSesion() {
        val vm = intentar("otro@corpaliv.cl", ADMIN_CLAVE)

        assertFalse(vm.iniciarSesion())
        assertNull(vm.usuario.value)
    }

    @Test
    fun camposVacios_pidenCompletarLosDatos() {
        val vm = SesionViewModel()

        assertFalse(vm.iniciarSesion())

        assertEquals("Ingresa tu correo y tu contraseña", vm.login.value.error)
    }

    @Test
    fun alEscribir_seQuitaElErrorAnterior() {
        val vm = intentar(ADMIN_CORREO, "otra")
        vm.iniciarSesion()

        vm.onClaveChange("a")

        assertNull(vm.login.value.error)
    }

    @Test
    fun alIngresarBien_noSeConservaLaClaveEnElFormulario() {
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE)

        vm.iniciarSesion()

        assertEquals("", vm.login.value.clave)
        assertEquals("", vm.login.value.correo)
    }

    @Test
    fun cerrarSesion_quitaLaCuenta() {
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE)
        vm.iniciarSesion()

        vm.cerrarSesion()

        assertNull(vm.usuario.value)
    }
}
