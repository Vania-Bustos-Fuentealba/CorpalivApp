package com.duocuc.corpalivapp.data.repository // Declaración del paquete al que pertenece este archivo dentro de la capa de repositorios

import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de oportunidad
import com.duocuc.corpalivapp.data.model.OportunidadRequestDto // Importación del cuerpo de la petición para crear o actualizar
import com.duocuc.corpalivapp.data.network.ApiService // Importación del servicio Retrofit de oportunidades
import com.duocuc.corpalivapp.data.network.RetrofitClient // Importación del cliente Retrofit con los microservicios
import java.io.IOException // Importación de la excepción que lanza Retrofit cuando no hay conexión con el servidor
import retrofit2.HttpException // Importación de la excepción que lanza Retrofit cuando el servidor responde con error

/**
- PARCIAL 4: REPOSITORIO DE OPORTUNIDADES (crear, modificar y eliminar)

- La lectura del listado sigue en OportunidadViewModel, que además tiene datos de respaldo si falla la red.
- Estas operaciones son solo para administradores y entregan un Result con un mensaje legible para la pantalla.

 */
class OportunidadesRepository( // Repositorio que recibe el servicio de red (por defecto el real, o uno falso en las pruebas)
    private val api: ApiService = RetrofitClient.apiService // Servicio Retrofit de oportunidades-service
) {

    /** Crea una oportunidad nueva. Requiere sesión de administrador. */
    suspend fun crear(solicitud: OportunidadRequestDto): Result<OportunidadModel> = // Registra la oportunidad en el servidor
        llamar { api.crearOportunidad(solicitud) }

    /** Modifica una oportunidad existente. Requiere sesión de administrador. */
    suspend fun actualizar(id: Int, solicitud: OportunidadRequestDto): Result<OportunidadModel> = // Reemplaza los datos de la oportunidad
        llamar { api.actualizarOportunidad(id, solicitud) }

    /** Elimina una oportunidad. Requiere sesión de administrador. */
    suspend fun eliminar(id: Int): Result<Unit> = // Borra la oportunidad del servidor
        llamar {
            val respuesta = api.eliminarOportunidad(id) // El servidor responde 204 sin cuerpo cuando se elimina
            if (!respuesta.isSuccessful) throw HttpException(respuesta) // Cualquier otro código se trata como error
        }

    // Ejecuta la llamada y convierte las fallas de red en mensajes claros
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> { // Envuelve cualquier petición en un Result
        return try { // Intenta la petición de red
            Result.success(bloque()) // Éxito: entrega el resultado del servidor
        } catch (e: HttpException) { // El servidor respondió con un código de error
            val mensaje = when (e.code()) { // Mensaje según el código HTTP recibido
                400 -> "El servidor rechazó los datos. Revisa el formulario" // Datos inválidos
                401, 403 -> "Tu sesión no es válida o expiró. Vuelve a iniciar sesión" // Sin sesión o sin permisos de administrador
                404 -> "La oportunidad ya no existe" // Fue eliminada por otra persona
                else -> "El servidor respondió con un error (${e.code()})" // Otro error del servidor
            }
            Result.failure(Exception(mensaje)) // Falla con el mensaje correspondiente
        } catch (e: IOException) { // No hubo conexión: servicios apagados, sin red o dirección incorrecta
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa que los servicios estén encendidos")) // Falla con aviso de conexión
        }
    }
}
