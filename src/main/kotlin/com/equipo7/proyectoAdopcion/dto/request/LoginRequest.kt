package com.equipo7.proyectoAdopcion.dto.request
/**
 * Representa la solicitud del endpoint HTTP al hacer login.
*/
data class LoginRequest(
    //Correo electrónico del usuario que intenta autenticarse.
    val email: String,
    // Contraseña ingresada por el usuario.
    val password: String
)
