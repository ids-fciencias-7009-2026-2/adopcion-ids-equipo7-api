package com.equipo7.proyectoAdopcion.repository

import com.equipo7.proyectoAdopcion.domain.Mascota
import com.equipo7.proyectoAdopcion.dto.request.CreateMascotaRequest
import com.equipo7.proyectoAdopcion.dto.response.MascotaResponse
import org.springframework.stereotype.Component

@Component
class MascotaMapper {

    fun requestToDomain(request: CreateMascotaRequest): Mascota {
        return Mascota(
            nombre = request.nombre,
            descripcion = request.descripcion,
            fotoBase64 = request.fotoBase64,
            tipo = request.tipo,
            raza = request.raza,
            codigoPostal = request.codigoPostal,
            usuarioId = request.usuarioId
        )
    }

    fun domainToEntity(domain: Mascota): MascotaEntity {
        return MascotaEntity(
            animalId = domain.animalId,
            nombre = domain.nombre,
            descripcion = domain.descripcion,
            fotoBase64 = domain.fotoBase64,
            tipo = domain.tipo,
            raza = domain.raza,
            codigoPostal = domain.codigoPostal,
            usuarioId = domain.usuarioId,
            estadoPublicacion = domain.estadoPublicacion
        )
    }

    fun entityToDomain(entity: MascotaEntity): Mascota {
        return Mascota(
            animalId = entity.animalId,
            nombre = entity.nombre,
            descripcion = entity.descripcion,
            fotoBase64 = entity.fotoBase64,
            tipo = entity.tipo,
            raza = entity.raza,
            codigoPostal = entity.codigoPostal,
            usuarioId = entity.usuarioId,
            estadoPublicacion = entity.estadoPublicacion
        )
    }

    fun domainToResponse(domain: Mascota): MascotaResponse {
        return MascotaResponse(
            animalId = domain.animalId!!,
            nombre = domain.nombre,
            descripcion = domain.descripcion,
            fotoBase64 = domain.fotoBase64,
            tipo = domain.tipo,
            raza = domain.raza,
            codigoPostal = domain.codigoPostal,
            usuarioId = domain.usuarioId,
            estadoPublicacion = domain.estadoPublicacion
        )
    }
}