package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este archivo dentro de la capa de red (Network)

import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de datos OportunidadModel usado para deserializar las respuestas de la API
import com.duocuc.corpalivapp.data.model.OportunidadRequestDto // Cuerpo de la petición para crear o modificar una oportunidad (Parcial 4)
import retrofit2.Response // Respuesta completa de Retrofit, usada cuando el servidor responde sin cuerpo (204)
import retrofit2.http.Body // Anotación que envía un objeto como cuerpo JSON
import retrofit2.http.DELETE // Anotación HTTP DELETE de Retrofit
import retrofit2.http.PUT // Anotación HTTP PUT de Retrofit
import retrofit2.http.POST // Anotación HTTP POST de Retrofit
import retrofit2.http.Path // Anotación que inserta un valor en la ruta
import retrofit2.http.GET // Importación de la anotación HTTP GET de Retrofit para realizar peticiones de lectura de datos

interface ApiService { // Declaración de la interfaz de servicio de Retrofit que define las peticiones a la API REST

    // Función suspendida para no bloquear pantalla al descargar datos.
    @GET("oportunidades") // Cambia "oportunidades" por el endpoint real de tu API // Anotación Retrofit que indica una petición HTTP GET al endpoint relativo "oportunidades"
    suspend fun getOportunidades(): List<OportunidadModel> // Función de suspensión asíncrona que ejecuta la petición de red y retorna una lista deserializada de OportunidadModel

    // PARCIAL 4: crear, modificar y eliminar oportunidades (solo administradores: el cliente HTTP envía el token de la sesión)
    @POST("oportunidades") // Crea una oportunidad nueva
    suspend fun crearOportunidad(@Body solicitud: OportunidadRequestDto): OportunidadModel // Devuelve la oportunidad creada con su id

    @PUT("oportunidades/{id}") // Reemplaza los datos de una oportunidad existente
    suspend fun actualizarOportunidad(@Path("id") id: Int, @Body solicitud: OportunidadRequestDto): OportunidadModel // Devuelve la oportunidad actualizada

    @DELETE("oportunidades/{id}") // Elimina una oportunidad (el servidor responde 204 sin cuerpo)
    suspend fun eliminarOportunidad(@Path("id") id: Int): Response<Unit> // Respuesta completa para poder revisar el código HTTP

}