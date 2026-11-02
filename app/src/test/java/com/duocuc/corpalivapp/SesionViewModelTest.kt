package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas unitarias locales

import com.duocuc.corpalivapp.data.model.RolUsuario // Importación del enum con los roles de usuario del sistema
import com.duocuc.corpalivapp.ui.viewmodel.ADMIN_CLAVE // Importación de la constante con la contraseña del administrador de prueba
import com.duocuc.corpalivapp.ui.viewmodel.ADMIN_CORREO // Importación de la constante con el correo del administrador de prueba
import com.duocuc.corpalivapp.ui.viewmodel.SesionViewModel // Importación del ViewModel de sesión a testear
import org.junit.Assert.assertEquals // Importación de aserción para verificar la igualdad entre dos valores
import org.junit.Assert.assertFalse // Importación de aserción para verificar que una condición sea falsa
import org.junit.Assert.assertNotNull // Importación de aserción para verificar que un objeto no sea nulo
import org.junit.Assert.assertNull // Importación de aserción para verificar que un objeto sea nulo
import org.junit.Assert.assertTrue // Importación de aserción para verificar que una condición sea verdadera
import org.junit.Test // Anotación de JUnit para identificar métodos que corresponden a casos de prueba

/**
- G13 - Pruebas del inicio de sesión de la cuenta de administrador (JVM, sin emulador).

 */
class SesionViewModelTest { // Clase de prueba unitaria local para validar la lógica del ViewModel de sesión

    private fun intentar(correo: String, clave: String): SesionViewModel { // Función auxiliar para instanciar el ViewModel y simular el ingreso de credenciales
        val vm = SesionViewModel() // Crea una nueva instancia limpia de SesionViewModel
        vm.onCorreoChange(correo) // Simula el ingreso del correo en la UI
        vm.onClaveChange(clave) // Simula el ingreso de la contraseña en la UI
        return vm // Retorna el ViewModel con los campos cargados
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun credencialesCorrectas_abrenLaSesionDeAdministrador() { // Valida que el inicio de sesión sea exitoso cuando las credenciales coinciden
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE) // Prepara el ViewModel con las credenciales de administración correctas

        assertTrue(vm.iniciarSesion(ahora = 1000L)) // Ejecuta el inicio de sesión pasando una marca de tiempo fija y afirma resultado verdadero

        val usuario = vm.usuario.value // Obtiene el objeto de sesión activo del StateFlow
        assertNotNull(usuario) // Comprueba que el usuario en sesión no sea nulo
        assertEquals(ADMIN_CORREO, usuario?.correo) // Verifica que el correo registrado en la sesión sea el del administrador
        assertEquals(RolUsuario.ADMINISTRADOR, usuario?.rol) // Verifica que el rol asignado sea ADMINISTRADOR
        assertEquals(1000L, usuario?.inicio) // Verifica que la marca de tiempo de inicio corresponda al valor entregado
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun elCorreoNoDistingueMayusculasNiEspacios() { // Verifica que el correo sea insensible a mayúsculas y espacios extremos
        val vm = intentar("  ADMIN@Corpaliv.CL ", ADMIN_CLAVE) // Carga el correo con combinaciones de mayúsculas y espacios agregados

        assertTrue(vm.iniciarSesion()) // Afirma que el inicio de sesión sea exitoso pese al formato de entrada
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun laClaveSiDistingueMayusculas() { // Comprueba la estricta sensibilidad a mayúsculas y minúsculas en la contraseña
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE.uppercase()) // Carga la contraseña convertida totalmente a mayúsculas

        assertFalse(vm.iniciarSesion()) // Verifica que el intento de inicio de sesión retorne falso
        assertNull(vm.usuario.value) // Comprueba que no se haya creado ninguna sesión activa
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun claveIncorrecta_muestraError_yLimpiaLaClave() { // Valida el manejo de errores ante contraseñas inválidas
        val vm = intentar(ADMIN_CORREO, "otra") // Carga una contraseña incorrecta

        assertFalse(vm.iniciarSesion()) // Verifica que la autenticación falle (false)

        assertEquals("Correo o contraseña incorrectos", vm.login.value.error) // Confirma el mensaje de error de credenciales en el estado de la UI
        assertEquals("", vm.login.value.clave) // Comprueba que la contraseña ingresada haya sido limpiada por seguridad
        assertNull(vm.usuario.value) // Confirma que el objeto de sesión siga siendo nulo
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun correoIncorrecto_noAbreLaSesion() { // Verifica el rechazo de inicio de sesión con correos no registrados
        val vm = intentar("otro@corpaliv.cl", ADMIN_CLAVE) // Carga un correo inexistente con la contraseña válida

        assertFalse(vm.iniciarSesion()) // Afirma que la autenticación falle
        assertNull(vm.usuario.value) // Confirma que no se otorgue acceso ni sesión de usuario
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun camposVacios_pidenCompletarLosDatos() { // Prueba la validación de campos obligatorios en el formulario de login
        val vm = SesionViewModel() // Instancia un ViewModel sin campos cargados (valores por defecto vacíos)

        assertFalse(vm.iniciarSesion()) // Intenta iniciar sesión y afirma que retorne falso

        assertEquals("Ingresa tu correo y tu contraseña", vm.login.value.error) // Confirma que el mensaje solicite completar los datos
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun alEscribir_seQuitaElErrorAnterior() { // Verifica que cualquier mensaje de error se borre cuando el usuario vuelve a tipear
        val vm = intentar(ADMIN_CORREO, "otra") // Provoca un intento fallido previo
        vm.iniciarSesion() // Genera el estado de error en el ViewModel

        vm.onClaveChange("a") // Simula la escritura de un nuevo carácter en la contraseña

        assertNull(vm.login.value.error) // Comprueba que el mensaje de error se haya reseteado a null
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun alIngresarBien_noSeConservaLaClaveEnElFormulario() { // Verifica la limpieza del formulario de ingreso tras un login exitoso
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE) // Carga credenciales válidas

        vm.iniciarSesion() // Inicia sesión exitosamente

        assertEquals("", vm.login.value.clave) // Confirma que el campo contraseña en el UiState quede totalmente vacío
        assertEquals("", vm.login.value.correo) // Confirma que el campo correo en el UiState quede totalmente vacío
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun cerrarSesion_quitaLaCuenta() { // Comprueba la correcta destrucción de la sesión activa al cerrar sesión
        val vm = intentar(ADMIN_CORREO, ADMIN_CLAVE) // Carga credenciales válidas
        vm.iniciarSesion() // Inicia sesión previamente

        vm.cerrarSesion() // Invoca la acción de cierre de sesión

        assertNull(vm.usuario.value) // Verifica que el usuario activo en el StateFlow vuelva a ser nulo
    }
}