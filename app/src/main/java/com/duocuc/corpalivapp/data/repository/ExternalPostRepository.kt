
package com.duocuc.corpalivapp.data.repository

import com.duocuc.corpalivapp.data.network.ExternalApiClient
import com.duocuc.corpalivapp.data.network.ExternalApiService
import com.duocuc.corpalivapp.data.network.ExternalPost

class ExternalPostRepository(
    private val apiService: ExternalApiService =
        ExternalApiClient.api
) {
    suspend fun obtenerPublicaciones(): List<ExternalPost> {
        return apiService.getPosts()
    }
}
