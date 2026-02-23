package com.equipo7.proyectoAdopcion.controllers

import com.equipo7.proyectoAdopcion.domain.Usuario
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controlador para la gestión de usuarios del sistema de adopción.
 */
@Controller
@RequestMapping("/usuarios") // Prefijo base para todos los endpoints de este controlador
class UsuarioController {

    /**
     * Logger para registrar eventos importantes del flujo de ejecución.
     *
     * Permite imprimir información útil en consola para depuración,
     * auditoría y análisis del comportamiento del sistema.
     */
    private val logger: Logger = LoggerFactory.getLogger(UsuarioController::class.java)

    /**
     * Endpoint para obtener la información del usuario actual.
     *
     * Utiliza datos simulados (fake) para esta etapa de la practica.
     *
     * URL: http://localhost:8080/usuarios/me
     * Metodo: GET
     *
     * @return ResponseEntity con un objeto Usuario y código HTTP 200 (OK).
     */
    @GetMapping("/me")
    fun retrieveUsuario(): ResponseEntity<Usuario> {

        // Simulación de un usuario registrado en la plataforma
        // Se debe proporcionar como minimo esta información
        val usuarioFake = Usuario(
            id = "123",
            nombre = "Usuario1",
            email = "usuario1@ciencias.unam.mx",
            codigoPostal = "04510"
        )

        logger.info("User found in database: ${usuarioFake.nombre}")

        // Retorna HTTP 200 junto con el usuario encontrado
        return ResponseEntity.ok(usuarioFake)
    }
}