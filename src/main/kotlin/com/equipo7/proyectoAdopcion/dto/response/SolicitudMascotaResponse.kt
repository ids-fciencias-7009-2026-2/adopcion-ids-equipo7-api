package com.equipo7.proyectoAdopcion.dto.response

data class SolicitudMascotaResponse(
    val mascotaId: Long,
    val nombreMascota: String,
    val foto: String,
    val tipo: String,
    val raza: String,
    val interesados: List<SolicitudInteresadoResponse>
)