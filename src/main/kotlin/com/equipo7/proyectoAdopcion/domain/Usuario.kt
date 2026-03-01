package com.equipo7.proyectoAdopcion.domain

/**
 * Representa el modelo de dominio "Usuario" para el proyecto de adopción.
 */

data class Usuario(
    val id: String,
    var nombre: String,
    var email: String,
    var codigoPostal: String,
    var password: String? = null,
    var token: String? = null
)
