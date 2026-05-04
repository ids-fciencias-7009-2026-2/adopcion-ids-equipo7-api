package com.equipo7.proyectoAdopcion.repository

import jakarta.persistence.*

@Entity
@Table(name = "animales")
data class MascotaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    val animalId: Long? = null,

    val nombre: String,
    val descripcion: String,

    // Configuración para Base64
    @Column(name = "foto_base64", columnDefinition = "TEXT")
    val fotoBase64: String,

    val tipo: String,
    val raza: String,

    @Column(name = "codigo_postal")
    val codigoPostal: String,

    @Column(name = "usuario_id")
    val usuarioId: Long,

    @Column(name = "estado_publicacion")
    val estadoPublicacion: String = "DISPONIBLE"
)