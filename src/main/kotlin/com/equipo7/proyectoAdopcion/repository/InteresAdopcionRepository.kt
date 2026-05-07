package com.equipo7.proyectoAdopcion.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InteresAdopcionRepository : JpaRepository<InteresAdopcionEntity, Long> {

    fun existsByUsuarioIdAndAnimalId(usuarioId: String, animalId: Long): Boolean

    fun findByUsuarioId(usuarioId: String): List<InteresAdopcionEntity>
}