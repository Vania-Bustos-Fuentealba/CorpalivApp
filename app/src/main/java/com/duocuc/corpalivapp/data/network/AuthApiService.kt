package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este archivo dentro de la capa de red (Network)

import com.duocuc.corpalivapp.data.model.LoginRequestDto // Importación del cuerpo de la petición de login
import com.duocuc.corpalivapp.data.model.SesionRemotaDto // Importación de la respuesta del login
import retrofit2.http.Body // Importación de la anotación que envía un objeto como cuerpo JSON
import retrofit2.http.POST // Importación de la anotación HTTP POST de Retrofit

/** PARCIAL 4: peticiones al microservicio de autenticación (auth-service, puerto 8083). */
interface AuthApiService { // Interfaz de Retrofit con los endpoints de autenticación

    // POST /api/auth/login: valida correo y contraseña y entrega un token JWT
    @POST("auth/login") // Ruta relativa a la URL base de auth-service
    suspend fun login(@Body solicitud: LoginRequestDto): SesionRemotaDto // Función de suspensión: no bloquea la pantalla mientras responde el servidor
}
