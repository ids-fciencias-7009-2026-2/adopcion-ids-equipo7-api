package com.equipo7.proyectoAdopcion.dto.request

/**
 * Representa la solicitud del endpoint HTTP para registrar un nuevo usuario.
 *
 * Este DTO modela el JSON que llega en:
 * POST /usuarios/register
 */
data class CreateUsuarioRequest(
    // Nombre del usuario que se registrará.
    val nombre: String,

    // Correo electrónico del usuario.
    val email: String,

    // Código postal del usuario (ubicación aproximada).
    val codigoPostal: String,

    // Contraseña del usuario (en esta práctica es simulado).
    val password: String
)