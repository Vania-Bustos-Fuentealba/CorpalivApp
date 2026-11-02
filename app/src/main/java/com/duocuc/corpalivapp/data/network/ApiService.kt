package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este archivo dentro de la capa de red (Network)

import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de datos OportunidadModel usado para deserializar las respuestas de la API
import retrofit2.http.GET // Importación de la anotación HTTP GET de Retrofit para realizar peticiones de lectura de datos

interface ApiService { // Declaración de la interfaz de servicio de Retrofit que define las peticiones a la API REST

    // Función suspendida para no bloquear pantalla al descargar datos.
    @GET("oportunidades") // Cambia "oportunidades" por el endpoint real de tu API // Anotación Retrofit que indica una petición HTTP GET al endpoint relativo "oportunidades"
    suspend fun getOportunidades(): List<OportunidadModel> // Función de suspensión asíncrona que ejecuta la petición de red y retorna una lista deserializada de OportunidadModel

}