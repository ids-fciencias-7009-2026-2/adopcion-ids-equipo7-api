package com.equipo7.proyectoAdopcion.dto.request

data class CreateMascotaRequest(
    val nombre: String,
    val descripcion: String,
    val fotoBase64: String,
    val tipo: String,
    val raza: String,
    val codigoPostal: String,
    val usuarioId: Long
)