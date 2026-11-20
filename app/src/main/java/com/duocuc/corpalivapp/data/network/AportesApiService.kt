package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este archivo dentro de la capa de red (Network)

import com.duocuc.corpalivapp.data.model.AporteRequestDto // Importación del cuerpo de la petición de registro
import com.duocuc.corpalivapp.data.model.EstadoRequestDto // Importación del cuerpo de la petición de cambio de estado
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Importación del modelo que representa un aporte o postulación
import retrofit2.http.Body // Importación de la anotación que envía un objeto como cuerpo JSON
import retrofit2.Response // Respuesta completa de Retrofit, usada cuando el servidor responde sin cuerpo (204)
import retrofit2.http.DELETE // Importación de la anotación HTTP DELETE de Retrofit
import retrofit2.http.GET // Importación de la anotación HTTP GET de Retrofit
import retrofit2.http.PATCH // Importación de la anotación HTTP PATCH de Retrofit
import retrofit2.http.POST // Importación de la anotación HTTP POST de Retrofit
import retrofit2.http.Path // Importación de la anotación que inserta un valor en la ruta

/** PARCIAL 4: peticiones al microservicio de aportes y postulaciones (aportes-service, puerto 8082). */
interface AportesApiService { // Interfaz de Retrofit con los endpoints de aportes

    // POST /api/aportes: público, lo usa el formulario de registro
    @POST("aportes") // Ruta relativa a la URL base de aportes-service
    suspend fun crear(@Body solicitud: AporteRequestDto): RegistroGuardado // Registra un aporte o una postulación y devuelve lo guardado

    // GET /api/aportes: solo administrador (requiere el token de la sesión)
    @GET("aportes") // Lista todos los registros, los más recientes primero
    suspend fun listar(): List<RegistroGuardado> // Devuelve aportes y postulaciones

    // PATCH /api/aportes/{id}/estado: solo administrador
    @PATCH("aportes/{id}/estado") // Cambia el estado de seguimiento del registro
    suspend fun cambiarEstado(@Path("id") id: Long, @Body cuerpo: EstadoRequestDto): RegistroGuardado // Devuelve el registro actualizado

    // DELETE /api/aportes/{id}: solo administrador (el servidor responde 204 sin cuerpo)
    @DELETE("aportes/{id}") // Elimina el aporte o la postulación
    suspend fun eliminar(@Path("id") id: Long): Response<Unit> // Respuesta completa para poder revisar el código HTTP
}
