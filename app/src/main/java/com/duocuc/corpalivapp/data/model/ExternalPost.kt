package com.duocuc.corpalivapp.data.model // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

/** Publicación recibida desde JSONPlaceholder. */
data class ExternalPost( // Agrupa los datos de una publicación; Kotlin genera equals, copy y otras utilidades.
    val userId: Int, // Identificador del autor de la publicación en la API.
    val id: Int, // Identificador único de la publicación.
    val title: String, // Título que se mostrará en la tarjeta de la publicación.
    val body: String // Contenido completo de la publicación.
)
