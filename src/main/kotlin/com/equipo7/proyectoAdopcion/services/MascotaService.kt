package com.equipo7.proyectoAdopcion.services

import com.equipo7.proyectoAdopcion.domain.Mascota
import com.equipo7.proyectoAdopcion.dto.response.InteresAdopcionResponse
import com.equipo7.proyectoAdopcion.repository.InteresAdopcionEntity
import com.equipo7.proyectoAdopcion.repository.InteresAdopcionRepository
import com.equipo7.proyectoAdopcion.repository.MascotaMapper
import com.equipo7.proyectoAdopcion.repository.MascotaRepository
import org.springframework.stereotype.Service

@Service
class MascotaService(
    private val mascotaRepository: MascotaRepository,
    private val mascotaMapper: MascotaMapper,
    private val usuarioService: UsuarioService,
    private val interesAdopcionRepository: InteresAdopcionRepository
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

    fun listarPublicaciones(
        nombre: String?,
        filtro: String?,
        token: String?
    ): List<Mascota> {
        val nombreLimpio = nombre?.trim()?.takeIf { it.isNotBlank() }
        val filtroLimpio = filtro?.trim()?.lowercase() ?: "todas"

        val entidades = when (filtroLimpio) {
            "mis-publicaciones" -> {
                val usuario = obtenerUsuarioAutenticado(token)
                if (nombreLimpio != null) {
                    mascotaRepository.findByUsuarioIdAndNombreContainingIgnoreCase(
                        usuario.id,
                        nombreLimpio
                    )
                } else {
                    mascotaRepository.findByUsuarioId(usuario.id)
                }
            }

            "me-interesa" -> {
                val usuario = obtenerUsuarioAutenticado(token)
                val animalIds = interesAdopcionRepository.findByUsuarioId(usuario.id)
                    .map { it.animalId }

                if (animalIds.isEmpty()) {
                    emptyList()
                } else if (nombreLimpio != null) {
                    mascotaRepository.findByAnimalIdInAndNombreContainingIgnoreCase(
                        animalIds,
                        nombreLimpio
                    )
                } else {
                    mascotaRepository.findByAnimalIdIn(animalIds)
                }
            }

            else -> {
                if (nombreLimpio != null) {
                    mascotaRepository.findByNombreContainingIgnoreCase(nombreLimpio)
                } else {
                    mascotaRepository.findAll()
                }
            }
        }

        return entidades.map { mascotaMapper.entityToDomain(it) }
    }

    fun registrarInteres(
        animalId: Long,
        token: String?
    ): InteresAdopcionResponse {
        val usuario = obtenerUsuarioAutenticado(token)

        val mascotaExiste = mascotaRepository.existsById(animalId)
        if (!mascotaExiste) {
            throw NoSuchElementException("Publicación no encontrada")
        }

        val yaExiste = interesAdopcionRepository.existsByUsuarioIdAndAnimalId(
            usuario.id,
            animalId
        )

        if (yaExiste) {
            throw IllegalStateException("Ya registraste interés en esta mascota")
        }

        val interes = InteresAdopcionEntity(
            usuarioId = usuario.id,
            animalId = animalId
        )

        interesAdopcionRepository.save(interes)

        return InteresAdopcionResponse(
            mensaje = "Interés registrado correctamente",
            animalId = animalId,
            usuarioId = usuario.id
        )
    }

    private fun obtenerUsuarioAutenticado(token: String?) =
        if (token.isNullOrBlank()) {
            throw SecurityException("Token no proporcionado")
        } else {
            usuarioService.findByToken(token)
                ?: throw SecurityException("Sesión inválida o expirada")
        }

}