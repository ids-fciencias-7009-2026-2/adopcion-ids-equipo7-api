package com.equipo7.proyectoAdopcion.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<UsuarioEntity, String> {

    /**
     * Busca un usuario por su correo electronico.
     * Requerido por el UsuarioService para procesos de login y busqueda.
     */
    fun findByEmail(email: String): UsuarioEntity?

    /**
     * Busca un usuario mediante su token de sesión activo.
     * Requerido por el UsuarioService para validar la sesión en /me y /logout.
     */
    fun findByToken(token: String): UsuarioEntity?

    // El metodo .save() ya viene heredado de JpaRepository
}