package com.duocuc.corpalivapp.ui.viewmodel // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import com.duocuc.corpalivapp.MainDispatcherRule // Importa MainDispatcherRule: prepara y restaura Dispatchers.Main para probar ViewModels sin Android.
import com.duocuc.corpalivapp.data.model.RolUsuario // Importa RolUsuario: representa el rol de la cuenta autenticada.
import com.duocuc.corpalivapp.data.model.SesionRemotaDto // Importa SesionRemotaDto: representa la respuesta de autenticación del backend.
import com.duocuc.corpalivapp.data.network.SesionToken // Importa SesionToken: mantiene el token que utilizará el interceptor HTTP.
import com.duocuc.corpalivapp.data.repository.AuthRepository // Importa AuthRepository: centraliza las peticiones de inicio de sesión al backend.
import io.mockk.coEvery // Importa coEvery: configura respuestas simuladas para funciones suspendidas.
import io.mockk.coVerify // Importa coVerify: comprueba cuántas veces se llamó a una función suspendida.
import io.mockk.mockk // Importa mockk: crea un objeto simulado en lugar de utilizar la dependencia real.
import kotlinx.coroutines.CompletableDeferred // Importa CompletableDeferred: simula una respuesta pendiente que el test puede completar.
import kotlinx.coroutines.ExperimentalCoroutinesApi // Importa ExperimentalCoroutinesApi: identifica las funciones de prueba que requieren aceptación explícita.
import kotlinx.coroutines.test.advanceUntilIdle // Importa advanceUntilIdle: ejecuta las tareas de prueba pendientes y avanza su reloj virtual.
import kotlinx.coroutines.test.runCurrent // Importa runCurrent: ejecuta solo las tareas programadas para el instante virtual actual.
import kotlinx.coroutines.test.runTest // Importa runTest: ejecuta un test de corrutinas con planificación y tiempo controlados.
import org.junit.After // Importa After: marca la limpieza que se ejecuta después de cada prueba.
import org.junit.Assert.* // Importa las comprobaciones de igualdad, nulidad y condiciones usadas por los tests.
import org.junit.Before // Importa Before: marca la preparación que se ejecuta antes de cada prueba.
import org.junit.Rule // Importa Rule: permite que JUnit aplique una regla antes y después de cada prueba.
import org.junit.Test // Importa Test: marca un método como prueba ejecutable por JUnit.

/** Comprueba el flujo remoto con respuestas simuladas, sin llamar al backend. */
@OptIn(ExperimentalCoroutinesApi::class) // Acepta explícitamente las API experimentales usadas para controlar las corrutinas de prueba.
class SesionRemotaViewModelTest { // Agrupa los escenarios que comprueban el comportamiento de esta clase.
    @get:Rule // Indica a JUnit que aplique la regla mediante el getter de esta propiedad Kotlin.
    val mainDispatcherRule = MainDispatcherRule() // Controla el mismo planificador usado por el ViewModel y por runTest.

    private val repository = mockk<AuthRepository>() // Simula la autenticación para probar el ViewModel sin un servidor.
    private val sesion = SesionRemotaDto( // Construye una sesión ficticia con los campos que entregaría el backend.
        token = "token-simulado", tipoToken = "Bearer", nombre = "Cuenta de prueba", // Define el token de prueba, su tipo y el nombre visible; no son credenciales reales.
        correo = "prueba@example.com", rol = "ADMINISTRADOR", // Asigna la identidad y el rol de la sesión simulada.
        rolEtiqueta = "Administrador", inicio = 2000L // Completa la etiqueta del rol y una fecha fija para resultados deterministas.
    )

    @Before // Ejecuta la preparación antes de cada caso de prueba.
    fun preparar() { SesionToken.valor = null } // Evita reutilizar el token de una prueba anterior.

    @After // Ejecuta la limpieza después de cada caso de prueba.
    fun limpiar() { SesionToken.valor = null } // Descarta el token global para no afectar otras clases de pruebas.

    private fun formulario() = SesionViewModel(repository).apply { // Crea el ViewModel con el mock y completa un formulario de prueba.
        onCorreoChange("  prueba@example.com  ") // Incluye espacios para verificar que el login recorte el correo.
        onClaveChange("clave-simulada") // Carga una contraseña ficticia que solo utiliza el mock.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun exito_guardaSesionYToken_yLimpiaElFormulario() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.login("prueba@example.com", "clave-simulada") } returns Result.success(sesion) // Simula una autenticación aceptada con los datos de sesión indicados.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.

        viewModel.iniciarSesionRemoto(1000L) // Inicia el login con una fecha local fija, para poder comprobarla.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(sesion.token, SesionToken.valor) // Verifica que el token simulado quede disponible para las siguientes peticiones.
        assertEquals(sesion.nombre, viewModel.usuario.value?.nombre) // Comprueba que el nombre provenga de la respuesta del backend.
        assertEquals(sesion.correo, viewModel.usuario.value?.correo) // Comprueba que la sesión use el correo devuelto por el backend.
        assertEquals(RolUsuario.ADMINISTRADOR, viewModel.usuario.value?.rol) // Verifica la conversión del rol remoto al modelo de la aplicación.
        assertEquals(2000L, viewModel.usuario.value?.inicio) // Comprueba que se conserve la fecha válida enviada por el servidor.
        assertEquals(LoginUiState(), viewModel.login.value) // Verifica que el formulario quede vacío, sin clave, sin carga y sin error.
        coVerify(exactly = 1) { repository.login("prueba@example.com", "clave-simulada") } // Comprueba que se realice una sola petición y no se duplique la operación.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun sinFechaDelServidor_usaLaFechaLocal() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.login(any(), any()) } returns Result.success(sesion.copy(inicio = 0L)) // Simula una autenticación aceptada con los datos de sesión indicados.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.

        viewModel.iniciarSesionRemoto(1000L) // Inicia el login con una fecha local fija, para poder comprobarla.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(1000L, viewModel.usuario.value?.inicio) // Comprueba la fecha local de respaldo cuando el servidor no envía una válida.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun camposVacios_noConsultanElBackend() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val viewModel = SesionViewModel(repository) // Crea un formulario vacío con el Repository de autenticación simulado.

        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals("Ingresa tu correo y tu contraseña", viewModel.login.value.error) // Comprueba el aviso de campos obligatorios antes de consultar al backend.
        assertFalse(viewModel.login.value.cargando) // Comprueba que el formulario deje de mostrar una operación en curso.
        assertNull(viewModel.usuario.value) // Verifica que no exista una cuenta autenticada en el estado.
        coVerify(exactly = 0) { repository.login(any(), any()) } // Comprueba que la validación local impida llamar al Repository.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun faltaClave_noConsultaElBackend() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val viewModel = SesionViewModel(repository) // Crea un formulario vacío con el Repository de autenticación simulado.
        viewModel.onCorreoChange("prueba@example.com") // Completa solo el correo para dejar intencionalmente la clave vacía.

        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals("Ingresa tu correo y tu contraseña", viewModel.login.value.error) // Comprueba el aviso de campos obligatorios antes de consultar al backend.
        coVerify(exactly = 0) { repository.login(any(), any()) } // Comprueba que la validación local impida llamar al Repository.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun intentoEnCurso_muestraCarga_yNoDuplicaElLogin() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val respuesta = CompletableDeferred<Result<SesionRemotaDto>>() // Mantiene el login pendiente hasta que el test entregue el resultado.
        coEvery { repository.login(any(), any()) } coAnswers { respuesta.await() } // Mantiene la llamada simulada suspendida hasta completar respuesta dentro del test.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.

        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        runCurrent() // Ejecuta las tareas del instante actual sin resolver una respuesta que sigue pendiente.

        assertTrue(viewModel.login.value.cargando) // Comprueba que el formulario indique que espera al servidor.
        assertNull(viewModel.usuario.value) // Verifica que no exista una cuenta autenticada en el estado.
        coVerify(exactly = 1) { repository.login(any(), any()) } // Comprueba que se realice una sola petición y no se duplique la operación.

        respuesta.complete(Result.success(sesion)) // Simula que el servidor acepta el login y devuelve la sesión.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertFalse(viewModel.login.value.cargando) // Comprueba que el formulario deje de mostrar una operación en curso.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun fallo_muestraElError_limpiaClave_yNoAbreSesion() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.login(any(), any()) } returns Result.failure(Exception("Credenciales incorrectas")) // Simula un rechazo de autenticación sin realizar peticiones HTTP.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.

        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals("Credenciales incorrectas", viewModel.login.value.error) // Comprueba que la pantalla reciba el mensaje de fallo simulado.
        assertEquals("", viewModel.login.value.clave) // Verifica que no se conserve la contraseña tras un intento fallido.
        assertFalse(viewModel.login.value.cargando) // Comprueba que el formulario deje de mostrar una operación en curso.
        assertNull(viewModel.usuario.value) // Verifica que no exista una cuenta autenticada en el estado.
        assertNull(SesionToken.valor) // Verifica que no quede un token de autenticación disponible.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun falloSinMensaje_muestraErrorPredeterminado() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.login(any(), any()) } returns Result.failure(Exception()) // Simula un rechazo de autenticación sin realizar peticiones HTTP.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.

        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals("No se pudo iniciar sesión", viewModel.login.value.error) // Comprueba el mensaje de respaldo cuando la excepción no tiene texto.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun cerrarSesion_remotaDescartaTokenYUsuario() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.login(any(), any()) } returns Result.success(sesion) // Simula una autenticación aceptada con los datos de sesión indicados.
        val viewModel = formulario() // Obtiene un ViewModel con el formulario de prueba ya completado.
        viewModel.iniciarSesionRemoto() // Solicita el inicio de sesión usando el flujo remoto del ViewModel.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertEquals(sesion.token, SesionToken.valor) // Verifica que el token simulado quede disponible para las siguientes peticiones.

        viewModel.cerrarSesion() // Ejecuta el cierre de sesión que debe borrar usuario, token y formulario.

        assertNull(SesionToken.valor) // Verifica que no quede un token de autenticación disponible.
        assertNull(viewModel.usuario.value) // Verifica que no exista una cuenta autenticada en el estado.
        assertEquals(LoginUiState(), viewModel.login.value) // Verifica que el formulario quede vacío, sin clave, sin carga y sin error.
    }
}
