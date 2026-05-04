package com.equipo7.proyectoAdopcion.dto.response

data class MascotaResponse(
    val animalId: Long,
    val nombre: String,
    val descripcion: String,
    val fotoBase64: String,
    val tipo: String,
    val raza: String,
    val codigoPostal: String,
    val usuarioId: Long,
    val estadoPublicacion: String
)