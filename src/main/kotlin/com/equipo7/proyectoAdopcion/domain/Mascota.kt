package com.equipo7.proyectoAdopcion.domain

data class Mascota(
    val animalId: Long? = null,
    val nombre: String,
    val descripcion: String,
    val fotoBase64: String,
    val tipo: String,
    val raza: String,
    val codigoPostal: String,
    val usuarioId: Long,
    val estadoPublicacion: String = "DISPONIBLE"
)