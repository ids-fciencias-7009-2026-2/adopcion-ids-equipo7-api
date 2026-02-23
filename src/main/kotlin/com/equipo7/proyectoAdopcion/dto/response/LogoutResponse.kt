package com.equipo7.proyectoAdopcion.dto.response

/**
 * Representa la respuesta del endpoint HTTP al hacer logout.
*/
data class LogoutResponse(
    // id del usuario
    val userId: String,
    // hora del logout
    val logoutDateTime: String
)
