package com.equipo7.proyectoAdopcion.repository

import jakarta.persistence.*

/**
 * Entidad JPA que representa la tabla 'usuario' en la base de datos.
 * Mapea los campos definidos en el script SQL.
 */
@Entity
@Table(name = "usuario")
class UsuarioEntity(

    @Id
    @Column(length = 50)
    var id: String = "", // Clave primaria (PK) de tipo VARCHAR(50)

    @Column(nullable = false, length = 100)
    var nombre: String = "",

    @Column(nullable = false, length = 100, unique = true)
    var email: String = "",

    @Column(name = "codigo_postal", nullable = false, length = 10)
    var codigoPostal: String = "",

    @Column(length = 255)
    var password: String? = null, // Almacenado como hash para seguridad

    @Column(length = 255)
    var token: String? = null
)