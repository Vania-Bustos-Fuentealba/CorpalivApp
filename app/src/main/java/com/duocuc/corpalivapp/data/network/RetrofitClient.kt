package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este cliente HTTP dentro de la capa de red (Network)

import retrofit2.Retrofit // Importación de la clase principal del framework Retrofit para construir y configurar cliente REST
import retrofit2.converter.gson.GsonConverterFactory // Importación del convertidor de Gson para deserializar automáticamente respuestas JSON a objetos Kotlin

object RetrofitClient { // Declaración de un objeto Singleton para garantizar una única instancia global del cliente HTTP en la app

    // URL base de API rest, la colocaremos aquí
    private const val BASE_URL = "https://run.mocky.io/v3/" // Constante privada con la dirección web base para todas las llamadas a la API

    val apiService: ApiService by lazy { // Propiedad delegada 'by lazy' que inicializa el servicio de forma diferida en su primer acceso
        Retrofit.Builder() // Inicia el patrón Builder para construir y configurar la instancia de Retrofit
            .baseUrl(BASE_URL) // Define la URL base de la red sobre la cual se resolverán las peticiones
            .addConverterFactory(GsonConverterFactory.create()) // Añade la fábrica del convertidor Gson para transformar JSON a data classes
            .build() // Construye la instancia configurada de Retrofit
            .create(ApiService::class.java) // Crea e implementa dinámicamente la interfaz ApiService con los endpoints configurados
    }
}