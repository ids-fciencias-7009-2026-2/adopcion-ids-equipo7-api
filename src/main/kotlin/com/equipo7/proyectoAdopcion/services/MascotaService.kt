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
                // Se filtran exclusivamente las publicaciones con estado DISPONIBLE
                if (nombreLimpio != null) {
                    mascotaRepository.findByNombreContainingIgnoreCase(nombreLimpio)
                        .filter { it.estadoPublicacion == "DISPONIBLE" }
                } else {
                    mascotaRepository.findAll()
                        .filter { it.estadoPublicacion == "DISPONIBLE" }
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

    fun eliminarPublicacion(
        animalId: Long,
        token: String?
    ) {
        val usuario = obtenerUsuarioAutenticado(token)

        val mascota = mascotaRepository.findById(animalId)
            .orElseThrow { NoSuchElementException("Publicación no encontrada") }

        if (mascota.usuarioId != usuario.id) {
            throw IllegalAccessException("No tienes permiso para eliminar esta publicación")
        }

        if (mascota.estadoPublicacion != "DISPONIBLE") {
            throw IllegalStateException("No se puede eliminar una publicación que ya fue adoptada")
        }

        mascotaRepository.delete(mascota)
    }

    private fun obtenerUsuarioAutenticado(token: String?) =
        if (token.isNullOrBlank()) {
            throw SecurityException("Token no proporcionado")
        } else {
            usuarioService.findByToken(token)
                ?: throw SecurityException("Sesión inválida o expirada")
        }

    fun editarPublicacion(animalId: Long, mascotaModificada: Mascota, token: String?): Mascota {
        val usuario = obtenerUsuarioAutenticado(token)

        val mascotaExistente = mascotaRepository.findById(animalId)
            .orElseThrow { NoSuchElementException("Publicación no encontrada") }

        if (mascotaExistente.usuarioId != usuario.id) {
            throw IllegalAccessException("No tienes permiso para editar esta publicación")
        }

        if (mascotaModificada.usuarioId != mascotaExistente.usuarioId) {
            throw IllegalAccessException("El ID de usuario enviado no coincide con el dueño de la publicación")
        }

        if (mascotaExistente.estadoPublicacion != "DISPONIBLE") {
            throw IllegalStateException("No se puede editar una publicación que ya no está disponible")
        }

        val entidadActualizada = mascotaExistente.copy(
            nombre = mascotaModificada.nombre,
            descripcion = mascotaModificada.descripcion,
            fotoBase64 = mascotaModificada.fotoBase64,
            tipo = mascotaModificada.tipo,
            raza = mascotaModificada.raza,
            codigoPostal = mascotaModificada.codigoPostal
        )

        val mascotaGuardada = mascotaRepository.save(entidadActualizada)
        return mascotaMapper.entityToDomain(mascotaGuardada)
    }

    fun marcarComoAdoptada(animalId: Long, token: String?): Mascota {
        val usuario = obtenerUsuarioAutenticado(token)

        val mascotaExistente = mascotaRepository.findById(animalId)
            .orElseThrow { NoSuchElementException("Publicación no encontrada") }

        if (mascotaExistente.usuarioId != usuario.id) {
            throw IllegalAccessException("No tienes permiso para marcar esta publicación como adoptada")
        }

        if (mascotaExistente.estadoPublicacion != "DISPONIBLE") {
            throw IllegalStateException("La mascota ya fue adoptada o no se encuentra disponible")
        }

        val entidadActualizada = mascotaExistente.copy(
            estadoPublicacion = "ADOPTADO"
        )

        val mascotaGuardada = mascotaRepository.save(entidadActualizada)
        return mascotaMapper.entityToDomain(mascotaGuardada)
    }
}