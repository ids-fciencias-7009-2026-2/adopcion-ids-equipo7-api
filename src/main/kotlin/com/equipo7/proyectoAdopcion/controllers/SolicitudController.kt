package com.equipo7.proyectoAdopcion.controllers

import com.equipo7.proyectoAdopcion.services.SolicitudService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/solicitudes")
class SolicitudController(
    private val solicitudService: SolicitudService
) {

    @GetMapping
    fun listarSolicitudes(
        @RequestHeader("Authorization", required = false) token: String?
    ): ResponseEntity<Any> {
        return try {
            val solicitudes = solicitudService.listarSolicitudes(token)
            ResponseEntity.ok(solicitudes)
        } catch (e: SecurityException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to e.message))
        }
    }
}