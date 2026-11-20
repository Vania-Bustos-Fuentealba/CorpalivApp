package com.duocuc.corpalivapp.data.repository // Declaración del paquete al que pertenece este archivo dentro de la capa de repositorios

import com.duocuc.corpalivapp.data.model.AporteRequestDto // Importación del cuerpo de la petición de registro
import com.duocuc.corpalivapp.data.model.EstadoRequestDto // Importación del cuerpo de la petición de cambio de estado
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Importación del modelo que representa un aporte o postulación
import com.duocuc.corpalivapp.data.network.AportesApiService // Importación del servicio Retrofit de aportes
import com.duocuc.corpalivapp.data.network.RetrofitClient // Importación del cliente Retrofit con los microservicios
import java.io.IOException // Importación de la excepción que lanza Retrofit cuando no hay conexión con el servidor
import retrofit2.HttpException // Importación de la excepción que lanza Retrofit cuando el servidor responde con error

/** Estado de seguimiento que guarda el servidor cuando el administrador contacta al aportante. */
const val ESTADO_CONTACTADO = "CONTACTADO" // Valor que espera aportes-service en PATCH /api/aportes/{id}/estado

/**
- PARCIAL 4: REPOSITORIO DE APORTES Y POSTULACIONES

- Es el único que habla con aportes-service. Entrega los resultados como Result con un mensaje legible
- para la pantalla, así los ViewModel no conocen Retrofit ni los códigos HTTP.

 */
class AportesRepository( // Repositorio que recibe el servicio de red (por defecto el real, o uno falso en las pruebas)
    private val api: AportesApiService = RetrofitClient.aportesApi // Servicio Retrofit de aportes-service
) {

    /** Envía el formulario (aporte o postulación). Es público: no necesita sesión. */
    suspend fun registrar(solicitud: AporteRequestDto): Result<RegistroGuardado> = // Registra el aporte o la postulación en el servidor
        llamar { api.crear(solicitud) }

    /** Lista los registros del servidor. Requiere sesión de administrador. */
    suspend fun listar(): Result<List<RegistroGuardado>> = // Descarga todos los aportes y postulaciones
        llamar { api.listar() }

    /** Marca un registro como contactado. Requiere sesión de administrador. */
    suspend fun marcarContactado(id: Long): Result<RegistroGuardado> = // Cambia el estado del registro a CONTACTADO
        llamar { api.cambiarEstado(id, EstadoRequestDto(ESTADO_CONTACTADO)) }

    /** Elimina un aporte o postulación. Requiere sesión de administrador. */
    suspend fun eliminar(id: Long): Result<Unit> = // Borra el registro del servidor
        llamar {
            val respuesta = api.eliminar(id) // El servidor responde 204 sin cuerpo cuando se elimina
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
                404 -> "No se encontró el registro" // El registro ya no existe
                else -> "El servidor respondió con un error (${e.code()})" // Otro error del servidor
            }
            Result.failure(Exception(mensaje)) // Falla con el mensaje correspondiente
        } catch (e: IOException) { // No hubo conexión: servicios apagados, sin red o dirección incorrecta
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa que los servicios estén encendidos")) // Falla con aviso de conexión
        }
    }
}
