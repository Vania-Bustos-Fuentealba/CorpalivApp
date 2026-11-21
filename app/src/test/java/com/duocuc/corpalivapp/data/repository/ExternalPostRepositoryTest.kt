
package com.duocuc.corpalivapp.data.repository

import com.duocuc.corpalivapp.data.network.ExternalApiService
import com.duocuc.corpalivapp.data.network.ExternalPost
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

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
class ExternalPostRepositoryTest {

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
    @Before
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
    @Test
    fun obtenerPublicaciones_respuestaExitosa() = runTest {

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

        assertEquals(
            "Publicación de prueba",
            resultado[0].title
        )

        // Verifica que el servicio fue llamado una vez.
        coVerify(exactly = 1) {
            apiService.getPosts()
        }
    }

    /**
     * PRUEBA 2: LISTA VACÍA
     *
     * Comprueba que el Repository pueda recibir
     * una lista sin publicaciones.
     */
    @Test
    fun obtenerPublicaciones_listaVacia() = runTest {

        // Simula una respuesta sin publicaciones.
        coEvery {
            apiService.getPosts()
        } returns emptyList()

        // Ejecuta la solicitud.
        val resultado = repository.obtenerPublicaciones()

        // Comprueba que la lista esté vacía.
        assertEquals(0, resultado.size)

        coVerify(exactly = 1) {
            apiService.getPosts()
        }
    }

    /**
     * PRUEBA 3: ERROR DE CONEXIÓN
     *
     * Comprueba que el Repository propague
     * correctamente los errores del servicio.
     */
    @Test
    fun obtenerPublicaciones_errorConexion() = runTest {

        // Simula un fallo en la API externa.
        coEvery {
            apiService.getPosts()
        } throws RuntimeException("Error de conexión")

        // Comprueba que se produzca la excepción.
        val error = assertThrows(
            RuntimeException::class.java
        ) {
            kotlinx.coroutines.runBlocking {
                repository.obtenerPublicaciones()
            }
        }

        // Verifica el mensaje del error.
        assertEquals(
            "Error de conexión",
            error.message
        )

        coVerify(exactly = 1) {
            apiService.getPosts()
        }
    }
}
