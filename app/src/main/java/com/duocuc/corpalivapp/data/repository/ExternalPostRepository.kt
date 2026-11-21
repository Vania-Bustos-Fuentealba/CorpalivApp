
package com.duocuc.corpalivapp.data.repository // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import com.duocuc.corpalivapp.data.network.ExternalApiClient // Importa ExternalApiClient: proporciona la instancia Retrofit de la API externa.
import com.duocuc.corpalivapp.data.network.ExternalApiService // Importa ExternalApiService: declara los endpoints HTTP de las publicaciones.
import com.duocuc.corpalivapp.data.model.ExternalPost // Importa ExternalPost: modela los campos de una publicación recibida desde JSONPlaceholder.

class ExternalPostRepository( // Aísla el acceso a datos para que el ViewModel no dependa directamente de Retrofit.
    private val apiService: ExternalApiService = // Permite inyectar un servicio simulado en las pruebas.
        ExternalApiClient.api // Usa el servicio real cuando no se proporciona uno explícitamente.
) {
    suspend fun obtenerPublicaciones(): List<ExternalPost> { // Expone la consulta de publicaciones a la capa de presentación.
        return apiService.getPosts() // Entrega la respuesta y deja que los errores lleguen al ViewModel.
    }
}
