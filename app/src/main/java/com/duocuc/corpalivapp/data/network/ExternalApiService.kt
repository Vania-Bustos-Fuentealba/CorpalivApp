package com.duocuc.corpalivapp.data.network // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import com.duocuc.corpalivapp.data.model.ExternalPost // Importa ExternalPost: modela los campos de una publicación recibida desde JSONPlaceholder.
import retrofit2.http.GET // Importa GET: identifica una petición HTTP de lectura.

// Endpoints disponibles.
interface ExternalApiService { // Declara las operaciones que Retrofit implementará para la API externa.

    @GET("posts") // Solicita GET /posts sobre la dirección base del cliente.
    suspend fun getPosts(): List<ExternalPost> // Devuelve las publicaciones sin bloquear el hilo mientras espera la red.
}
