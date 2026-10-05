package com.duocuc.corpalivapp.data.model

data class OportunidadModel(
    val id: Int,
    val titulo: String,             // Ej: "Programa Inclusión Laboral Alba Lab"
    val categoria: String,          // Ej: "RSE", "Donación", "Voluntariado"
    val descripcion: String,        // Descripción corta de la iniciativa
    val impactoSocial: String,      // Ej: "Financia herramientas de trabajo para 15 jóvenes"
    val imagenUrl: String,          // URL de imagen ficticia/stock (Unsplash)
    val contactoEmail: String       // Ej: "alianzas@corpaliv-demo.cl" (ficticio)
)