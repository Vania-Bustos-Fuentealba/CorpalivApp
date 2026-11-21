package com.duocuc.corpalivapp.data.network // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import retrofit2.Retrofit // Importa Retrofit: construye la implementación del servicio HTTP.
import retrofit2.converter.gson.GsonConverterFactory // Importa GsonConverterFactory: convierte las respuestas JSON en objetos Kotlin.

/** Cliente separado del backend: nunca envía el token de Corpaliv a la API externa. */
object ExternalApiClient { // Define un único cliente compartido para la API externa.
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/" // Dirección base; la barra final es necesaria para resolver las rutas de Retrofit.

    val api: ExternalApiService by lazy { // Crea el servicio solamente cuando se utiliza por primera vez.
        Retrofit.Builder() // Empieza la configuración del cliente HTTP.
            .baseUrl(BASE_URL) // Establece la dirección a la que se añadirán los endpoints.
            .addConverterFactory(GsonConverterFactory.create()) // Activa la conversión automática entre JSON y objetos Kotlin.
            .build() // Construye Retrofit con las opciones configuradas.
            .create(ExternalApiService::class.java) // Genera la implementación HTTP de la interfaz ExternalApiService.
    }
}
