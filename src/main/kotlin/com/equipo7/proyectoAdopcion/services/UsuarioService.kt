package com.equipo7.proyectoAdopcion.services

import com.equipo7.proyectoAdopcion.domain.Usuario
import com.equipo7.proyectoAdopcion.dto.request.LoginRequest
import com.equipo7.proyectoAdopcion.dto.request.UpdateUsuarioRequest
import com.equipo7.proyectoAdopcion.repository.UsuarioRepository
import com.equipo7.proyectoAdopcion.repository.toDomain
import com.equipo7.proyectoAdopcion.repository.toEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.UUID

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

    val logger = LoggerFactory.getLogger(UsuarioService::class.java)

    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun findByEmail(email: String): Usuario? {
        val entity = usuarioRepository.findByEmail(email)
        return entity?.toDomain()
    }

    fun findByToken(token: String): Usuario? {
        val entity = usuarioRepository.findByToken(token)
        logger.info("Usuario exists: ${entity.toString()}")
        return entity?.toDomain()
    }

    fun authenticate(loginRequest: LoginRequest): Usuario? {
        val entity = usuarioRepository.findByEmail(loginRequest.email) ?: return null
        val storedPassword = entity.password ?: return null

        return if (hashPassword(loginRequest.password) == storedPassword) {
            // Generaramos token
            val token = UUID.randomUUID().toString()
            entity.token = token
            // Guardar en base de datos el usuario actualizado con su nuevo token
            val savedEntity = usuarioRepository.save(entity)
            savedEntity.toDomain()
        } else {
            null
        }
    }

    fun logout(token: String) {
        val entity = usuarioRepository.findByToken(token)
        if (entity != null) {
            entity.token = null // Borramos el token
            usuarioRepository.save(entity)
        }
    }

    fun addNewUsuario(usuario: Usuario): Usuario? {
        val existente = usuarioRepository.findByEmail(usuario.email)
        if (existente != null) {
            return null
        }

        val usuarioConPasswordHash = usuario.copy(
            password = usuario.password?.let { hashPassword(it) }
        )

        val savedEntity = usuarioRepository.save(usuarioConPasswordHash.toEntity())
        return savedEntity.toDomain()
    }

    fun updateUsuario(emailActual: String, request: UpdateUsuarioRequest): Usuario? {
        val usuarioExistente = usuarioRepository.findByEmail(emailActual) ?: return null

        if (request.email != emailActual) {
            val correoOcupado = usuarioRepository.findByEmail(request.email)
            if (correoOcupado != null) {
                return null
            }
        }

        usuarioExistente.nombre = request.nombre
        usuarioExistente.email = request.email
        usuarioExistente.codigoPostal = request.codigoPostal

        // SEGURIDAD: Solo se hashea y se cambia la contraseña si el usuario envió una nueva
        if (!request.password.isNullOrBlank()) {
            usuarioExistente.password = hashPassword(request.password)
        }

        val savedEntity = usuarioRepository.save(usuarioExistente)
        return savedEntity.toDomain()
    }
}