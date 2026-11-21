package com.duocuc.corpalivapp.ui.viewmodel // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import com.duocuc.corpalivapp.MainDispatcherRule // Importa MainDispatcherRule: prepara y restaura Dispatchers.Main para probar ViewModels sin Android.
import com.duocuc.corpalivapp.data.model.ExternalPost // Importa ExternalPost: modela los campos de una publicación recibida desde JSONPlaceholder.
import com.duocuc.corpalivapp.data.repository.ExternalPostRepository // Importa ExternalPostRepository: centraliza la consulta de publicaciones al servicio.
import io.mockk.coEvery // Importa coEvery: configura respuestas simuladas para funciones suspendidas.
import io.mockk.coVerify // Importa coVerify: comprueba cuántas veces se llamó a una función suspendida.
import io.mockk.mockk // Importa mockk: crea un objeto simulado en lugar de utilizar la dependencia real.
import kotlinx.coroutines.CompletableDeferred // Importa CompletableDeferred: simula una respuesta pendiente que el test puede completar.
import kotlinx.coroutines.CancellationException // Importa CancellationException: distingue la cancelación de una corrutina de un error de red.
import kotlinx.coroutines.ExperimentalCoroutinesApi // Importa ExperimentalCoroutinesApi: identifica las funciones de prueba que requieren aceptación explícita.
import kotlinx.coroutines.test.advanceUntilIdle // Importa advanceUntilIdle: ejecuta las tareas de prueba pendientes y avanza su reloj virtual.
import kotlinx.coroutines.test.runCurrent // Importa runCurrent: ejecuta solo las tareas programadas para el instante virtual actual.
import kotlinx.coroutines.test.runTest // Importa runTest: ejecuta un test de corrutinas con planificación y tiempo controlados.
import org.junit.Assert.assertEquals // Importa assertEquals: comprueba que el valor esperado coincida con el obtenido.
import org.junit.Assert.assertFalse // Importa assertFalse: comprueba que una condición sea falsa.
import org.junit.Assert.assertNull // Importa assertNull: comprueba que un valor sea nulo.
import org.junit.Assert.assertTrue // Importa assertTrue: comprueba que una condición sea verdadera.
import org.junit.Rule // Importa Rule: permite que JUnit aplique una regla antes y después de cada prueba.
import org.junit.Test // Importa Test: marca un método como prueba ejecutable por JUnit.

@OptIn(ExperimentalCoroutinesApi::class) // Acepta explícitamente las API experimentales usadas para controlar las corrutinas de prueba.
class ExternalPostViewModelTest { // Agrupa los escenarios que comprueban el comportamiento de esta clase.
    @get:Rule // Indica a JUnit que aplique la regla mediante el getter de esta propiedad Kotlin.
    val mainDispatcherRule = MainDispatcherRule() // Controla el mismo planificador usado por el ViewModel y por runTest.

    private val repository = mockk<ExternalPostRepository>() // Sustituye el Repository real para evitar peticiones de red.
    private val publicaciones = listOf(ExternalPost(1, 1, "Título", "Contenido")) // Define una respuesta ficticia reutilizable en las pruebas.

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun alCrear_cargaPublicacionesUnaSolaVez() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } returns publicaciones // Configura el mock para devolver la lista ficticia al consultar publicaciones.

        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(ExternalPostUiState(publicaciones = publicaciones), viewModel.uiState.value) // Comprueba datos correctos, carga terminada y ausencia de error en un único estado esperado.
        coVerify(exactly = 1) { repository.obtenerPublicaciones() } // Comprueba que se realice una sola petición y no se duplique la operación.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun mientrasEspera_muestraCarga_yLaFinalizaAlRecibirDatos() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val respuesta = CompletableDeferred<List<ExternalPost>>() // Crea una respuesta pendiente controlada por el test.
        coEvery { repository.obtenerPublicaciones() } coAnswers { respuesta.await() } // Mantiene la llamada simulada suspendida hasta completar respuesta dentro del test.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.

        runCurrent() // Ejecuta las tareas del instante actual sin resolver una respuesta que sigue pendiente.

        assertTrue(viewModel.uiState.value.cargando) // Comprueba que la pantalla indique que hay una petición pendiente.
        assertTrue(viewModel.uiState.value.publicaciones.isEmpty()) // Comprueba que todavía no se publiquen datos mientras llega la respuesta.
        assertNull(viewModel.uiState.value.error) // Comprueba que el estado no muestre un error.

        respuesta.complete(publicaciones) // Simula la llegada de una respuesta con publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertFalse(viewModel.uiState.value.cargando) // Comprueba que se apague el indicador al finalizar la operación.
        assertEquals(publicaciones, viewModel.uiState.value.publicaciones) // Compara la lista completa recibida con la lista esperada.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun respuestaVacia_esUnResultadoExitoso() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } returns emptyList() // Simula una consulta exitosa sin publicaciones.

        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(ExternalPostUiState(), viewModel.uiState.value) // Una respuesta vacía debe terminar sin error y sin indicador de carga.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun error_muestraMensaje_yTerminaLaCarga() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } throws IllegalStateException("Sin conexión") // Hace que el mock lance este error para comprobar el manejo del fallo.

        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(ExternalPostUiState(error = "Sin conexión"), viewModel.uiState.value) // Verifica el mensaje de error, la lista vacía y la carga terminada.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun errorSinMensaje_muestraMensajePredeterminado() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } throws IllegalStateException() // Hace que el mock lance este error para comprobar el manejo del fallo.

        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals( // Compara el estado completo esperado con el estado realmente publicado.
            ExternalPostUiState(error = "Error al cargar publicaciones"), // Define el estado esperado para una excepción sin mensaje.
            viewModel.uiState.value // Lee el estado actual que observaría la pantalla.
        )
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun actualizar_reemplazaLasPublicacionesConLaNuevaRespuesta() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val nuevas = listOf(ExternalPost(2, 2, "Nuevo título", "Nuevo contenido")) // Crea datos distintos para comprobar que una actualización sustituya los anteriores.
        coEvery { repository.obtenerPublicaciones() } returnsMany listOf(publicaciones, nuevas) // Devuelve primero la lista original y después la nueva para simular una actualización.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals(ExternalPostUiState(publicaciones = nuevas), viewModel.uiState.value) // Verifica que aparezcan los datos actualizados y que no queden carga ni errores.
        coVerify(exactly = 2) { repository.obtenerPublicaciones() } // Comprueba que se realicen la petición inicial y la petición de actualización.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun reintentar_limpiaElErrorAnterior_yRecuperaLosDatos() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } throws IllegalStateException("Sin conexión") // Hace que el mock lance este error para comprobar el manejo del fallo.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertEquals("Sin conexión", viewModel.uiState.value.error) // Confirma que existe un error anterior antes de probar el reintento.
        val respuesta = CompletableDeferred<List<ExternalPost>>() // Crea una respuesta pendiente controlada por el test.
        coEvery { repository.obtenerPublicaciones() } coAnswers { respuesta.await() } // Mantiene la llamada simulada suspendida hasta completar respuesta dentro del test.

        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        runCurrent() // Ejecuta las tareas del instante actual sin resolver una respuesta que sigue pendiente.

        assertTrue(viewModel.uiState.value.cargando) // Comprueba que la pantalla indique que hay una petición pendiente.
        assertNull(viewModel.uiState.value.error) // Comprueba que el estado no muestre un error.

        respuesta.complete(publicaciones) // Simula la llegada de una respuesta con publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertEquals(ExternalPostUiState(publicaciones = publicaciones), viewModel.uiState.value) // Comprueba datos correctos, carga terminada y ausencia de error en un único estado esperado.
        coVerify(exactly = 2) { repository.obtenerPublicaciones() } // Comprueba que se realicen la petición inicial y la petición de actualización.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun errorAlActualizar_noPresentaLosDatosAnterioresComoActuales() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } returns publicaciones // Configura el mock para devolver la lista ficticia al consultar publicaciones.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        coEvery { repository.obtenerPublicaciones() } throws IllegalStateException("Servidor no disponible") // Hace que el mock lance este error para comprobar el manejo del fallo.

        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertEquals( // Compara el estado completo esperado con el estado realmente publicado.
            ExternalPostUiState(error = "Servidor no disponible"), // Define el estado esperado tras fallar una actualización.
            viewModel.uiState.value // Lee el estado actual que observaría la pantalla.
        )
        coVerify(exactly = 2) { repository.obtenerPublicaciones() } // Comprueba que se realicen la petición inicial y la petición de actualización.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun actualizarDuranteLaCarga_noDuplicaLaPeticion() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        val respuesta = CompletableDeferred<List<ExternalPost>>() // Crea una respuesta pendiente controlada por el test.
        coEvery { repository.obtenerPublicaciones() } coAnswers { respuesta.await() } // Mantiene la llamada simulada suspendida hasta completar respuesta dentro del test.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.

        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        runCurrent() // Ejecuta las tareas del instante actual sin resolver una respuesta que sigue pendiente.
        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        runCurrent() // Ejecuta las tareas del instante actual sin resolver una respuesta que sigue pendiente.

        coVerify(exactly = 1) { repository.obtenerPublicaciones() } // Comprueba que se realice una sola petición y no se duplique la operación.
        respuesta.complete(publicaciones) // Simula la llegada de una respuesta con publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertEquals(ExternalPostUiState(publicaciones = publicaciones), viewModel.uiState.value) // Comprueba datos correctos, carga terminada y ausencia de error en un único estado esperado.
    }

    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun cancelacion_noSeMuestraComoErrorDeRed_yPermiteOtraCarga() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.
        coEvery { repository.obtenerPublicaciones() } throws CancellationException("Cancelada") // Simula una cancelación, que no debe presentarse como un error de red.
        val viewModel = ExternalPostViewModel(repository) // Crea el ViewModel con el mock; init programa la carga inicial.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.

        assertNull(viewModel.uiState.value.error) // Comprueba que el estado no muestre un error.
        assertFalse(viewModel.uiState.value.cargando) // Comprueba que se apague el indicador al finalizar la operación.

        coEvery { repository.obtenerPublicaciones() } returns publicaciones // Configura el mock para devolver la lista ficticia al consultar publicaciones.
        viewModel.cargarPublicaciones() // Simula la acción del usuario de actualizar las publicaciones.
        advanceUntilIdle() // Ejecuta las tareas pendientes del planificador de prueba hasta que no quede trabajo programado.
        assertEquals(ExternalPostUiState(publicaciones = publicaciones), viewModel.uiState.value) // Comprueba datos correctos, carga terminada y ausencia de error en un único estado esperado.
    }
}
