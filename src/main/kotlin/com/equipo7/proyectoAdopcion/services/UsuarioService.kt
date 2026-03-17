package com.equipo7.proyectoAdopcion.services

import com.equipo7.proyectoAdopcion.domain.Usuario
import com.equipo7.proyectoAdopcion.dto.request.LoginRequest
import com.equipo7.proyectoAdopcion.dto.request.UpdateUsuarioRequest
import com.equipo7.proyectoAdopcion.repository.UsuarioRepository
import com.equipo7.proyectoAdopcion.repository.toDomain
import com.equipo7.proyectoAdopcion.repository.toEntity
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

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

    fun authenticate(loginRequest: LoginRequest): Usuario? {
        val usuario = findByEmail(loginRequest.email) ?: return null
        val storedPassword = usuario.password ?: return null

        return if (hashPassword(loginRequest.password) == storedPassword) {
            usuario
        } else {
            null
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

    fun updateUsuario(email: String, request: UpdateUsuarioRequest): Usuario? {
        val usuarioExistente = usuarioRepository.findByEmail(email) ?: return null

        if (request.email != email) {
            val usuarioConNuevoEmail = usuarioRepository.findByEmail(request.email)
            if (usuarioConNuevoEmail != null) {
                return null
            }
        }

        usuarioExistente.nombre = request.nombre
        usuarioExistente.email = request.email
        usuarioExistente.codigoPostal = request.codigoPostal

        if (request.password != null) {
            usuarioExistente.password = hashPassword(request.password)
        }

        val savedEntity = usuarioRepository.save(usuarioExistente)
        return savedEntity.toDomain()
    }
}