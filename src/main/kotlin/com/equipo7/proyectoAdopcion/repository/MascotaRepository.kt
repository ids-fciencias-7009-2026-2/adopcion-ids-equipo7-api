package com.equipo7.proyectoAdopcion.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MascotaRepository : JpaRepository<MascotaEntity, Long> {
    fun findByNombreContainingIgnoreCase(nombre: String): List<MascotaEntity>
    
    fun findByUsuarioId(usuarioId: String): List<MascotaEntity>
    
    fun findByAnimalIdIn(animalIds: List<Long>): List<MascotaEntity>
    
    fun findByUsuarioIdAndNombreContainingIgnoreCase(
        usuarioId: String,
        nombre: String
    ): List<MascotaEntity>
    
    fun findByAnimalIdInAndNombreContainingIgnoreCase(
        animalIds: List<Long>,
        nombre: String
    ): List<MascotaEntity>
}