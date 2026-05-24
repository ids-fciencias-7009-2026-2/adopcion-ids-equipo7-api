package com.equipo7.proyectoAdopcion.services

import com.equipo7.proyectoAdopcion.dto.response.SolicitudInteresadoResponse
import com.equipo7.proyectoAdopcion.dto.response.SolicitudMascotaResponse
import com.equipo7.proyectoAdopcion.repository.InteresAdopcionRepository
import com.equipo7.proyectoAdopcion.repository.MascotaRepository
import com.equipo7.proyectoAdopcion.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class SolicitudService(
    private val mascotaRepository: MascotaRepository,
    private val interesAdopcionRepository: InteresAdopcionRepository,
    private val usuarioRepository: UsuarioRepository,
    private val usuarioService: UsuarioService
) {

    private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun listarSolicitudes(token: String?): List<SolicitudMascotaResponse> {
        val duenio = obtenerUsuarioAutenticado(token)

        val mascotasDelDuenio = mascotaRepository.findByUsuarioId(duenio.id)

        if (mascotasDelDuenio.isEmpty()) {
            return emptyList()
        }

        val idsMascotas = mascotasDelDuenio.mapNotNull { it.animalId }

        val intereses = interesAdopcionRepository
            .findByAnimalIdInOrderByFechaInteresDesc(idsMascotas)

        val idsInteresados = intereses
            .map { it.usuarioId }
            .distinct()

        val usuariosInteresados = usuarioRepository
            .findAllById(idsInteresados)
            .associateBy { it.id }

        val interesesPorMascota = intereses.groupBy { it.animalId }

        return mascotasDelDuenio.mapNotNull { mascota ->
            val mascotaId = mascota.animalId ?: return@mapNotNull null

            val interesados = interesesPorMascota[mascotaId]
                .orEmpty()
                .mapNotNull { interes ->
                    val usuarioInteresado = usuariosInteresados[interes.usuarioId]
                        ?: return@mapNotNull null

                    SolicitudInteresadoResponse(
                        nombre = usuarioInteresado.nombre,
                        email = usuarioInteresado.email,
                        fecha = interes.fechaInteres.format(formatoFecha)
                    )
                }

            SolicitudMascotaResponse(
                mascotaId = mascotaId,
                nombreMascota = mascota.nombre,
                foto = prepararFoto(mascota.fotoBase64),
                tipo = mascota.tipo,
                raza = mascota.raza,
                interesados = interesados
            )
        }
    }

    private fun prepararFoto(fotoBase64: String): String {
        if (fotoBase64.isBlank()) {
            return ""
        }

        return if (fotoBase64.startsWith("data:")) {
            fotoBase64
        } else {
            "data:image/jpeg;base64,$fotoBase64"
        }
    }

    private fun obtenerUsuarioAutenticado(token: String?) =
        if (token.isNullOrBlank()) {
            throw SecurityException("Token no proporcionado")
        } else {
            usuarioService.findByToken(token)
                ?: throw SecurityException("Sesión inválida o expirada")
        }
}