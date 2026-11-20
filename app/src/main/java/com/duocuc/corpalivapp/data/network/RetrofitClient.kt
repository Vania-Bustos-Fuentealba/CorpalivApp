package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este cliente HTTP dentro de la capa de red (Network)

import okhttp3.Interceptor // Importación del interceptor de OkHttp para modificar cada petición antes de enviarla
import okhttp3.OkHttpClient // Importación del cliente HTTP de OkHttp que usa Retrofit por debajo
import java.util.concurrent.TimeUnit // Importación de las unidades de tiempo usadas en los tiempos de espera de la red
import retrofit2.Retrofit // Importación de la clase principal del framework Retrofit para construir y configurar cliente REST
import retrofit2.converter.gson.GsonConverterFactory // Importación del convertidor de Gson para deserializar automáticamente respuestas JSON a objetos Kotlin

object RetrofitClient { // Declaración de un objeto Singleton para garantizar una única instancia global del cliente HTTP en la app

    // URL base de API rest, la colocaremos aquí
    // Parcial 4: microservicios Spring Boot propios. En el emulador de Android Studio, 10.0.2.2 es el computador donde corren.
    // En un celular físico se cambia por la IP del computador en la misma red Wi-Fi.
    private const val HOST = "http://10.0.2.2" // Dirección del computador visto desde el emulador de Android
    private const val BASE_URL = "http://10.0.2.2:8081/api/" // Constante privada con la dirección web base para todas las llamadas a la API

    private const val BASE_URL_AUTH = "$HOST:8083/api/" // Base del microservicio de autenticación (auth-service)
    private const val BASE_URL_APORTES = "$HOST:8082/api/" // Base del microservicio de aportes y postulaciones (aportes-service)

    // Cliente HTTP común: agrega el token JWT de la sesión (si existe) y corta las esperas largas
    private val clienteHttp: OkHttpClient by lazy { // Se crea una sola vez, en el primer uso
        OkHttpClient.Builder() // Inicia la construcción del cliente HTTP
            .connectTimeout(10, TimeUnit.SECONDS) // Máximo de espera para conectar con el servidor
            .readTimeout(10, TimeUnit.SECONDS) // Máximo de espera para recibir la respuesta
            .addInterceptor(Interceptor { cadena -> // Interceptor que se ejecuta en cada petición saliente
                val token = SesionToken.valor // Token de la sesión activa (null si no hay sesión)
                val peticion = if (token.isNullOrBlank()) { // Sin sesión: la petición sale tal cual
                    cadena.request()
                } else { // Con sesión: se agrega la cabecera Authorization con el token
                    cadena.request().newBuilder().header("Authorization", "Bearer $token").build()
                }
                cadena.proceed(peticion) // Envía la petición y retorna la respuesta del servidor
            })
            .build() // Construye el cliente configurado
    }

    val apiService: ApiService by lazy { // Propiedad delegada 'by lazy' que inicializa el servicio de forma diferida en su primer acceso
        Retrofit.Builder() // Inicia el patrón Builder para construir y configurar la instancia de Retrofit
            .baseUrl(BASE_URL) // Define la URL base de la red sobre la cual se resolverán las peticiones
            .client(clienteHttp) // Usa el cliente que agrega el token de la sesión a las peticiones
            .addConverterFactory(GsonConverterFactory.create()) // Añade la fábrica del convertidor Gson para transformar JSON a data classes
            .build() // Construye la instancia configurada de Retrofit
            .create(ApiService::class.java) // Crea e implementa dinámicamente la interfaz ApiService con los endpoints configurados
    }

    val authApi: AuthApiService by lazy { // Servicio de autenticación (login con JWT) creado de forma diferida
        Retrofit.Builder() // Inicia el patrón Builder para el microservicio de autenticación
            .baseUrl(BASE_URL_AUTH) // Dirección base de auth-service
            .client(clienteHttp) // Mismo cliente HTTP con tiempos de espera y token
            .addConverterFactory(GsonConverterFactory.create()) // Convierte el JSON de las respuestas a objetos Kotlin
            .build() // Construye la instancia de Retrofit
            .create(AuthApiService::class.java) // Implementa la interfaz AuthApiService
    }

    val aportesApi: AportesApiService by lazy { // Servicio de aportes y postulaciones creado de forma diferida
        Retrofit.Builder() // Inicia el patrón Builder para el microservicio de aportes
            .baseUrl(BASE_URL_APORTES) // Dirección base de aportes-service
            .client(clienteHttp) // Mismo cliente HTTP con tiempos de espera y token
            .addConverterFactory(GsonConverterFactory.create()) // Convierte el JSON de las respuestas a objetos Kotlin
            .build() // Construye la instancia de Retrofit
            .create(AportesApiService::class.java) // Implementa la interfaz AportesApiService
    }
}
