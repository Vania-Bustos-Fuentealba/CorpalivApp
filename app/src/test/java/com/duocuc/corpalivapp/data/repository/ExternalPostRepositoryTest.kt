
package com.duocuc.corpalivapp.data.repository // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import com.duocuc.corpalivapp.data.network.ExternalApiService // Importa ExternalApiService: declara los endpoints HTTP de las publicaciones.
import com.duocuc.corpalivapp.data.model.ExternalPost // Importa ExternalPost: modela los campos de una publicación recibida desde JSONPlaceholder.
import io.mockk.coEvery // Importa coEvery: configura respuestas simuladas para funciones suspendidas.
import io.mockk.coVerify // Importa coVerify: comprueba cuántas veces se llamó a una función suspendida.
import io.mockk.mockk // Importa mockk: crea un objeto simulado en lugar de utilizar la dependencia real.
import kotlinx.coroutines.test.runTest // Importa runTest: ejecuta un test de corrutinas con planificación y tiempo controlados.
import org.junit.Assert.assertEquals // Importa assertEquals: comprueba que el valor esperado coincida con el obtenido.
import org.junit.Assert.assertThrows // Importa assertThrows: comprueba que el bloque lance el tipo de excepción esperado.
import org.junit.Before // Importa Before: marca la preparación que se ejecuta antes de cada prueba.
import org.junit.Test // Importa Test: marca un método como prueba ejecutable por JUnit.

/**
 * ==================================================
 * PRUEBAS UNITARIAS DEL REPOSITORY
 * ==================================================
 *
 * Objetivo:
 * Comprobar que ExternalPostRepository funciona
 * correctamente al obtener datos desde una API.
 *
 * Se utiliza MockK para simular el servicio
 * sin realizar peticiones reales a Internet.
 */
class ExternalPostRepositoryTest { // Agrupa los escenarios que comprueban el comportamiento de esta clase.

    // Servicio de API simulado mediante MockK.
    private lateinit var apiService: ExternalApiService

    // Repository que será sometido a las pruebas.
    private lateinit var repository: ExternalPostRepository

    /**
     * PREPARACIÓN DE LAS PRUEBAS
     *
     * Se ejecuta antes de cada prueba para crear
     * un servicio simulado y un Repository nuevo.
     */
    @Before // Ejecuta la preparación antes de cada caso de prueba.
    fun configurar() {

        apiService = mockk()

        repository = ExternalPostRepository(
            apiService = apiService
        )
    }

    /**
     * PRUEBA 1: RESPUESTA EXITOSA
     *
     * Comprueba que el Repository devuelve
     * correctamente las publicaciones recibidas.
     */
    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun obtenerPublicaciones_respuestaExitosa() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.

        // Datos ficticios utilizados en la prueba.
        val publicaciones = listOf(
            ExternalPost(
                userId = 1,
                id = 1,
                title = "Publicación de prueba",
                body = "Contenido de prueba"
            )
        )

        // Simula una respuesta exitosa de la API.
        coEvery {
            apiService.getPosts()
        } returns publicaciones

        // Ejecuta la función que queremos probar.
        val resultado = repository.obtenerPublicaciones()

        // Comprueba que los datos sean correctos.
        assertEquals(1, resultado.size)

        assertEquals( // Compara el valor esperado con el obtenido en la respuesta o en la excepción.
            "Publicación de prueba",
            resultado[0].title
        )

        // Verifica que el servicio fue llamado una vez.
        coVerify(exactly = 1) { // Comprueba que se realice una sola petición y no se duplique la operación.
            apiService.getPosts()
        }
    }

    /**
     * PRUEBA 2: LISTA VACÍA
     *
     * Comprueba que el Repository pueda recibir
     * una lista sin publicaciones.
     */
    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun obtenerPublicaciones_listaVacia() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.

        // Simula una respuesta sin publicaciones.
        coEvery {
            apiService.getPosts()
        } returns emptyList()

        // Ejecuta la solicitud.
        val resultado = repository.obtenerPublicaciones()

        // Comprueba que la lista esté vacía.
        assertEquals(0, resultado.size)

        coVerify(exactly = 1) { // Comprueba que se realice una sola petición y no se duplique la operación.
            apiService.getPosts()
        }
    }

    /**
     * PRUEBA 3: ERROR DE CONEXIÓN
     *
     * Comprueba que el Repository propague
     * correctamente los errores del servicio.
     */
    @Test // Marca este escenario como una prueba que JUnit debe ejecutar.
    fun obtenerPublicaciones_errorConexion() = runTest { // Ejecuta este escenario con corrutinas y un reloj virtual controlados por el test.

        // Simula un fallo en la API externa.
        coEvery {
            apiService.getPosts()
        } throws RuntimeException("Error de conexión")

        // Comprueba que se produzca la excepción.
        val error = assertThrows(
            RuntimeException::class.java
        ) {
            kotlinx.coroutines.runBlocking {
                repository.obtenerPublicaciones() // Espera la respuesta de la función suspendida del Repository.
            }
        }

        // Verifica el mensaje del error.
        assertEquals( // Compara el valor esperado con el obtenido en la respuesta o en la excepción.
            "Error de conexión",
            error.message
        )

        coVerify(exactly = 1) { // Comprueba que se realice una sola petición y no se duplique la operación.
            apiService.getPosts()
        }
    }
}
