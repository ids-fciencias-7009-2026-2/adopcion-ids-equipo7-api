package com.equipo7.proyectoAdopcion.repository

import com.equipo7.proyectoAdopcion.domain.Usuario

/**
 * Convierte una entidad de base de datos en un objeto de dominio.
 * Se usa cuando recuperamos datos (ej. findByEmail).
 */
fun UsuarioEntity.toDomain(): Usuario {
    return Usuario(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        codigoPostal = this.codigoPostal,
        password = this.password,
        token = this.token
    )
}

/**
 * Convierte un objeto de dominio en una entidad de base de datos.
 * Se usa cuando queremos guardar o actualizar datos (ej. save).
 */
fun Usuario.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        codigoPostal = this.codigoPostal,
        password = this.password,
        token = this.token
    )
}