package com.duocuc.corpalivapp.data.network

import com.duocuc.corpalivapp.data.model.OportunidadModel
import retrofit2.http.GET

interface ApiService {

    // Función suspendida para no bloquear pantalla al descargar datos.
    @GET("oportunidades") // Cambia "oportunidades" por el endpoint real de tu API
    suspend fun getOportunidades(): List<OportunidadModel>

}