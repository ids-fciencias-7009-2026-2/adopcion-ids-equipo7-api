package com.equipo7.proyectoAdopcion.services

import com.equipo7.proyectoAdopcion.domain.Mascota
import com.equipo7.proyectoAdopcion.repository.MascotaMapper
import com.equipo7.proyectoAdopcion.repository.MascotaRepository
import org.springframework.stereotype.Service

@Service
class MascotaService(
    private val mascotaRepository: MascotaRepository,
    private val mascotaMapper: MascotaMapper
) {

    // Crear publicación
    fun publicarMascota(mascota: Mascota): Mascota {
        val entity = mascotaMapper.domainToEntity(mascota)
        val savedEntity = mascotaRepository.save(entity)
        return mascotaMapper.entityToDomain(savedEntity)
    }

    // Obtener detalle
    fun obtenerDetalleMascota(id: Long): Mascota? {
        val entityOptional = mascotaRepository.findById(id)
        return if (entityOptional.isPresent) {
            mascotaMapper.entityToDomain(entityOptional.get())
        } else {
            null
        }
    }
}