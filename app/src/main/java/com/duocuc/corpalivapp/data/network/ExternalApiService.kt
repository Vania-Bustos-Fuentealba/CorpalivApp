package com.duocuc.corpalivapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Modelo para recibir publicaciones de la API externa.
data class ExternalPost(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// Endpoints disponibles.
interface ExternalApiService {

    @GET("posts")
    suspend fun getPosts(): List<ExternalPost>
}

// Cliente independiente del backend de CorpalivApp.
object ExternalApiClient {

    private const val BASE_URL =
        "https://jsonplaceholder.typicode.com/"

    val api: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}
