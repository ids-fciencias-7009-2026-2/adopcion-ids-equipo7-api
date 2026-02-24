package com.equipo7.proyectoAdopcion.dto.request

/**
 * Representa la solicitud del endpoint HTTP para actualizar información del usuario.
 *
 * Este DTO modela el JSON que llega en:
 * PUT /usuarios
 */
data class UpdateUsuarioRequest(
    // Nuevo nombre del usuario.
    val nombre: String,

    // Nuevo correo electrónico del usuario.
    val email: String,

    // Nuevo código postal del usuario.
    val codigoPostal: String,

    // Nueva contraseña (opcional si solo quieren cambiar otros datos).
    val password: String? = null
)